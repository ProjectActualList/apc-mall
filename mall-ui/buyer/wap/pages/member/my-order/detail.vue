<template>
  <div id="order-detail" style="background-color: #f7f7f7">
    <nav-bar title="订单详情"/>
    <div v-if="order" class="order-container">
      <!--交易状态-->
      <div class="trade-status" v-if="order.ping_tuan_status">
        <span class="trade-status-title">
          <i class="iconfont ea-icon-express"></i>
          交易状态
        </span>
        <span>{{ order.ping_tuan_status }}</span>
      </div>
      <div class="order-items">
        <div class="order-item">
          <span>订单编号：</span><span>{{ order.sn }}</span>
        </div>
        <div v-if="!orderLog.length" class="order-item">
          <span>下单时间：</span><span>{{ order.create_time | unixToDate }}</span>
        </div>
        <div v-for="(log, index) in orderLog" :key="index" class="order-item">
          <span>{{ log.message }}：</span><span>{{ log.op_time | unixToDate }}</span>
        </div>
        <div class="order-item">
          <span>配送时间：</span><span>{{ order.receive_time || '无' }}</span>
        </div>
        <div class="order-item">
          <span>订单备注：</span><span>{{ order.remark || '无' }}</span>
        </div>
        <div class="order-item">
          <span>订单状态：</span><span>{{ order.order_status_text || '无' }}</span>
        </div>
        <span class="hr"></span>
        <div class="order-item">
          <span>应付：</span><span class="price">￥{{ order.need_pay_money | unitPrice }}</span>
        </div>
      </div>
      <div class="sku-box">
        <div class="shop-info">
          <div class="shop-name">
            <nuxt-link :to="'/shop/' + order.seller_id">
              <van-icon name="points-mall"/>{{ order.seller_name }}<van-icon name="arrow"/>
            </nuxt-link>
            <div class="sku-num">共计{{ countSkuNum() }}件商品</div>
          </div>
        </div>
        <ul v-if="skuList" class="sku-list">
          <li class="sku-item" v-for="(sku, index) in skuList" :key="index">
            <a :href="'/goods/' + sku.goods_id" class="goods-image">
              <img :src="sku.goods_image" :alt="sku.name">
            </a>
            <a :href="'/goods/' + sku.goods_id">
              <span  class="goods-name">{{ sku.name }}</span>
              <p v-if="sku.spec_list" class="sku-spec">{{ sku | formatterSkuSpec }}</p>
              <p v-if="sku.promotion_tags && sku.promotion_tags.length">
                <span class="sku-act-tag" v-for="(tag, _index) in sku.promotion_tags" :key="'tag_' + _index">{{ tag }}</span>
              </p>
            </a>
            <div class="goods-infos">
              <p class="price">￥{{ sku.purchase_price | unitPrice }}</p>
              <p class="goods-num">x{{ sku.num }}</p>
              <p v-if="sku.goods_operate_allowable_vo.allow_apply_service" class="after-btn">
                <nuxt-link :to="'/member/after-sale/apply?order_sn=' + order.sn + '&sku_id=' + sku.sku_id">申请售后</nuxt-link>
              </p>
            </div>
          </li>
        </ul>
        <div class="order-btns">
          <nuxt-link v-if="order.order_operate_allowable_vo.allow_apply_service" :to="'/member/after-sale/apply?order_sn=' + order.sn">
            <van-button size="small" type="default">申请售后</van-button>
          </nuxt-link>
          <nuxt-link v-if="order.logi_id && order.ship_no" :to="'./express?logi_id=' + order.logi_id + '&ship_no=' + order.ship_no">
            <van-button size="small" type="default">查看物流</van-button>
          </nuxt-link>
          <nuxt-link v-if="order.order_operate_allowable_vo.allow_comment" :to="'../comments?order_sn=' + order.sn">
            <van-button size="small" type="default">去评论</van-button>
          </nuxt-link>
        </div>
      </div>
      <!--赠品列表-->
      <div v-if="order.gift_list && order.gift_list.length" class="sku-box">
        <div class="shop-info">
          <div class="shop-name">
            <van-icon name="point-gift"/>赠品列表
          </div>
        </div>
        <ul class="sku-list" style="overflow:hidden;">
          <li class="sku-item gift" v-for="(gift, index) in order.gift_list" :key="index">
            <a :href="gift.gift_img" class="goods-image">
              <img :src="gift.gift_img" :alt="gift.gift_name">
            </a>
            <a :href="gift.gift_img">
              <span  class="goods-name">{{ gift.gift_name }}</span>
            </a>
            <div class="goods-info">
              <p class="price">￥{{ gift.gift_price | unitPrice }}</p>
            </div>
          </li>
          <li class="sku-item gift" v-if="order.gift_coupon" key="gift_coupon">
            <a class="goods-image">
              <img src="../../../assets/images/icon-color-coupon.png">
            </a>
            <span class="goods-name">
              价值<em class="price">￥{{ order.gift_coupon.amount | unitPrice }}</em>的优惠券
            </span>
          </li>
        </ul>
      </div>
      <van-cell-group class="receipt-cell">
        <van-cell v-if="!order.receipt_history || !order.receipt_history.receipt_title" title="发票信息" value="无" />
        <van-cell
          v-else
          title="发票信息"
          :value="order.receipt_history.receipt_type +'-'+ order.receipt_history.receipt_title"
          is-link
          @click="showReceiptDialog = true"
        />
      </van-cell-group>
      <div class="information">
        <div class="address">
          <span class="name">{{ order.ship_name }}</span>
          <span>{{ order.ship_mobile }}</span>
          <p>{{ order.ship_province }} {{ order.ship_city }} {{ order.ship_county }} {{ order.ship_town }} {{ order.ship_addr }}</p>
        </div>
        <div class="order-items">
          <div class="order-item">
            <span>商品总价：</span><span>￥{{ order.goods_price | unitPrice }}</span>
          </div>
          <div v-if="order.coupon_price" class="order-item">
            <span>优惠券抵扣：</span><span>-￥{{ order.coupon_price | unitPrice }}</span>
          </div>
          <div v-if="order.cash_back" class="order-item">
            <span>返现金额：</span><span>-￥{{ order.cash_back | unitPrice }}</span>
          </div>
          <div v-if="order.use_point" class="order-item">
            <span>积分抵扣：</span><span>-{{ order.use_point }}积分</span>
          </div>
          <div class="order-item">
            <span>运费：</span><span>￥{{ order.shipping_price | unitPrice }}</span>
          </div>
          <span class="hr"></span>
          <div class="order-item">
            <span>订单金额：</span><span class="price">￥{{ order.need_pay_money | unitPrice }}</span>
          </div>
        </div>
      </div>
      <!--拼团详情-->
      <div
        class="assembly"
        v-if="order.order_status !== 'CONFIRM' && order.order_status !== 'CANCELLED' && order.order_status !== 'COMPLETED' && order.ping_tuan_status">
        <nuxt-link :to="'/member/my-order/assemble?order_sn='+ order_sn">
          <van-button size="small" type="default">查看拼团详情</van-button>
        </nuxt-link>
      </div>
    </div>
    <van-popup v-model="showReceiptDialog" position="bottom" class="receipt-dialog">
      <van-cell-group v-if="order && order.receipt_history">
        <van-nav-bar title="开票详情" @click-right="showReceiptDialog = false">
          <i class="iconfont ea-icon-close" slot="right"></i>
        </van-nav-bar>
        <van-cell title="发票类型" :value="order.receipt_history.receipt_type"/>
        <van-cell title="发票抬头" :value="order.receipt_history.receipt_title"/>
        <van-cell title="发票内容" :value="order.receipt_history.receipt_content"/>
        <van-cell title="发票税号" :value="order.receipt_history.tax_no"/>
      </van-cell-group>
    </van-popup>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_Order from '@/api/order'
  import * as API_Trade from '@/api/trade'
  export default {
    name: 'order-detail',
    validate({ query }) {
      return !!query.order_sn
    },
    data() {
      return {
        order_sn: this.$route.query.order_sn,
        order: '',
        skuList: '',
        // 显示发票信息
        showReceiptDialog: false,
        // 订单日志
        orderLog: ''
      }
    },
    mounted() {
      this.GET_OrderDetail()
    },
    methods: {
      /** 计算当前订单中的商品件数 */
      countSkuNum() {
        let num = 0
        this.skuList.forEach(sku => {
          num += sku.num
        })
        return num
      },
      /** 取消订单 */
      handleCancelOrder() {
        this.$layer.prompt({
          formType: 2,
          title: '请输入取消原因'
        }, (value, index) => {
          const val = value.trim()
          if (!val) {
            this.$message.error('请填写取消原因！')
          } else {
            API_Order.cancelOrder(this.order_sn, val).then(() => {
              this.$message.success('订单取消申请成功！')
              layer.close(index)
              this.GET_OrderDetail()
            })
          }
        })
      },
      /** 确认收货 */
      handleRogOrder() {
        this.$confirm('请确认是否收到货物，否则可能会钱财两空！', () => {
          API_Order.confirmReceipt(this.order_sn).then(() => {
            this.$message.success('确认收货成功！')
            this.GET_OrderDetail()
          })
        })
      },
      /** 获取订单详情 */
      async GET_OrderDetail() {
        const { order_sn } = this
        const values = await Promise.all([
          API_Order.getOrderDetail(order_sn),
          API_Order.getOrderLog(order_sn)
        ])
        this.order = values[0]
        this.skuList = values[0]['order_sku_list']
        this.orderLog = values[1]
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  .order-container {
    padding-top: 45px;
    /*交易状态*/
    .trade-status {
      padding: 20px;
      display: flex;
      flex-wrap: nowrap;
      justify-content: space-between;
      align-items: center;
      span {
        font-size: 14px;
      }
      span.trade-status-title {
        display: flex;
        align-items: center;
        .iconfont {
          display: inline-block;
          margin-right: 5px;
        }
      }
      span:last-child {
        color: #f24;
      }
    }
    .hr {
      display: block;
      width: 100%;
      height: 1px;
      background-color: #e5e5e5;
      clear: both;
    }
    .order-items {
      background-color: #fff;
      padding: 0 10px;
      .order-item {
        padding: 5px 0;
        font-size: 14px;
        .price {
          font-size: 16px;
        }
      }
    }
    .sku-box {
      clear: both;
      margin-top: 10px;
      background-color: #fff;
      .shop-info {
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        border-bottom: 1px solid #e5e5e5;
        padding: 0 10px;
        margin: 0 auto;
      }
      .shop-name {
        a {
          color: $color-href;
          &:hover { color: $color-main }
        }
        .van-icon {
          vertical-align: middle;
          margin-right: 5px;
        }
        .van-icon-point-gift, .van-icon-points-mall {
          font-size: 20px;
        }
      }
      .sku-num {
        float: right;
      }
    }
    .sku-list {
      padding: 0 10px;
    }
    .sku-item {
      display: flex;
      align-items: center;
      margin: 10px auto;
      min-height: 75px;
      &.gift {
        min-height: auto;
      }
      .goods-name {
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 3;
        overflow: hidden;
      }
      .goods-image {
        margin-right: 10px;
        img {
          width: 60px;
          height: 60px;
        }
      }
      .goods-info {
        margin-left: 10px;
        .price {
          font-size: 14px;
        }
      }
    }
    .order-btns {
      line-height: 50px;
      padding: 0 10px;
      border-top: 1px solid #e5e5e5;
      background-color: #fff;
      text-align: right;
      a {
        display: inline-block;
        margin-left: 5px
      }
    }
    .receipt-cell {
      margin-top: 10px;
      /deep/ .van-cell__value {
        overflow: hidden;
        text-overflow:ellipsis;
        white-space: nowrap;
      }
    }
    /*拼团详情*/
    .assembly {
      padding: 10px;
      text-align: right;
    }
  }
  .information {
    width: 100%;
    background: #fff;
    margin-top: 10px;
    .address {
      margin: 0 auto;
      padding: 10px;
      font-size: 14px;
      border-bottom: 1px solid #ccc;
      .name {
        margin-right: 40px;
        display: inline-block;
        line-height: 25px;
      }
      p {
        color: #999;
        overflow: hidden;
        width: 100%;
        text-overflow: ellipsis;
        -webkit-line-clamp: 2;
        font-size: 12px;
      }
    }
  }
  /deep/ {
    .receipt-dialog {
      height: 70%;
      .van-nav-bar { position: relative }
    }
  }
  .goods-infos {
    min-width: 60px;
    margin-left: 10px;
  }
  .after-btn {
    a {
      color: $color-main
    }
  }
  .sku-act-tag {
    display: inline-block;
    padding: 0 5px;
    line-height: 15px;
    margin-right: 5px;
    border: 1px solid $color-main;
    color: $color-main
  }
</style>
