<template>
  <div v-loading="loading" class="container">
    <el-card>
      <div slot="header" class="chart-header">
        <div class="chart-header-item">
          <span>查询周期：</span>
          <en-year-month-picker @changed="handleYearMonthChanged"/>
        </div>
        <div class="chart-header-item">
          <span>店铺：</span>
          <en-shop-picker @changed="(shop) => { params.seller_id = shop.shop_id }"/>
        </div>
      </div>
      <div id="traffic-analysis-shop-chart" style="height: 300px"></div>
    </el-card>
  </div>
</template>

<script>
  import * as API_Statistics from '@/api/statistics'
  import echartsOptions from '../echartsOptions'

  export default {
    name: 'trafficAnalysisShop',
    data() {
      return {
        loading: false,
        params: {
          year: '',
          month: '',
          seller_id: 0,
          cycle_type: 'MONTH'
        }
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.echarts = this.$echarts.init(document.getElementById('traffic-analysis-shop-chart'))
      })
    },
    watch: {
      params: {
        handler: 'GET_TrafficAnalysis',
        deep: true
      }
    },
    methods: {
      /** 年月发生改变 */
      handleYearMonthChanged(object) {
        this.params.year = object.year
        this.params.month = object.month
        this.params.cycle_type = object.type
      },
      GET_TrafficAnalysis() {
        if (this.loading) return
        this.loading = true
        API_Statistics.getTrafficAnalysisShop(this.params).then(response => {
          this.loading = false
          const { data, localName, name } = response.series
          const { xAxis } = response
          this.echarts.setOption(echartsOptions({
            titleText: '店铺访问量',
            tooltipFormatter: (params) => {
              params = params[0]
              return `日期：${localName[params.dataIndex]}${this.params.cycle_type === 'MONTH' ? '日' : '月'}<br/>访问量：${params.value}`
            },
            seriesData: data,
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
</style>
