<template>
  <div id="my-order" style="background-color: #f7f7f7">
    <van-nav-bar
      left-arrow
      title="我的订单"
      @click-left="MixinRouterBack"
    >
      <van-icon slot="right" name="search" @click="showSearchPopup = true"/>
    </van-nav-bar>
    <van-tabs v-model="tabActive" :swipe-threshold="5" @change="handleTabChange">
      <van-tab title="全部"/>
      <van-tab title="待付款"/>
      <van-tab title="待发货"/>
      <van-tab title="待收货"/>
      <van-tab title="待评论"/>
    </van-tabs>
    <div class="order-container">
      <empty-member v-if="finished && !orderList.length">暂无订单</empty-member>
      <van-list
        v-else
        v-model="loading"
        :finished="finished"
        @load="onLoad"
      >
        <div class="order-item" v-for="(order, index) in orderList" :key="index">
          <div class="sn-order-item">
            <span>订单号：{{ order.sn }}</span>
            <span v-show="order.order_type === 'pintuan'"> {{ order.ping_tuan_status }} </span>
          </div>
          <div class="info-order-item">
            <p><span>状<i></i>态：</span><em style="color: #3985ff;">{{ order.order_status_text }}</em></p>
            <p><span>总<i></i>价：</span><em class="price">￥{{ order.order_amount | unitPrice }}</em></p>
            <div class="order-btns">
              <a v-if="order.order_operate_allowable_vo.allow_cancel" @click="handleCancelOrder(order.sn)" style="background-color: #f19325">取消订单</a>
              <nuxt-link v-if="order.order_operate_allowable_vo.allow_service_cancel" :to="'/member/after-sale/apply?order_sn=' + order.sn" style="background-color: #f19325">取消订单</nuxt-link>
              <a v-if="order.order_operate_allowable_vo.allow_rog" @click="handleRogOrder(order.sn)">确认收货</a>
              <nuxt-link v-if="order.order_operate_allowable_vo.allow_pay" :to="'/checkout/cashier?order_sn=' + order.sn">订单付款</nuxt-link>
              <nuxt-link v-if="order.order_operate_allowable_vo.allow_comment" :to="'/member/comments?order_sn=' + order.sn">去评论</nuxt-link>
              <nuxt-link v-if="order.order_operate_allowable_vo.allow_apply_service" :to="'/member/after-sale/apply?order_sn=' + order.sn">申请售后</nuxt-link>
              <nuxt-link :to="'./my-order/detail?order_sn=' + order.sn">查看详情</nuxt-link>
            </div>
          </div>
          <a :href="'/shop/' + order.seller_id" class="shop-order-item">
            <em>{{ order.seller_name }}</em>
          </a>
          <div class="sku-order-item" v-for="(sku, index) in order.sku_list" :key="index">
            <div class="sku-content">
              <a :href="'/goods/' + sku.goods_id" class="img-info">
                <img :src="sku.goods_image" :alt="sku.name">
                <span v-if="order.order_type === 'pintuan'">多人拼团</span>
              </a>
              <nuxt-link :to="'./my-order/detail?order_sn=' + order.sn" style="margin-top: 10px">
                <div style="margin-top: 3px" class="sku-name">{{ sku.name }}</div>
                <p>
                  <span class="sku-spec" style="margin-right: 5px">{{ sku | formatterSkuSpec }}</span>
                  <span>{{ sku.num }}件</span>
                  <span class="sku-assemble" v-if="order.waiting_group_nums">还差<em>{{ order.waiting_group_nums }}</em>人成团</span>
                </p>
                <p v-if="sku.goods_operate_allowable_vo.allow_apply_service" class="after-btn">
                  <nuxt-link :to="'/member/after-sale/apply?order_sn=' + order.sn + '&sku_id=' + sku.sku_id">申请售后</nuxt-link>
                </p>
              </nuxt-link>
            </div>
          </div>
        </div>
      </van-list>
    </div>
    <van-dialog
      v-model="showCancelDialog"
      title="取消订单"
      show-cancel-button
      :before-close="cancelBeforeClose"
    >
      <van-field
        v-model="reason"
        type="textarea"
        rows="1"
        autosize
        placeholder="请输入取消原因"
      />
    </van-dialog>
    <van-popup
      v-model="showSearchPopup"
      position="top"
    >
      <form action="/">
        <van-search
          placeholder="请输入商品关键字"
          v-model="searchKeyword"
          @search="handleSearchOrder"
        />
      </form>
    </van-popup>
  </div>
</template>

<script>
  import * as API_Order from '@/api/order'
  export default {
    name: 'my-order-index',
    data() {
      const { order_status } = this.$route.query
      return {
        // 加载中
        loading: false,
        // 是否全部已加载完成
        finished: false,
        // 当前tab的index
        tabActive: this.getParam(order_status),
        params: {
          page_no: 0,
          page_size: 5,
          order_status
        },
        orderList: [],
        // 显示取消订单dialog
        showCancelDialog: false,
        // 取消订单原因
        reason: '',
        // 显示搜索框
        showSearchPopup: false,
        // 搜索关键字
        searchKeyword: ''
      }
    },
    methods: {
      /** tabIndex发生改变 */
      handleTabChange(index) {
        this.finished = false
        this.orderList = []
        this.params.page_no = 1
        this.params.order_status = this.getParam(index)
        this.GET_OrderList()
      },
      /** 根据订单状态获取tabActive */
      getParam(param) {
        switch (param) {
          case 0: return ''
          case 1: return 'WAIT_PAY'
          case 2: return 'WAIT_SHIP'
          case 3: return 'WAIT_ROG'
          case 4: return 'WAIT_COMMENT'
          case '': return 0
          case 'WAIT_PAY': return 1
          case 'WAIT_SHIP': return 2
          case 'WAIT_ROG': return 3
          case 'WAIT_COMMENT': return 4
        }
      },
      /** 加载数据 */
      onLoad() {
        this.params.page_no += 1
        this.GET_OrderList()
      },
      /** 取消dialog关闭前校验 */
      cancelBeforeClose(action, done) {
        if (action === 'confirm') {
          const { reason, cancel_sn } = this
          if (!reason) {
            this.$message.error('请填写取消原因！')
            done(false)
          } else {
            API_Order.cancelOrder(cancel_sn, reason).then(() => {
              done()
              this.$message.success('订单取消申请成功！')
              this.finished = false
              this.orderList = []
              this.params.page_no = 1
              this.GET_OrderList()
            })
          }
        } else {
          done()
        }
      },
      /** 取消订单 */
      handleCancelOrder(order_sn) {
        this.reason = ''
        this.cancel_sn = order_sn
        this.showCancelDialog = true
      },
      /** 确认收货 */
      handleRogOrder(order_sn) {
        this.$confirm('请确认是否收到货物，否则可能会钱财两空！', () => {
          API_Order.confirmReceipt(order_sn).then(() => {
            this.$message.success('确认收货成功！')
            this.finished = false
            this.orderList = []
            this.params.page_no = 1
            this.GET_OrderList()
          })
        })
      },
      /** 订单搜索 */
      handleSearchOrder() {
        this.showSearchPopup = false
        this.params.key_words = this.searchKeyword
        this.params.page_no = 0
        this.finished = false
        this.orderList = []
        this.onLoad()
      },
      /** 获取订单数据 */
      GET_OrderList() {
        this.loading = true
        API_Order.getOrderList(this.params).then(response => {
          const { data } = response
          if(!data || !data.length) {
            this.finished = true
          } else {
            this.orderList.push(...data)
          }
          this.loading = false
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  .order-item {
    margin-top: 10px;
    background-color: #fff;
    padding: 0 10px 10px 10px;
    .sn-order-item {
      position: relative;
      height: 45px;
      line-height: 45px;
      font-size: 14px;
      color: #333;
      & > span:first-child {
        float: left;
      }
      & > span:last-child {
        float: right;
        color: $color-main;
      }
      &::after {
        content: "";
        position: absolute;
        z-index: 1;
        pointer-events: none;
        background-color: #e5e5e5;
        height: 1px;
        left: 0;
        right: 0;
        bottom: 0;
        margin: 0 -10px;
      }
    }
    .info-order-item {
      display: block;
      padding: 10px 0;
      position: relative;
      &::after {
        content: "";
        height: 0;
        display: block;
        border-bottom: 1px solid #e5e5e5;
        position: absolute;
        left: 0;
        right: 0;
        bottom: 0;
      }
      p {
        line-height: 20px;
        font-size: 12px;
        color: #999;
        span { color: #666 }
        i {
          display: inline-block;
          width: 24px;
        }
      }
    }
    .order-btns {
      position: absolute;
      top: 17px;
      right: 0;
      a {
        display: inline-block;
        width: 65px;
        height: 30px;
        margin-left: 5px;
        text-align: center;
        color: #fff;
        line-height: 30px;
        z-index: 1;
        border-radius: 2px;
        background: #e4393c;
        -webkit-tap-highlight-color: rgba(0,0,0,0);
        outline: 0;
        border: none;
      }
    }
    .shop-order-item {
      display: block;
      position: relative;
      height: 40px;
      line-height: 40px;
      font-size: 14px;
      color: $color-href;
      em {
        display: block;
        position: relative;
        padding-right: 20px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        font-style: normal;
        &::after {
          position: absolute;
          content: "";
          display: block;
          width: 8px;
          height: 8px;
          border-top: 1px solid #666;
          border-left: 1px solid #666;
          transform-origin: 50%;
          transform: rotate(135deg);
          right: 5px;
          top: 14px;
        }
      }
    }
    .sku-order-item {
      position: relative;
      margin-top: 10px;
      .sku-content {
        display: block;
        overflow: hidden;
        position: relative;
        a.img-info {
          & > span {
            display: inline-block;
            position: absolute;
            bottom: 0;
            left: 27px;
            z-index: 9;
            background-color: $color-main;
            color: #fff;
          }
        }
        img {
          position: relative;
          display: block;
          width: 75px;
          height: 75px;
          float: left;
          margin-right: 10px;
        }
      }
    }
    .sku-name {
      width: calc(100% - 85px);
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
      min-height: 36px;
    }
    .sku-assemble {
      display: inline-block;
      margin-left: 5px;
      float: right;
      & > em {
        color: $color-main;
      }
    }
  }
  .after-btn {
    a {
      color: $color-main;
    }
  }
</style>
