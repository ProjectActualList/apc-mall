<template>
  <div v-if="promotion && showPromotion" id="goods-groupbuy" class="groupbuy-container">
    <!--团购活动-->
    <goods-prom-bar
      v-if="promotion.prom_type === 'gb'"
      title="团购活动"
      type="groupbuy"
      :price="promotion.groupbuy_goods_vo.price"
      :old-price="promotion.groupbuy_goods_vo.original_price"
      :end-time="promotion.end_time - parseInt(new Date() / 1000)"
      @count-end="handleCountEnd"
    />
    <!--限时抢购-->
    <goods-prom-bar
      v-else
      title="限时抢购"
      type="seckill"
      :price="promotion.seckill_goods_vo.seckill_price"
      :old-price="promotion.seckill_goods_vo.original_price"
      :end-time="promotion.seckill_goods_vo.distance_end_time"
      @count-end="handleCountEnd"
    />
  </div>
</template>

<script>
  /**
   * 商品页团购模块
   */
  import GoodsPromBar from './-goods-prom-bar'
  export default {
    name: 'goods-groupbuy-seckill',
    components: { GoodsPromBar },
    props: ['promotions'],
    data() {
      return {
        showPromotion: true
      }
    },
    computed: {
      promotion() {
        const { promotions } = this
        if (!promotions || !promotions.length) return false
        // 先试试看有没有团购活动
        let prom = promotions.filter(item => item.groupbuy_goods_vo)
        // 如果有团购活动，活动类型标记为groupbuy
        if (prom && prom[0]) {
          prom[0].prom_type = 'gb'
        } else {
          // 否则再试试有没有限时抢购活动
          prom = promotions.filter(item => item.seckill_goods_vo)
          if (prom && prom[0] && prom[0].seckill_goods_vo.distance_start_time < 0) {
            return false
          }
        }
        // 如果都没有，返回false
        if (!prom || !prom[0]) return false
        return prom[0]
      }
    },
    methods: {
      handleCountEnd() {
        this.showPromotion = false
        this.$alert('活动已结束，商品已恢复原价。')
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .groupbuy-container {
    position: relative;
    .iconfont {
      display: inline-block;
      width: 20px;
      height: 20px;
      margin-right: 5px;
      vertical-align: -1px;
      color: #fff;
      &.seckill {
        font-size: 20px;
      }
    }
    .pro-list {
      position: absolute;
      z-index: 3;
      top: 32px;
      left: 0;
      right: 0;
      background-color: #f3f3f3;
    }
  }
</style>
