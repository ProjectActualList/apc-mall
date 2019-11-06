<template>
  <div v-loading="loading" id="order-statistics-price-chart" style="height: 300px"></div>
</template>

<script>
  import * as API_Statistics from '@/api/statistics'
  import echartsOptions from '../echartsOptions'
  import { Foundation } from '~/ui-utils'

  export default {
    name: 'orderStatisticsOrder',
    props: ['params', 'curTab'],
    data() {
      return {
        loading: false
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.echarts = this.$echarts.init(document.getElementById('order-statistics-price-chart'))
      })
    },
    watch: {
      curTab: 'GET_OrderStatisticsPrice',
      params: {
        handler: 'GET_OrderStatisticsPrice',
        deep: true
      }
    },
    methods: {
      /** 获取会员下单金额 */
      GET_OrderStatisticsPrice() {
        if (this.curTab !== 'price' || this.loading) return
        this.loading = true
        this.$emit('update')
        const { cycle_type } = this.params
        const type_str = this.params.cycle_type === 'MONTH' ? '日' : '月'
        API_Statistics.getOrderStatisticsPrice(this.params).then(response => {
          this.loading = false
          const { xAxis } = response
          const series0 = response.series[0]
          const series1 = response.series[1]
          this.echarts.setOption(echartsOptions({
            color: ['#c23531', '#2f4554'],
            titleText: `订单销售金额统计（${cycle_type === 'MONTH' ? '月' : '年'}）`,
            tooltipFormatter: (params) => {
              return `日期：${params[0].dataIndex + 1}${type_str}<br/>
                      ${params[0].marker}${params[0].seriesName}下单金额：￥${Foundation.formatPrice(params[0].value)}<br/>
                      ${params[1].marker}${params[1].seriesName}下单金额：￥${Foundation.formatPrice(params[1].value)}`
            },
            legend: {
              right: 50,
              data: [series0.name, series1.name]
            },
            xAxisData: xAxis,
            seriesName: '下单金额',
            series: [
              {
                type: 'line',
                name: series0.name,
                data: series0.data
              },
              {
                type: 'line',
                name: series1.name,
                data: series1.data
              }
            ]
          }))
          this.echarts.resize()
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>
