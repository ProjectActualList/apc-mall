<template>
  <div>
    <en-table-layout
      pagination
      :tableData="tableData"
      :loading="loading">
      <template slot="table-columns">
        <!--结算编号-->
        <el-table-column prop="bill_sn" label="结算编号" min-width="160" />
        <!--起止时间-->
        <el-table-column label="起止时间"　min-width="280">
          <template slot-scope="scope">
            {{ scope.row.start_time | unixToDate }} - {{ scope.row.end_time | unixToDate }}
          </template>
        </el-table-column>
        <!--本期应收-->
        <el-table-column label="本期应收" width="120">
          <template slot-scope="scope">{{ scope.row.bill_price | unitPrice('￥') }}</template>
        </el-table-column>
        <!--结算状态-->
        <el-table-column prop="status_text" label="结算状态" width="120"/>
        <!--付款时间-->
        <el-table-column label="付款时间" width="180">
          <template slot-scope="scope">
            {{ scope.row.create_time | unixToDate }}
          </template>
        </el-table-column>
        <!--操作-->
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button
              type="primary"
              @click="handleQueryDetail(scope.$index, scope.row)">查看详情
            </el-button>
          </template>
        </el-table-column>
      </template>
      <el-pagination
        slot="pagination"
        v-if="pageData"
        @size-change="handlePageSizeChange"
        @current-change="handlePageCurrentChange"
        :current-page="pageData.page_no"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageData.page_size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageData.data_total">
      </el-pagination>
    </en-table-layout>
  </div>
</template>

<script>
  import * as API_settlement from '@/api/settlement'
  import { CategoryPicker } from '@/components'

  export default {
    name: 'settlementManage',
    components: {
      [CategoryPicker.name]: CategoryPicker
    },
    data() {
      return {
        /** 列表loading状态 */
        loading: false,

        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10
        },

        /** 列表数据 */
        tableData: null,

        /** 列表分页数据 */
        pageData: null
      }
    },
    mounted() {
      this.GET_SetTelMentList()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_SetTelMentList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_SetTelMentList()
      },

      /** 查看详情 传递结算单号*/
      handleQueryDetail(index, row) {
        this.$router.push({ path: `/order/settlement-detail/${row.bill_id}` })
      },

      /** 获取结算单列表 */
      GET_SetTelMentList() {
        this.loading = true
        API_settlement.getSettleMentList(this.params).then(response => {
          this.loading = false
          this.tableData = response.data
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /** 工具条 */
  /deep/ div.toolbar {
    display: none;
  }

  /deep/ .el-table td:not(.is-left) {
    text-align: center;
  }

  .inner-toolbar {
    display: flex;
    width: 100%;
    justify-content: space-between;
    padding: 0 20px;
  }

  .goods-image {
    width: 50px;
    height: 50px;
  }
</style>


