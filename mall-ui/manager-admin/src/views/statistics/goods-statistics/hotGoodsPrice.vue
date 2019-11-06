<template>
  <div v-loading="loading" >
    <div id="hot-goods-price-chart" style="height: 300px"></div>
    <en-table-layout
      :toolbar="false"
      :pagination="false"
      :tableData="tableData.data"
    >
      <template slot="table-columns">
        <el-table-column type="index" width="150" label="排名"/>
        <el-table-column prop="goods_name" label="商品名称"/>
        <el-table-column prop="price" :formatter="MixinFormatPrice" label="销售金额"/>
      </template>
    </en-table-layout>
  </div>
</template>

<script>
  import * as API_Statistics from '@/api/statistics'
  import echartsOptions from '../echartsOptions'
  import { Foundation } from '~/ui-utils'

  export default {
    name: 'hotGoodsPrice',
    props: ['params', 'curTab'],
    data() {
      return {
        loading: false,
        /** 列表数据 */
        tableData: ''
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.echarts = this.$echarts.init(document.getElementById('hot-goods-price-chart'))
      })
    },
    watch: {
      curTab: 'GET_HotGoodsPrice',
      params: {
        handler: 'GET_HotGoodsPrice',
        deep: true
      }
    },
    methods: {
      /** 获取会员下单量 */
      GET_HotGoodsPrice() {
        if (this.curTab !== 'price' || this.loading) return
        this.loading = true
        Promise.all([
          API_Statistics.getHotGoodsPrice(this.params),
          API_Statistics.getHotGoodsPricePage(this.params)
        ]).then(responses => {
          this.loading = false
          this.tableData = responses[1]
          const { data: _data, localName: _name } = responses[0].series
          const { xAxis } = responses[0]
          this.echarts.setOption(echartsOptions({
            titleText: '热卖商品销量TOP' + xAxis.length,
            tooltipFormatter: function(params) {
              params = params[0]
              const member_name = _name[params.dataIndex]
              return `商品名称：${member_name}<br/>销售金额：￥${Foundation.formatPrice(params.value)}`
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

</style>
