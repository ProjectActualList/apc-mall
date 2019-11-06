<template>
  <div id="checkout" style="background-color: #f7f7f7">
    <nav-bar fixed title="填写订单"/>
    <en-empty v-if="inventories && inventories.length === 0">
      购物清单为空
    </en-empty>
    <div v-else class="checkout-container">
      <!--收货地址 start-->
      <van-cell-group>
        <van-cell is-link to="/member/shipping-address?from=checkout">
          <div v-if="!address" class="no-address">新建或选择收货地址以确保商品顺利送达</div>
          <div v-else>
            <div class="tit-address">
              <div class="name-tit-address">{{ address.name }}</div>
              <div class="mobile-tit-address">{{ address.mobile }}</div>
            </div>
            <div class="cnt-address">
              <van-icon name="location"/>
              <span class="address-where">{{ formatterAddress() }}</span>
            </div>
          </div>
        </van-cell>
        <div class="address-list-hr"></div>
      </van-cell-group>
      <!--收货地址 end-->
      <!--购物清单 start-->
      <checkout-inventory :inventories="inventories"/>
      <!--购物清单 end-->
      <van-cell-group>
        <!--支付配送 start-->
        <van-cell title="支付配送" is-link @click="showPaymentPopup = true">
          <div>
            <p>{{ params.payment_type === 'ONLINE' ? '在线支付' : '货到付款' }}</p>
            <p>{{ params.receive_time }}</p>
          </div>
        </van-cell>
        <!--支付配送 end-->
        <!--发票信息 start-->
        <van-cell title="发票信息" is-link @click="showReceiptPopup = true" class="remark-cell">
          <span v-if="!params.receipt || !params.receipt.receipt_title">不开具发票</span>
          <span v-else>{{ params.receipt.receipt_title }}-{{ params.receipt.receipt_content }}</span>
        </van-cell>
        <!--发票信息 end-->
      </van-cell-group>
      <van-cell-group>
        <!--优惠券 start-->
        <van-cell is-link @click="showCouponsPopup = true">
          <div slot="title">
            <span>优惠券</span>
            <em class="can-use-coupon-num">{{ coupon_num }}张可用</em>
          </div>
          <span v-if="!orderTotal.coupon_price">未使用</span>
          <span v-else class="price">-￥{{ orderTotal.coupon_price | unitPrice }}</span>
        </van-cell>
        <!--优惠券 end-->
        <!--备注信息 start-->
        <van-cell title="备注信息" is-link @click="showRemarkDialog = true" class="remark-cell">
          {{ params.remark || '未填写' }}
        </van-cell>
        <!--备注信息 end-->
      </van-cell-group>
      <!--订单金额 start-->
      <van-cell-group class="price-cells">
        <van-cell title="商品金额">
          <span class="price">￥{{ orderTotal.original_price | unitPrice }}</span>
        </van-cell>
        <van-cell title="优惠券抵扣" v-if="orderTotal.coupon_price">
          <span class="price">-￥{{ orderTotal.coupon_price | unitPrice }}</span>
        </van-cell>
        <van-cell title="返现金额" v-if="orderTotal.cash_back">
          <span class="price">-￥{{ orderTotal.cash_back | unitPrice }}</span>
        </van-cell>
        <van-cell title="积分抵扣" v-if="orderTotal.exchange_point" class="exchange-cell">
          <span>-{{ orderTotal.exchange_point }}分</span>
          <p class="exchange-tip">温馨提示：订单取消、申请售后积分不退还</p>
        </van-cell>
        <van-cell title="运费">
          <span class="price">￥{{ orderTotal.freight_price | unitPrice }}</span>
        </van-cell>
      </van-cell-group>
      <!--订单金额 end-->
    </div>
    <van-submit-bar
      :loading="loading"
      :price="orderTotal.total_price * 100"
      :disabled="submitDisabled"
      button-text="提交订单"
      @submit="handleCreateTrade"
    />
    <!--优惠券popup start-->
    <checkout-coupons
      :show="showCouponsPopup"
      :inventories="inventories"
      @close="showCouponsPopup = false"
      @changed="GET_Inventories"
      @loaded="(coupons_num) => { coupon_num = coupons_num  }"
    />
    <!--优惠券popup end-->
    <!--发票信息popup start-->
    <checkout-receipt
      v-if="params"
      :show="showReceiptPopup"
      :receipt="params.receipt"
      @close="showReceiptPopup = false"
      @changed="handleReceiptChanged"
    />
    <!--发票信息popup end-->
    <!--支付配送popup start-->
    <checkout-payment
      v-if="params"
      :show="showPaymentPopup"
      :payment-type="params.payment_type"
      :receive-time="params.receive_time"
      @close="showPaymentPopup = false"
      @changed="handlePaymentChanged"
    />
    <!--支付配送popup end-->
    <!--订单备注dialog start-->
    <van-dialog
      v-model="showRemarkDialog"
      title="订单备注"
      show-cancel-button
      :before-close="remarkBeforeClose"
    >
      <van-field
        v-model="remark"
        type="textarea"
        rows="2"
        autosize
        maxlength="30"
        placeholder="超过30个字我就记不住了嗷！"
      />
    </van-dialog>
    <!--订单备注dialog end-->
    <van-actionsheet v-model="showErrorActionsheet" :title="errorActionsheetMessage">
      <div v-for="(goods, index) in errorActionsheetData" :key="index" class="error-goods-item">
        <img :src="goods.image" :alt="goods.name" class="error-goods-image">
        <h5 class="error-goods-name">{{ goods.name }}</h5>
      </div>
    </van-actionsheet>
  </div>
</template>

<script>
  import Vue from 'vue'
  import { mapGetters, mapActions } from 'vuex'
  import * as CheckoutComponents from './'
  import * as API_Trade from '@/api/trade'
  import * as API_Members from '@/api/members'
  import * as API_Address from '@/api/address'
  export default {
    name: 'checkout-index',
    components: CheckoutComponents,
    data() {
      return {
        // 加载订单结算中
        loading: false,
        // 是否是拼团
        isAssemble: false,
        // 结算参数
        params: '',
        // 订单总金额
        orderTotal: {
          discount_price: 0,
          exchange_point: 0,
          freight_price: 0,
          goods_price: 0,
          is_free_freight: 0,
          total_price: 0,
        },
        // 购物清单
        inventories: '',
        // 已选地址
        address: '',
        // 显示优惠券弹窗
        showCouponsPopup: false,
        // 优惠券张数
        coupon_num: 0,
        // 显示购物清单弹窗
        showInventoryPopup: false,
        // 显示发票信息弹窗
        showReceiptPopup: false,
        // 显示支付配送弹窗
        showPaymentPopup: false,
        // 显示订单备注弹窗
        showRemarkDialog: false,
        // 订单备注暂缓区
        remark: '',
        // 显示错误actionsheet
        showErrorActionsheet: false,
        // 错误actionsheet消息
        errorActionsheetMessage: '',
        // 错误actionsheet数据
        errorActionsheetData: []
      }
    },
    async mounted() {
      // 获取购物清单
      this.loading = true
      const params = await API_Trade.getCheckoutParams()
      this.params = params
      this.remark = params.remark
      if (params.address_id) {
        await API_Trade.setAddressId(params.address_id)
        this.address = await API_Address.getAddressDetail(params.address_id)
      }
      // 是否是拼团
      this.isAssemble = this.$route.query.from_nav === 'assemble'
      await this.GET_Inventories()
      this.loading = false
    },
    computed: {
      // 计算是否禁用提交订单按钮
      submitDisabled() {
        return !this.inventories.length
      }
    },
    methods: {
      /** 发票信息发生改变 */
      handleReceiptChanged(receipt) {
        this.$set(this.params, 'receipt', receipt)
      },
      /** 支付配送发生改变 */
      handlePaymentChanged(payment) {
        const { params } = this
        this.$set(this, 'params', {...params, ...payment})
      },
      /** 订单备注dialog关闭前 */
      remarkBeforeClose(action, done) {
        if (action === 'confirm') {
          const { remark } = this
          API_Trade.setRemark(remark).then(() => {
            this.$set(this.params, 'remark', remark)
            done()
          })
        } else {
          done()
        }
      },
      /** 格式化地址信息 */
      formatterAddress() {
        const { address } = this
        if (!address) return ''
        return `${address.province} ${address.city} ${address.county} ${address.town} ${address.addr}`
      },
      /** 提交订单 */
      handleCreateTrade() {
        /** 先调用创建订单接口，再跳转到收银台 */
        Promise.all([
          this.isAssemble ? API_Trade.createAssembleTrade('WAP', this.$route.query.order_id) : API_Trade.createTrade(),
        ]).then(response => {
          this.isAssemble ? this.$store.dispatch('cart/getAssembleCartAction') : this.$store.dispatch('cart/getCartDataAction')
          this.$router.replace({ path: '/checkout/cashier?trade_sn=' + response[0].trade_sn })
        }).catch(error => {
          const { data } = error[0].response || {}
          if (data.data) {
            const { data: list } = data
            if (!list || !list[0]) {
              this.$message.error(data.message)
              return
            }
            this.errorActionsheetMessage = data.message
            this.errorActionsheetData = typeof (list) === 'string' ? JSON.parse(list) : list
            this.showErrorActionsheet = true
          } else {
            this.$message.error(data.message)
          }
        })
      },
      /**
       * 获取购物清单，和结算金额
       * @returns {Promise<void>}
       * @constructor
       */
      GET_Inventories() {
        Promise.all([
          this.isAssemble ? API_Trade.getAssembleCarts() : API_Trade.getCarts('checked'),
        ]).then(response => {
          const { cart_list, total_price } = response[0]
          this.orderTotal = total_price
          let coupon_num = 0
          cart_list.forEach(shop => { coupon_num += shop.coupon_list.length })
          this.coupon_num = coupon_num
          this.inventories = cart_list
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .checkout-container {
    padding-top: 46px;
    padding-bottom: 50px + 10px;
    /deep/ {
      .van-cell-group:not(:first-child) {
        margin-top: 10px;
      }
      .van-cell {
        align-items: center;
      }
    }
  }
  .no-address {
    height: 50px;
    font-size: 14px;
    line-height: 50px;
    text-align: center;
  }
  .address-list-hr {
    width: 100%;
    height: 5px;
    background: url(../../assets/images/icon-address-hr.png) repeat-x left top;
  }
  .tit-address {
    padding-bottom: 10px;
    overflow: hidden;
    color: #252525;
    .name-tit-address {
      float: left;
      font-size: 16px;
      color: #252525;
      font-weight: bold;
      margin-left: 23px;
    }
    .mobile-tit-address {
      float: left;
      font-size: 16px;
      margin-left: 4px;
    }
  }
  .cnt-address {
    padding-right: 10px;
    padding-left: 23px;
    font-size: 14px;
    color: #232326;
    line-height: 22px;
    position: relative;
    .van-icon-location {
      position: absolute;
      top: 0;
      left: 0;
      font-size: 18px;
    }
    .address-where {
      color: #232326;
    }
  }
  .sku-list {
    float: left;
    padding-left: 0;
    padding-right: 2.5%;
    width: 80%;
    height: 70px;
    text-align: center;
    overflow: hidden;
    .sku-item {
      position: relative;
      width: 50px;
      height: 70px;
      float: left;
      margin-right: 7px;
      img {
        display: inline-block;
        width: 50px;
        height: 50px;
      }
    }
  }
  .sku-count {
    text-align: right;
    line-height: 70px;
  }
  .sku-single {
    position: relative;
    line-height: 18px;
    .img-single-item {
      padding-right: 10px;
      width: 87px;
      float: left;
      text-align: center;
    }
    .inner-img-sinle-item {
      width: 77px;
      height: 77px;
      position: relative;
      &::after {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 200%;
        height: 200%;
        border: 1px solid #e3e5e9;
        border-radius: 4px;
        transform: scale(0.5);
        transform-origin: top left;
      }
      img { width: 100%; height: 100% }
    }
    .content-single-item {
      position: relative;
      width: auto;
      float: none;
      padding-right: 10px;
      font-size: 14px;
      color: #232326;
    }
    .name-single-item {
      display: -webkit-box;
      overflow: hidden;
      text-overflow: ellipsis;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      margin-bottom: 8px;
      font-size: 14px;
      line-height: 17px;
      color: #232326;
      padding-top: 10px;
    }
    .price-single-item {
      white-space: nowrap;
      line-height: 17px;
      overflow: hidden;
      zoom: 1;
    }
    .price-box {
      float: left;
      font-size: 16px;
      color: $color-main;
      em {
        font-size: 12px;
      }
      .price {
        font-size: 16px;
      }
    }
    .sam-num {
      float: right;
      color: #848689;
      font-size: 13px;
      margin-top: 4px;
    }
  }
  .price-cells {
    .van-cell {
      padding: 3px 15px;
      &:after {
        content: none;
      }
    }
  }
  .can-use-coupon-num {
    display: inline-block;
    padding: 2px;
    font-size: 12px;
    background-color: #f42424;
    color: #fff;
    line-height: 12px;
    margin-left: 5px;
  }
  /deep/ {
    .big-btn {
      position: absolute;
      padding: 0;
      width: 100%;
      left: 0;
      bottom: 0;
      text-align: center;
      .van-button {
        width: 100%;
        border-radius: 0;
        background-color: $color-main;
        border-color: $color-main;
      }
    }
    .eui-checkbox-btn {
      display: inline-block;
      padding: 0 16px;
      font-size: 12px;
      color: #686868;
      border-radius: 2px;
      height: 30px;
      line-height: 30px;
      position: relative;
      box-sizing: border-box;
      & + .eui-checkbox-btn {
        margin-left: 10px;
        margin-bottom: 15px;
      }
      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        border: 1px solid #bfbfbf;
        width: 200%;
        height: 200%;
        transform: scale(0.5, 0.5);
        transform-origin: left top;
        box-sizing: border-box;
        border-radius: 8px;
      }
      &.checked {
        color: $color-main;
        padding: 0 10px 0 23px;
        background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAOCAMAAAAYGszCAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAACWUExURQAAAPIwMPIvL/EvL/UxMew2NvIvL/8AAP8qKv9VVfEvL/EvL/EvL/EuLvIwMPIwMPQ1NfIwMPEvL/QvL/EwMPIwMPIwMPEuLvIvL/IvL/EwMPEwMPEwMPIvL/IvL/IvL/EvL/84OPEwMPEvL/EvL/EwMPAzM+UzM/EvL/EvL/EwMPEvL/8/P/8/P/EvL/IwMPEwMPIwMMNbt20AAAAxdFJOUwB+i5caDrUBBgOH/PpNj/MYZKwwqN6eN6L16JjjysagwQnSp+vCIwpgq668BAiGP/mq889uAAAAe0lEQVQY02NgIAaoKclhiOmrGCqgi2kYGKqyogqx6xkaaquDmQIsUDFFTUNDNl0IW0pSEEyzyBoaanFAFYgZGooAzRGWMTRU1oGZxMFmaCgqzstvaCjPiTCfQ8jQkIfb0FCaHdlWTglDIOBjR3MLs6EhF4ZH2JkYCfkfAKpCCddSCmBzAAAAAElFTkSuQmCC') no-repeat 10px 12px;
        background-size: 10px 7px;
        &::before {
          border-color: $color-main
        }
      }
    }
    .remark-cell .van-cell__value {
      flex: 3;
      overflow: hidden;
      text-overflow:ellipsis;
      white-space: nowrap;
    }
    .error-goods-item {
      display: flex;
      justify-content: center;
      align-items: center;
      margin: 10px 0;
    }
    .error-goods-image {
      width: 80px;
      height: 80px;
      margin: 0 10px;
    }
    .error-goods-name {
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 3;
      overflow: hidden;
      color: red;
    }
    .exchange-cell .van-cell__value {
      flex: 2;
    }
    .exchange-tip {
      color: red;
      font-size: 12px;
    }
  }
</style>
