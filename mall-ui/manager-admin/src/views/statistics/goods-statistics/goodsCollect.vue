<template>
  <div class="container" v-loading="loading">
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
      <div id="goods-collect-top-chart" style="height: 300px"></div>
      <en-table-layout
        :toolbar="false"
        :pagination="false"
        :tableData="tableData.data"
      >
        <template slot="table-columns">
          <el-table-column type="index" width="150" label="排名"/>
          <el-table-column prop="goods_name" label="商品名称"/>
          <el-table-column prop="price" label="商品价格" :formatter="MixinFormatPrice"/>
          <el-table-column prop="seller_name" label="店铺名称"/>
          <el-table-column prop="favorite_num" label="收藏数量"/>
        </template>
      </en-table-layout>
    </el-card>
  </div>
</template>

<script>
  import * as API_Statistics from '@/api/statistics'
  import echartsOptions from '../echartsOptions'

  export default {
    name: 'goodsCollect',
    data() {
      return {
        loading: false,
        tableData: '',
        params: {
          year: '',
          month: '',
          cycle_type: 'MONTH',
          category_id: 0,
          seller_id: 0
        }
      }
    },
    watch: {
      params: {
        handler: 'GET_GoodsCollectTop',
        deep: true
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.echarts = this.$echarts.init(document.getElementById('goods-collect-top-chart'))
      })
    },
    methods: {
      /** 年月份发生变化 */
      handleYearMonthChanged(object) {
        this.params.year = object.year
        this.params.month = object.month
        this.params.cycle_type = object.type
      },
      /** 获取商品收藏排行 */
      GET_GoodsCollectTop() {
        this.loading = true
        Promise.all([
          API_Statistics.getGoodsCollectTop(this.params),
          API_Statistics.getGoodsCollectTopPage(this.params)
        ]).then(responses => {
          this.loading = false
          this.tableData = responses[1]
          const { data: _data, name: _name, localName } = responses[0].series
          const { xAxis } = responses[0]
          this.echarts.setOption(echartsOptions({
            titleText: '商品收藏TOP' + xAxis.length,
            tooltipFormatter: function(params) {
              params = params[0]
              return `商品名称：${localName[params.dataIndex]}<br/>收藏数量：${params.value}`
            },
            seriesData: _data,
            xAxisData: xAxis
          }))
          this.echarts.resize()
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
  .tab-chart {
    height: 300px;
  }
</style>
