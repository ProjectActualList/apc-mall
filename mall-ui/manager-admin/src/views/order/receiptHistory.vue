<template>
  <en-table-layout
    :tableData="tableData.data"
    :loading="loading"
    :toolbar="false"
  >
    <template slot="table-columns">
      <el-table-column prop="add_time" :formatter="MixinUnixToDate" label="日期"/>
      <el-table-column prop="member_name" label="会员名称"/>
      <el-table-column prop="order_sn" label="订单编号"/>
      <el-table-column prop="receipt_amount" :formatter="MixinFormatPrice" label="发票金额"/>
      <el-table-column prop="receipt_type" label="发票类别"/>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleOperateReceipt(scope.$index, scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </template>
    <el-pagination
      v-if="tableData"
      slot="pagination"
      @size-change="handlePageSizeChange"
      @current-change="handlePageCurrentChange"
      :current-page="tableData.page_no"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="tableData.page_size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="tableData.data_total">
    </el-pagination>
  </en-table-layout>
</template>

<script>
  import * as API_Receipt from '@/api/receipt'

  export default {
    name: 'receiptHistory',
    data() {
      return {
        // 列表loading状态
        loading: false,
        // 列表参数
        params: {
          page_no: 1,
          page_size: 10
        },
        // 列表数据
        tableData: ''
      }
    },
    mounted() {
      this.GET_ReceiptHistory()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_ReceiptHistory()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_ReceiptHistory()
      },

      /** 查看发票 */
      handleOperateReceipt(index, row) {
        this.$router.push({ name: 'receiptDetail', params: { id: row.history_id }})
      },

      /** 获取发票历史 */
      GET_ReceiptHistory() {
        this.loading = true
        API_Receipt.getHistoryReceiptList(this.params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /deep/ .el-table td:not(.is-left) {
    text-align: center;
  }

  .item-receipt {
    padding: 10px;

    .item-receipt-label {
      display: inline-block;
    }

    .item-receipt-value {
      display: inline-block;
      margin-left: 20px;
    }
  }
</style>
