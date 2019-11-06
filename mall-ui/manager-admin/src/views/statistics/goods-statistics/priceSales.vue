<template>
  <div class="container" v-loading="loading">
    <el-card>
      <div slot="header" class="chart-header">
        <div class="chart-header-item">
          <span>商品分类</span>
          <en-category-picker clearable @changed="(category) => { params.category_id = category.category_id || 0 }"/>
        </div>
        <div class="chart-header-item">
          <span>销售周期：</span>
          <en-year-month-picker @changed="handleYearMonthChanged"/>
        </div>
        <div class="chart-header-item">
          <span>店铺：</span>
          <en-shop-picker @changed="(shop) => { params.seller_id = shop.shop_id }"/>
        </div>
        <div class="chart-header-item">
          <span>价格区间：</span>
          <en-price-range :default-range="priceRange" @changed="handleRriceRangeChanged"/>
        </div>
      </div>
      <div id="price-sales-chart" style="height: 300px"></div>
    </el-card>
  </div>
</template>

<script>
  import * as API_Statistics from '@/api/statistics'
  import echartsOptions from '../echartsOptions'

  export default {
    name: 'priceSales',
    data() {
      return {
        loading: false,
        shopList: [],
        params: {
          category_id: 0,
          seller_id: 0,
          year: '',
          month: '',
          cycle_type: 'MONTH'
        },
        priceRange: [[0, 100], [100, 1000], [1000, 10000], [10000, 50000]]
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.echarts = this.$echarts.init(document.getElementById('price-sales-chart'))
      })
    },
    watch: {
      params: {
        handler: 'GET_PriceSalesData',
        deep: true
      }
    },
    methods: {
      /** 年月份发生变化 */
      handleYearMonthChanged(object) {
        this.params.year = object.year
        this.params.month = object.month
        this.params.cycle_type = object.type
      },
      /** 价格区间发生改变 */
      handleRriceRangeChanged(range) {
        this.priceRange = range
        this.GET_PriceSalesData()
      },

      /** 获取价格销量数据 */
      GET_PriceSalesData() {
        this.loading = true
        const prices = []
        const pricesLen = this.priceRange.length
        this.priceRange.forEach((item, index) => {
          prices.push(item[0])
          if (index === pricesLen - 1) prices.push(item[1])
        })
        this.params.prices = prices
        API_Statistics.getPriceSales(this.params).then(response => {
          this.loading = false
          const { data, name, localName } = response.series
          const { xAxis } = response
          this.echarts.setOption(echartsOptions({
            titleText: '价格销量分布',
            tooltipFormatter: function(params) {
              params = params[0]
              return `${localName[params.dataIndex]}元：${params.value}件`
            },
            xAxisData: xAxis,
            seriesData: data
          }))
        }).catch(() => { this.loading = false })
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
</style>
