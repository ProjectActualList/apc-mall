<template>
  <div v-if="coupons && coupons.length" id="goods-coupons">
    <van-cell-group class="coupons-cells">
      <van-cell :value="'共' + coupons.length + '张'" is-link @click="showPopup = true">
        <div slot="title">
          <span>领券</span>
          <div class="coupons-cell">
            <template v-for="(coupon, index) in coupons">
              <span
                v-if="index < 2"
                :key="index"
                class="cell-coupon"
              >满{{ coupon.coupon_threshold_price }}减{{ coupon.coupon_price }}</span>
            </template>
          </div>
        </div>
      </van-cell>
    </van-cell-group>
    <van-popup v-model="showPopup" position="bottom" style="height:70%">
      <van-nav-bar title="店铺优惠券" @click-right="showPopup = false">
        <i class="iconfont ea-icon-close" slot="right"/>
      </van-nav-bar>
      <div class="coupons-list">
        <div class="coupon-item" v-for="(coupon, index) in coupons" :key="index">
          <a href="javascript:;" class="coupon-main" @click="handleReceiveCoupon(coupon)">
            <div class="coupon-view">
              <p class="coupon-price"><i>¥</i><strong>{{ coupon.coupon_price }}</strong></p>
              <p class="coupon-des">满{{ coupon.coupon_threshold_price }}元可用</p>
            </div>
            <div class="coupon-info">
              <p class="coupon-text">{{ coupon.title }}</p>
              <span class="coupon-info-btn">领取</span>
              <p class="coupon-info-date">{{ coupon.start_time | unixToDate('yyyy-MM-dd') }} 至 <br/> {{ coupon.end_time | unixToDate('yyyy-MM-dd') }}</p>
            </div>
          </a>
        </div>
      </div>
    </van-popup>
  </div>
</template>

<script>
  import Storage from '@/utils/storage'
  import * as API_Promotions from '@/api/promotions'
  import * as API_Members from '@/api/members'
  export default {
    name: 'goods-coupons',
    props: ['shop-id'],
    data() {
      return {
        // 优惠券列表
        coupons: '',
        // 显示优惠券弹窗
        showPopup: false
      }
    },
    mounted() {
      API_Promotions.getShopCoupons(this.shopId).then(response => {
        this.coupons = response
      })
    },
    methods: {
      /** 领取优惠券 */
      handleReceiveCoupon(coupon) {
        if (!Storage.getItem('refresh_token')) {
          this.$message.error('您还未登录！')
          return false
        }
        API_Members.receiveCoupons(coupon.coupon_id).then(response => {
          this.$message.success('领取成功！')
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  /deep/ {
    .van-nav-bar { position: relative }
    .coupons-cells {
      border-bottom: 10px solid #e8e8ed;;
      .van-cell__title { flex: 5 }
    }
  }
  .coupons-cell {
    display: inline-block;
    margin-left: 15px;
    .cell-coupon {
      position: relative;
      display: inline-block;
      margin-right: 5px;
      padding: 0 9px 0 12px;
      border-top: 1px solid $color-main;
      border-bottom: 1px solid $color-main;
      height: 16px;
      line-height: 16px;
      color: $color-main;
      font-size: 10px;
      &::before, &::after {
        content: "";
        position: absolute;
        top: -1px;
        height: 18px;
        background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAkCAMAAACpD3pbAAAAPFBMVEUAAAD////kOTzqaGr+9vb4zc32vL3sdXfnTE/mRUfxmpz87O3yn6HqZ2npXWDlQEP97e33xsf3xsbxl5nHV2NIAAAAAXRSTlMAQObYZgAAAJpJREFUOMvllEsSwiAQRMnjF8g/3v+uKhIrjhHK0l16B4+C7hlALS07qSyS2kVlKnHmCpqdNvwYwTdY6yKGX7AeKtb+jmN3jLuYMMEd5XYBUMZ6cO/BHHhr7ut6wizxHOhvM+kUT5S5I77ZsGWU1kbsExsmiSdMBVc3l9a0tJaDdR+CVcryWtRBFLXYkku1oae955Xnv5Y+j/UKs1MGwyraxf4AAAAASUVORK5CYII=) no-repeat;
        background-size: 15px 18px;
      }
      &::before {
        left: 0;
        width: 7px;
      }
      &::after {
        right: 0;
        width: 4px;
        background-position: -11px 0;
      }
    }
  }
  .coupons-list {
    padding: 0 10px;
    height: calc(100% - 46px);
    overflow-x: hidden;
    overflow-y: scroll;
    .coupon-item {
      color: $color-main;
      margin-bottom: 15px;
      padding: 7px 10px 10px;
      border-top: 6px solid currentColor;
      box-shadow: 0 0 6px 0 rgba(0,0,0,.1);
      border-radius: 6px;
    }
    .coupon-main {
      display: flex;
      min-height: 62px;
      color: currentColor;
    }
    .coupon-view {
      width: 110px;
      margin-right: 10px;
      line-height: 1;
      text-align: center;
      display: flex;
      -webkit-box-orient: vertical;
      flex-direction: column;
      justify-content: center;
    }
    .coupon-price {
      i {
        font-size: 14px;
        display: inline-block;
        vertical-align: top;
        margin: 3px 4px 0 0;
      }
      strong {
        font-weight: 700;
        font-size: 45px;
        position: relative;
        top: 1px;
      }
    }
    .coupon-des {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      font-size: 12px;
      margin-top: 5px;
    }
    .coupon-info {
      flex: 1;
      position: relative;
      .coupon-text {
        height: 36px;
        line-height: 18px;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        font-size: 12px;
        color: #666;
      }
      .coupon-info-btn {
        position: absolute;
        right: 0;
        bottom: 0;
        width: 68px;
        height: 20px;
        line-height: 20px;
        text-align: center;
        border-radius: 10px;
        box-sizing: border-box;
        z-index: 2;
        background-color: $color-main;
        color: #fff;
        font-size: 12px;
      }
      .coupon-info-date {
        position: absolute;
        left: 0;
        bottom: 0;
        box-sizing: border-box;
        width: 100%;
        line-height: 20px;
        font-size: 10px;
        color: #999;
        padding-right: 68px;
      }
    }
  }
</style>
