<template>
  <div class="container" v-loading="loading">
    <el-card>
      <div slot="header" class="chart-header">
        <div slot="header" class="chart-header">
          <div class="chart-header-item">
            <span>查询周期：</span>
            <en-year-month-picker @changed="handleYearMonthChanged"/>
          </div>
          <div class="chart-header-item">
            <span>表格类型：</span>
            <el-select v-model="tableType" style="width: 150px">
              <el-option label="销售单" value="sales"></el-option>
              <el-option label="退款单" value="after"></el-option>
            </el-select>
          </div>
        </div>
      </div>
      <div class="total-box">
        销售收入总览：
        <span>收款金额: {{ MixinFormatPrice('', '', totalData.receive_money || 0) }}</span>
        <span>退款金额: {{ MixinFormatPrice('', '', totalData.refund_money || 0) }}</span>
        <span>实收金额: {{ MixinFormatPrice('', '', totalData.real_money || 0) }}</span>
      </div>
      <en-table-layout
        :toolbar="false"
        :tableData="tableData.data"
        border
      >
        <template v-if="tableType === 'sales'" slot="table-columns">
          <el-table-column prop="goods_name" label="商品名称"/>
          <el-table-column prop="price" :formatter="MixinFormatPrice" label="商品单价"/>
          <el-table-column prop="goods_num" label="商品数量" width="100"/>
          <el-table-column prop="total" :formatter="MixinFormatPrice" label="订单金额"/>
        </template>
        <template v-else slot="table-columns">
          <el-table-column prop="refund_sn" label="退款单号"/>
          <el-table-column prop="order_sn" label="订单号"/>
          <el-table-column prop="create_time" :formatter="MixinUnixToDate" label="创建时间"/>
          <el-table-column prop="refund_price" :formatter="MixinFormatPrice" label="退款金额"/>
        </template>
        <el-pagination
          v-if="tableData"
          slot="pagination"
          @size-change="(page_size) => { params.page_size = page_size }"
          @current-change="(page_no) => { params.page_no = page_no }"
          :current-page="tableData.page_no"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="tableData.page_size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="tableData.data_total">
        </el-pagination>
      </en-table-layout>
    </el-card>
  </div>
</template>

<script>
  import * as API_Statistics from '@/api/statistics'

  export default {
    name: 'salesRevenueStatistics',
    data() {
      return {
        /** 列表loading状态 */
        loading: false,
        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10,
          year: '',
          month: '',
          cycle_type: 'MONTH'
        },
        /** 列表数据 */
        tableData: '',
        /** 总览 */
        totalData: '',
        // 是否为退款单类型
        tableType: 'sales'
      }
    },
    watch: {
      params: {
        handler: 'GET_SalesRevenueStatistics',
        deep: true
      },
      tableType: 'GET_SalesRevenueStatistics'
    },
    methods: {
      /** 年月发生改变 */
      handleYearMonthChanged(object) {
        this.params.year = object.year
        this.params.month = object.month
        this.params.cycle_type = object.type
      },

      /** 获取销售收入统计 */
      GET_SalesRevenueStatistics() {
        this.loading = true
        const { tableType } = this
        const tableApi = tableType === 'sales'
          ? API_Statistics.getSalesRevenueStatisticsPage
          : API_Statistics.getSalesAftersaleStatistics
        Promise.all([
          tableApi(this.params),
          API_Statistics.getSalesRevenueStatisticsTotal(this.params)
        ]).then(responses => {
          this.loading = false
          this.tableData = responses[0]
          this.totalData = responses[1]
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
  .total-box {
    padding: 10px;
    span { margin-left: 15px }
  }
</style>
