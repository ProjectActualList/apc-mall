<template>
  <div class="container">
    <el-card>
      <div slot="header" class="chart-header">
        <div class="chart-header-item">
          <span>商品分类</span>
          <en-category-picker clearable @changed="(category) => { params.category_id = category.category_id || 0 }"/>
        </div>
        <div class="chart-header-item">
          <span>查询周期：</span>
          <en-year-month-picker @changed="handleYearMonthChanged"/>
        </div>
        <div class="chart-header-item">
          <span>店铺：</span>
          <en-shop-picker @changed="(shop) => { params.seller_id = shop.shop_id }"/>
        </div>
      </div>
      <el-tabs v-model="cur_tab" type="card">
        <el-tab-pane label="销售金额" name="price">
          <hot-goods-price :params="params" :cur-tab="cur_tab"/>
        </el-tab-pane>
        <el-tab-pane label="销售数量" name="num">
          <hot-goods-num :params="params" :cur-tab="cur_tab"/>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
  import HotGoodsPrice from './hotGoodsPrice'
  import HotGoodsNum from './hotGoodsNum'

  export default {
    name: 'hotGoods',
    components: {
      HotGoodsPrice,
      HotGoodsNum
    },
    data() {
      return {
        cur_tab: 'price',
        params: {
          year: '',
          month: '',
          cycle_type: 'MONTH',
          category_id: 0,
          seller_id: 0
        }
      }
    },
    methods: {
      /** 年月份发生变化 */
      handleYearMonthChanged(object) {
        this.params.year = object.year
        this.params.month = object.month
        this.params.cycle_type = object.type
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .container {
    background-color: #fff;
    padding: 10px;
  }
  .chart-header {
    padding: 0 10px;
  }
  .chart-header-item {
    display: inline-block;
    margin-right: 30px;
  }
  .tab-chart {
    height: 300px;
  }
</style>
