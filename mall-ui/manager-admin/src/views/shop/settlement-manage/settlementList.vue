<template>
  <div>
    <en-table-layout
      :toolbar="false"
      :tableData="tableData.data"
      :loading="loading"
    >
      <template slot="table-columns">
        <el-table-column prop="bill_sn" label="结算单号"/>
        <el-table-column prop="shop_name" label="店铺名称"/>
        <el-table-column prop="price" :formatter="MixinFormatPrice" label="订单金额"/>
        <el-table-column prop="commi_price" :formatter="MixinFormatPrice" label="收取佣金"/>
        <el-table-column prop="refund_price" :formatter="MixinFormatPrice" label="退单金额"/>
        <el-table-column prop="refund_commi_price" :formatter="MixinFormatPrice" label="退还佣金"/>
        <el-table-column prop="bill_price" :formatter="MixinFormatPrice" label="本期应结"/>
        <el-table-column prop="status_text" label="订单状态"/>
        <el-table-column prop="create_time" :formatter="MixinUnixToDate" label="出账日期"/>
        <el-table-column prop="start_time" :formatter="MixinUnixToDate" label="开始日期"/>
        <el-table-column prop="end_time" :formatter="MixinUnixToDate" label="结束日期"/>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleOperateSettlement(scope.$index, scope.row)">查看</el-button>
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
  </div>
</template>

<script>
  import * as API_Order from '@/api/order'

  export default {
    name: 'settlementList',
    data() {
      return {
        // 列表loading状态
        loading: false,
        // 列表参数
        params: {
          page_no: 1,
          page_size: 10,
          sn: this.$route.params.sn
        },
        // 列表数据
        tableData: ''
      }
    },
    beforeRouteUpdate(to, from, next) {
      this.params.sn = to.params.sn
      next()
    },
    activated() {
      this.params.sn = this.$route.params.sn
    },
    watch: {
      'params.sn': 'GET_SettlementList'
    },
    mounted() {
      this.GET_SettlementList()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_SettlementList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_SettlementList()
      },

      /** 当选择项发生变化 */
      handleSelectionChange(val) {
        this.selectedData = val.map(item => item.id)
      },

      /** 操作结算单 */
      handleOperateSettlement(index, row) {
        this.$router.push({ path: `/shop/settlement-manage/detail/${row.bill_id}` })
      },

      /** 获取结算单列表 */
      GET_SettlementList() {
        this.loading = true
        API_Order.getSettlementList(this.params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>

</style>
