<template>
  <div id="regional-analysis-price-chart" v-loading="loading" style="height: 600px"></div>
</template>

<script>
  import * as API_Statistics from '@/api/statistics'
  import echartsOptionsMap from '../echartsOptionsMap'

  export default {
    name: 'regionalAnalysis',
    props: ['params', 'curTab'],
    data() {
      return {
        /** 列表loading状态 */
        loading: false
      }
    },
    watch: {
      curTab: 'GET_RegionalAnalysisPrice',
      params: {
        handler: 'GET_RegionalAnalysisPrice',
        deep: true
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.echarts = this.$echarts.init(document.getElementById('regional-analysis-price-chart'))
      })
    },
    methods: {
      /** 获取下单会员数据 */
      GET_RegionalAnalysisPrice() {
        if (this.curTab !== 'price' || this.loading) return
        this.loading = true
        API_Statistics.getRegionalAnalysisPrice(this.params).then(response => {
          this.loading = false
          const { data, name } = response
          const _data = data.map((item, index) => ({ name: name[index], value: data[index] }))
          this.echarts.setOption(echartsOptionsMap({
            titleText: '下单金额分布',
            seriesName: '下单金额',
            seriesData: _data,
            visualMapMax: Math.max(...data)
          }))
          this.echarts.resize()
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>
