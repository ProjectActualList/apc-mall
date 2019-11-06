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
        <div class="coupon-box">
          <div class="coupon-price">{{ coupon.coupon_price }}</div>
          <div class="coupon-name">
            <h2>优惠券</h2>
            <p>{{ coupon.title }}</p>
            <p class="use-res">满{{ coupon.coupon_threshold_price }}元可使用</p>
          </div>
          <div class="coupon-receive">领</div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
  import mixin from './themeCouponsMixin'

  export default {
    name: 'theme-2-coupons',
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
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    width: 100%;
    margin-top: 10px;
    background: #fff;
    text-align: center;
    overflow: hidden;
  }
  .coupon-item {
    position: relative;
    margin: 0 0 20px 30px;
    background: #b6bfa1;
    padding: 10px;
    .coupon-box {
      display: flex;
      width: 180px;
      height: 80px;
      border: 1px solid #897459;
    }
    .coupon-price {
      align-self: center;
      color: #424242;
      cursor: pointer;
      font-family: georgia,serif;
      font-size: 40px;
      line-height: 35px;
      letter-spacing: -6px;
      text-align: center;
      width: 90px;
    }
    .coupon-name {
      display: flex;
      flex-direction: column;
      h2 {
        border-bottom: 1px dashed #faf6f0;
        cursor: pointer;
        display: block;
        font: 20px/37px "微软雅黑";
        text-align: center;
      }
      p {
        color: #faf6f0;
        cursor: pointer;
        float: left;
        font: 12px/15px "微软雅黑";
        text-align: center;
      }
    }
    .coupon-receive {
      cursor: pointer;
      position: absolute;
      width: 50px;
      height: 50px;
      background: #fff;
      right: -25px;
      top: 25px;
      line-height: 50px;
      text-align: center;
      color: #b6bfa1;
      font: 24px/50px "微软雅黑";
      border-radius: 30px;
      transition: all ease 0.2s;
      box-shadow: 2px 0 8px 1px #efefef;;
      &:hover {
        right: -20px;
      }
    }
  }
</style>
