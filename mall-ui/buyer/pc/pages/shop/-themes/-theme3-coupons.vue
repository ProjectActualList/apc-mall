<template>
  <div v-if="coupons && coupons.length" id="theme1-coupons" class="shop-coupons">
    <div class="shop-coupons-title">
      <h2>领</h2>
      <h3>
        <span>券后买更优惠</span>
        <i>RECEIVE-COUPON</i>
      </h3>
    </div>
    <ul class="shop-coupons-list">
      <li v-for="(coupon, index) in coupons" :key="index" class="coupon-item" @click="handleReceiveCoupon(coupon)">
        <h2>{{ coupon.coupon_price }}</h2>
        <p>{{ coupon.title }}</p>
        <span>{{ coupon.start_time | unixToDate('yyyy-MM-dd') }} - {{ coupon.end_time | unixToDate('yyyy-MM-dd') }}</span>
        <span>满{{ coupon.coupon_threshold_price }}元可使用</span>
      </li>
    </ul>
  </div>
</template>

<script>
  import mixin from './themeCouponsMixin'

  export default {
    name: 'theme-3-coupons',
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
    height: 80px;
    h2 {
      text-align: center;
      display: block;
      float: left;
      width: 60px;
      height: 60px;
      line-height: 60px;
      color: #fff;
      background: #644139;
      font-size: 28px;
      font-weight: 200;
      font-family: 微软雅黑;
      border-radius: 60px;
      margin: 0 0 0 400px;
    }
    h3 {
      display: block;
      height: 60px;
      float: left;
      margin: 0 0 0 5px;
    }
    span {
      display: block;
      height: 40px;
      line-height: 40px;
      font-size: 24px;
      color: #644139;
      text-align: left;
      font-family: 微软雅黑;
      font-weight: 200;
    }
    i {
      display: block;
      height: 20px;
      line-height: 20px;
      font-size: 12px;
      font-weight: 200;
      color: #ccc;
    }
  }
  .shop-coupons-list {
    width: 100%;
    margin-top: 10px;
    background: #fff;
    text-align: center;
    overflow: hidden;
  }
  .coupon-item {
    background: url("../../../assets/images/background-shop-coupons.jpg") no-repeat center top;
    margin: 0 30px;
    display: inline-block;
    border: 1px solid #dcdcdc;
    height: 218px;
    overflow: hidden;
    text-align: center;
    text-decoration: none;
    width: 225px;
    cursor: pointer;
    &:hover {
      border-color: #000
    }
    h2 {
      font-family: georgia;
      font-size: 60px;
      font-weight: 500;
      line-height: 60px;
      overflow: hidden;
      padding-top: 32px;
      width: 225px;
    }
    p {
      font-family: georgia;
      font-weight: 500;
      overflow: hidden;
      padding-top: 15px;
      width: 225px;
    }
    span {
      font-weight: 500;
      overflow: hidden;
      padding-top: 10px;
      width: 225px;
      display: block;
    }
  }
</style>
