<template>
  <div id="regional-analysis-num-chart" v-loading="loading" style="height: 600px"></div>
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
      curTab: 'GET_RegionalAnalysisNum',
      params: {
        handler: 'GET_RegionalAnalysisNum',
        deep: true
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.echarts = this.$echarts.init(document.getElementById('regional-analysis-num-chart'))
      })
    },
    methods: {
      /** 获取下单会员数据 */
      GET_RegionalAnalysisNum() {
        if (this.curTab !== 'num' || this.loading) return
        this.loading = true
        API_Statistics.getRegionalAnalysisNum(this.params).then(response => {
          this.loading = false
          const { data, name } = response
          const _data = data.map((item, index) => ({ name: name[index], value: data[index] }))
          this.echarts.setOption(echartsOptionsMap({
            titleText: '下单量分布',
            seriesName: '下单数量',
            seriesData: _data,
            visualMapMax: Math.max(...data)
          }))
          this.echarts.resize()
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>
