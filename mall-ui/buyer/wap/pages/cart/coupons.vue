<template>
  <div id="coupons">
    <nav-bar fixed title="店铺优惠券"/>
    <en-empty v-if="!coupons || !coupons.length">该店铺暂无优惠券</en-empty>
    <div v-else class="coupons-container">
      <div
        class="coupon-item"
        v-for="(coupon, index) in coupons"
        :key="index"
        @click="handleReceiveCoupons(coupon)"
      >
        <div class="par">
          <p>{{ coupon.title }}</p>
          <sub class="sign">￥</sub><span>{{ coupon.coupon_price | unitPrice }}</span>
          <p>订单满{{ coupon.coupon_threshold_price | unitPrice }}元</p>
        </div>
        <div class="copy">
          <span>副券</span>
          <p>{{ coupon.start_time | unixToDate('yyyy-MM-dd') }}</p>
          <p>~</p>
          <p>{{ coupon.end_time | unixToDate('yyyy-MM-dd') }}</p>
        </div>
        <i></i>
      </div>
    </div>
  </div>
</template>

<script>
  import * as API_Promotions from '@/api/promotions'
  import * as API_Members from '@/api/members'
  export default {
    name: 'coupons',
    validate({ query }) {
      return /^\d+$/.test(query.shop_id)
    },
    head() {
      return {
        title: `店铺优惠券-${this.site.site_name}`
      }
    },
    data() {
      return {
        shop_id: this.$route.query.shop_id,
        coupons: ''
      }
    },
    mounted() {
      this.GET_Coupons()
    },
    methods: {
      /** 领取优惠券 */
      handleReceiveCoupons(coupon) {
        API_Members.receiveCoupons(coupon.coupon_id).then(() => {
          this.$message.success('领取成功！')
        })
      },
      /** 获取店铺优惠券 */
      GET_Coupons() {
        API_Promotions.getShopCoupons(this.shop_id).then(response => {
          this.coupons = response
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .coupons-container {
    padding: 56px 10px 10px 10px;
  }
  .coupon-item {
    position: relative;
    padding: 0 10px;
    height: 98px;
    margin: 10px auto;
    overflow: hidden;
    background: radial-gradient(transparent 0, transparent 5px, $color-main 5px);
    background-size: 15px 15px;
    background-position: 9px 3px;
    &::before {
      content: '';
      position: absolute;
      top: 0;
      bottom: 0;
      left: 10px;
      right: 10px;
      z-index: -1;
      background-color: $color-main;
    }
    &::after {
      content: '';
      position: absolute;
      left: 10px;
      top: 10px;
      right: 10px;
      bottom: 10px;
      box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.5);
      z-index: -2;
    }
    .par {
      float: left;
      padding: 12px 12px;
      width: 53%;
      border-right: 2px dashed rgba(255, 255, 255, .3);
      text-align: left;
      p {
        color: #fff;
        font-size: 12px;
        line-height: 12px;
      }
      .sign {
        font-size: 34px;
      }
      sub {
        position: relative;
        top: -2px;
        color: rgba(255, 255, 255, .8);
      }
      span {
        font-size: 30px;
        color: #fff;
        margin-right: 5px;
        line-height: 45.5px;
      }
    }
    .copy {
      display: inline-block;
      padding: 15px 10px;
      width: 30%;
      font-size: 25px;
      color: rgb(255, 255, 255);
      text-align: center;
      line-height: initial;
      p {
        font-size: 12px;
        color: #fff;
        line-height: 12px;
      }
    }
    i {
      position: absolute;
      left: 20%;
      top: 45px;
      height: 190px;
      width: 390px;
      background-color: rgba(255, 255, 255, .15);
      transform: rotate(-30deg);
    }
  }
</style>
