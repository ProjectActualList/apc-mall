<template>
  <ul v-if="coupons && coupons.length" class="shop-coupons-list">
    <li v-for="(coupon, index) in coupons" :key="index" class="coupon-item" @click="handleReceiveCoupon(coupon)">
      <p><span>￥</span>{{ coupon.coupon_price }}<span>元优惠券</span></p>
      <b>满{{ coupon.coupon_threshold_price }}元使用</b>
      <label>点击领取</label>
    </li>
  </ul>
</template>

<script>
  import mixin from './themeCouponsMixin'
  export default {
    name: 'theme-1-coupons',
    mixins: [mixin]
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .shop-coupons-list {
    overflow: hidden;
    margin: 8px 0;
    background: #fff;
    padding: 0.5rem;
    box-shadow: 1px 1px 10px #ccc;
  }
  .coupon-item {
    float: left;
    background: #fff;
    width: 47%;
    margin: 0 2% 8px 0;
    overflow: hidden;
    position: relative;
    padding: 0.6 * 16px 0;
    border: 1px solid #dedede;
    height: 4.5 * 16px;
    text-align: center;
    p {
      font-size: 1.2 * 16px;
      width: 100%;
      text-align: center;
      font-weight: 400;
      span {
        font-size: 0.8rem;
        color: #ccc;
        font-weight: 200;
        height: 1.5rem;
        line-height: 1.5rem;
        &:first-child {
          margin: 0 0.3 * 16px 0 0;
        }
        &:last-child {
          margin: 0 0 0 0.3 * 16px;
        }
      }
    }
    b {
      width: 100%;
      height: 16px;
      line-height: 16px;
      text-align: center;
      font-size: 0.8 * 16px;
      color: #ccc;
      font-weight: 200;
      display: block;
      margin: 0.3 * 16px 0;
    }
    label {
      padding: 0 0.3 * 16px;
      border: 1px solid #dedede;
      font-size: 0.8 * 16px;
      color: #ccc;
      font-weight: 200;
    }
  }
</style>
