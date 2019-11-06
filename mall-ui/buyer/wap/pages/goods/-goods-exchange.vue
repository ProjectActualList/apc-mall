<template>
  <div v-if="promotion" id="goods-exchange">
    <div class="prom-bar-container">
      <div class="prom-price">¥<em>{{ promotion.exchange_money | unitPrice('', 'before') }}</em>.{{ promotion.exchange_money | unitPrice('', 'after') }} + {{ promotion.exchange_point }}积分</div>
      <div class="msg">
        <div class="text">
          <span></span>
          <del class="old-price">¥{{ promotion.goods_price | unitPrice }}</del>
        </div>
        <div class="text">
          <span class="icon exchange">积分兑换</span>
        </div>
      </div>
      <div class="count-down">
        <p class="count-down-times">
          <span>{{ promotion.enable_exchange }}人</span>
        </p>
        <p class="count-down-text">已兑换</p>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'goods-exchange',
    props: ['promotions'],
    computed: {
      promotion() {
        const { promotions } = this
        if (!promotions || !promotions.length) return false
        let prom = promotions.filter(item => item.exchange)
        if (prom[0]) return prom[0].exchange
        return false
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .prom-bar-container {
    position: relative;
    display: flex;
    align-items: center;
    z-index: 10;
    height: 49px;
    color: #fff;
    background: url(../../assets/images/background-promotion-bar.png) no-repeat,-webkit-linear-gradient(top,#fc5997,#ef4747);
    background-size: 259px,100%;
    .prom-price {
      margin: 0 10px;
      font-size: 14px;
      font-weight: 700;
      em {
        font-size: 24px;
      }
    }
    .msg {
      flex: 1;
      font-size: 12px;
      .icon {
        position: relative;
        display: inline-block;
        margin: -2px 3px 0 0;
        padding: 0 3px 0 18px;
        height: 15px;
        vertical-align: middle;
        line-height: 15px;
        font-size: 10px;
        &.exchange::before {
          background: #fff url(data:image/png;base64,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) no-repeat 50%;
          background-size: 12px
        }
        &::before {
          content: "";
          position: absolute;
          left: 0;
          top: 0;
          width: 15px;
          height: 15px;
          border-top-left-radius: 1px;
          border-bottom-left-radius: 1px;
        }
        &::after {
          content: "";
          display: block;
          border: 1px solid #fff;
          position: absolute;
          top: 0;
          left: 0;
          pointer-events: none;
          bottom: 0;
          right: 0;
          border-radius: 2px;
        }
      }
    }
    .count-down {
      position: absolute;
      right: 0;
      top: 0;
      min-width: 116px;
      height: 100%;
      text-align: center;
      background-color: #f8f8f8;
      .count-down-text {
        font-size: 10px;
        color: #666;
      }
      .count-down-times {
        margin: 5px 0 2px;
        font-size: 14px;
        color: $color-main;
      }
    }
  }
</style>
