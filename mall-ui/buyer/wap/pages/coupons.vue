<template>
  <div id="coupons" style="background-color: #f7f7f7">
    <nav-bar title="优惠券市场" fixed  :zIndex="10"/>
    <div class="coupons-container">
      <en-empty v-if="finished && !couponsList.length">暂无优惠券</en-empty>
      <van-list
        v-else
        v-model="loading"
        :finished="finished"
        @load="onLoad"
      >
        <a
          v-for="(coupon, index) in couponsList"
          :key="index"
          href="javascript:;"
          class="item-coupon"
        >
          <div class="inner-coupon">
            <img class="img-coupon" src="data:image/png;base64,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">
            <p class="name-coupon">
              <span>{{ coupon.title }}</span>
            </p>
            <span class="price-coupon">
              <em>¥{{ coupon.coupon_price }}</em>满{{ coupon.coupon_threshold_price }}可用
            </span>
            <div class="receive-coupon" @click="receiveCoupon(coupon)">
              <span class="icon-receive-coupon">立即<br>领取</span>
            </div>
          </div>
        </a>
      </van-list>
    </div>
  </div>
</template>

<script>
  import * as API_promotions from '@/api/promotions'
  import * as API_Members from '@/api/members'
  import Storage from '@/utils/storage'
  export default {
    name: 'coupons',
    data() {
      return {
        loading: false,
        finished: false,
        params: {
          page_no: 0,
          page_size: 10
        },
        couponsList: []
      }
    },
    mounted() {
    },
    methods: {
      /** 加载数据 */
      onLoad() {
        this.params.page_no += 1
        this.GET_Coupons()
      },
      /** 领取优惠券 */
      receiveCoupon(coupon) {
        if (!Storage.getItem('refresh_token')) {
          this.$message.error('请先登录！')
          return false
        }
        API_Members.receiveCoupons(coupon.coupon_id).then(() => {
          this.$message.success('领取成功！')
        })
      },
      /** 获取优惠券列表 */
      GET_Coupons() {
        this.loading = true
        API_promotions.getAllCoupons(this.params).then(response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            this.couponsList.push(...data)
          }
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../assets/styles/color";
  .coupons-container {
    padding: 56px 10px 0 10px;
  }
  .item-coupon {
    margin: 0 0 10px;
    color: $color-main;
    display: block;
    position: relative;
    height: 114px;
    z-index: 1;
    &::after {
      content: "";
      position: absolute;
      top: 5px;
      bottom: 5px;
      left: 0;
      right: 50px;
      box-shadow: 2px 2px 10px 8px hsla(0,0%,67%,.2);
    }
  }
  .inner-coupon {
    position: relative;
    z-index: 1;
    height: 114px;
    background-color: #fff;
    mask-border-source: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAKoAAAApBAMAAABXfK/HAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAASUExURUxpcf////////////////////t7VQsAAAAFdFJOUwB5N+QXTLmpnQAAAHxJREFUSMft17sRgDAMA1A+GYCGHg4WoGAPSPD+q3AQdCwgddIAr7ALy008ORokKKnMBTQx1Qy1Y6on1JapFqg9U43hU0eqmqljhVqmV124at0XaVe/Gtua5p2uUmPVqlWrVq1a1V5uScvQNCJNe5M0TU0r1jR4zbfB/oxuVMRWrAgOtD4AAAAASUVORK5CYII=);
    mask-border-width: 10px 85px 10px 0;
    mask-border-slice: 20 168 20 1 fill;
    border-radius: 5px
  }
  .img-coupon {
    display: block;
    width: 75px;
    height: 75px;
    position: absolute;
    left: 10px;
    top: 20px;
    border-radius: 4px;
  }
  .name-coupon {
    font-size: 12px;
    color: #666;
    position: absolute;
    left: 90px;
    top: 10px;
    right: 80px;
    span {
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }
  }
  .price-coupon {
    font-size: 14px;
    position: absolute;
    left: 90px;
    right: 80px;
    bottom: 10px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    em {
      display: inline-block;
      vertical-align: -2px;
      margin-right: 6px;
      font-size: 30px;
      line-height: 30px;
    }
  }
  .receive-coupon {
    display: block;
    width: 75px;
    text-align: center;
    position: absolute;
    right: 0;
    top: 0;
    bottom: 0;
    background-color: currentColor;
    border-radius: 0 5px 5px 0;
    .icon-receive-coupon {
      display: block;
      font-size: 12px;
      color: rgba(0,0,0,.2);
      border: 2px solid currentColor;
      border-radius: 50%;
      text-align: center;
      position: absolute;
      left: 50%;
      -webkit-transform: translate(-50%,-50%);
      transform: translate(-50%,-50%);
      line-height: 15px;
      color: #fff;
      margin: 0;
      width: 50px;
      height: 50px;
      padding: 9px 0;
      box-sizing: border-box;
      top: 50%;
    }
  }
</style>
