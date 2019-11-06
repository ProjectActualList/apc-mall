<template>
  <div v-if="coupons && coupons.length" id="theme1-coupons" class="shop-coupons">
    <div class="shop-coupons-title"></div>
    <ul class="shop-coupons-list">
      <li v-for="(coupon, index) in coupons" :key="index" class="coupon-item" @click="handleReceiveCoupon(coupon)">
        <div class="coupon-price">{{ coupon.coupon_price }}</div>
        <div class="coupon-other">
          <p>{{ coupon.title }}</p>
          <p>满{{ coupon.coupon_threshold_price }}元可使用</p>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
  import mixin from './themeCouponsMixin'
  export default {
    name: 'theme-1-coupons',
    mixins: [mixin]
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .shop-coupons {
    width: 950px;
    margin: 20px auto;
  }
  .shop-coupons-title {
    width: 100%;
    height: 90px;
    background: url("../../../assets/images/background-shop-coupons-title.png") no-repeat center center #fffbfb;
  }
  .shop-coupons-list {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    margin-top: 10px;
    background: #fff;
    text-align: center;
    overflow: hidden;
  }
  .coupon-item {
    display: flex;
    width: 230px;
    cursor: pointer;
    border: 1px dashed rgb(222, 222, 222);
    margin-right: 10px;
    .coupon-price {
      display: flex;
      align-items: center;
      font-family: georgia;
      padding-left: 10px;
      font-size: 48px;
      height: 108px;
      line-height: 45px;
      text-align: left;
      width: 110px;
      color: #424242;
      cursor: pointer;
    }
    .coupon-other {
      width: 110px;
      padding-top: 55px;
      background: url("../../../assets/images/icon-shop-coupons.png") no-repeat left 20px;
    }
  }
</style>
