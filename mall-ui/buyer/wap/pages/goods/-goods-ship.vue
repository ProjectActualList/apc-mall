<template>
  <div id="goods-ship">
    <van-cell-group class="ship-cells">
      <van-cell is-link @click="showSelector = true">
        <div slot="title" style="display: flex">
          <span>送至</span>
          <div class="area-box">
            <p class="area-str">{{ area_str || '选择配送地区' }}</p>
            <p v-if="in_store === 0" class="area-tip">无货<span style="margin-left: 5px">该配送地区暂时无货</span></p>
          </div>
        </div>
      </van-cell>
    </van-cell-group>
    <en-region-picker
      :show="showSelector"
      :api="MixinRegionApi"
      @closed="showSelector = false"
      @changed="handleShipAreaChange"
    />
    <!--<van-popup v-model="showPopup" position="bottom" style="height:70%"></van-popup>-->
  </div>
</template>

<script>
  import * as API_Goods from '@/api/goods'
  export default {
    name: 'goods-ship',
    props: ['goods-id'],
    data() {
      return {
        showSelector: false,
        in_store: 2,
        area_str: ''
      }
    },
    methods: {
      handleShipAreaChange(object) {
        const goods_id = this.goodsId
        const area_id = object.last_id
        API_Goods.getGoodsShip(goods_id, area_id).then(response => {
          this.area_str = object.string
          this.in_store = response
          this.$emit('in-stock-change', response)
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .area-box {
    font-size: 12px;
    margin-left: 5px;
  }
  .area-tip {
    color: $color-main;
  }
</style>
