<template>
  <div class="container">
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
      <el-tabs v-model="cur_tab" type="card">
        <el-tab-pane label="下单会员" name="member">
          <regional-analysis-member :params="params" :cur-tab="cur_tab"/>
        </el-tab-pane>
        <el-tab-pane label="下单量" name="num">
          <regional-analysis-num :params="params" :cur-tab="cur_tab"/>
        </el-tab-pane>
        <el-tab-pane label="下单金额" name="price">
          <regional-analysis-price :params="params" :cur-tab="cur_tab"/>
        </el-tab-pane>
      </el-tabs>
      <en-table-layout
        :toolbar="false"
        :pagination="false"
        :tableData="tableData.data"
        :loading="loading"
        border
      >
        <template slot="table-columns">
          <el-table-column prop="region_name" label="地区"/>
          <el-table-column prop="member_num" label="下单会员数"/>
          <el-table-column prop="sn_num" label="下单量"/>
          <el-table-column prop="order_price" label="下单金额"/>
        </template>
      </en-table-layout>
    </el-card>
  </div>
</template>

<script>
  import * as API_Statistics from '@/api/statistics'
  import RegionalAnalysisMember from './regionalAnalysisMember'
  import RegionalAnalysisNum from './regionalAnalysisNum'
  import RegionalAnalysisPrice from './regionalAnalysisPrice'
  import echartMapChina from '@/assets/echart-map-china'

  export default {
    name: 'regionalAnalysis',
    components: {
      RegionalAnalysisMember,
      RegionalAnalysisNum,
      RegionalAnalysisPrice
    },
    data() {
      return {
        cur_tab: 'member',
        params: {
          year: '',
          month: '',
          cycle_type: 'MONTH',
          seller_id: 0
        },
        loading: false,
        tableData: ''
      }
    },
    created() {
      this.$echarts.registerMap('china', echartMapChina)
    },
    watch: {
      params: {
        handler: 'GET_RegionalAnalysisTable',
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
      GET_RegionalAnalysisTable() {
        this.loading = true
        API_Statistics.getRegionalAnalysisTable(this.params).then(response => {
          this.loading = false
          this.tableData = response
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
