<template>
  <div>
    <en-table-layout
      :tableData="tableData.data"
      :loading="loading"
    >
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns"></div>
        <div class="toolbar-search">
          <en-table-search @search="searchEvent" placeholder="请输入活动名称" />
        </div>
      </div>
      <template slot="table-columns">
        <el-table-column prop="promotion_name" label="活动名称"/>
        <el-table-column prop="seller_name" label="店铺名称"/>
        <el-table-column prop="start_time" width="150" :formatter="MixinUnixToDate" label="开始时间" />
        <el-table-column prop="end_time" width="150" :formatter="MixinUnixToDate" label="结束时间"/>
        <el-table-column prop="status" :formatter="AssembleStatusText" width="120" label="活动状态"/>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleViewAssembleDetail(scope.row)"
            >查看</el-button>
            <el-button
              v-if="scope.row.option_status !== 'NOTHING'"
              size="mini"
              type="danger"
              @click="handleCloseAssemble(scope.row)"
            >{{ scope.row.option_status | operaStatus }}</el-button>
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
  import * as API_Promotion from '@/api/promotion'
  import { Foundation } from '~/ui-utils'

  export default {
    name: 'assembleList',
    data() {
      return {
        // 列表loading状态
        loading: false,
        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10
        },
        /** 列表数据 */
        tableData: {
          data: []
        },

        /** 开始/关闭文本提示 */
        tip: '关闭'
      }
    },
    mounted() {
      this.GET_AssembleList()
    },
    filters: {
      operaStatus(val) {
        switch (val) {
          case 'CAN_OPEN': return '开启'
          case 'CAN_CLOSE': return '关闭'
        }
      }
    },
    methods: {

      /** 状态文本显示 */
      AssembleStatusText({ status }) {
        switch (status) {
          case 'WAIT': return '待开始'
          case 'UNDERWAY': return '进行中'
          case 'END': return '已结束'
        }
      },

      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_AssembleList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_AssembleList()
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        this.params = {
          ...this.params,
          name: data
        }
        this.params.page_no = 1
        this.GET_AssembleList()
      },

      /** 获取拼团活动列表 */
      GET_AssembleList() {
        this.loading = true
        API_Promotion.getAssembleList(this.params).then(response => {
          this.loading = false
          this.tableData = { ...response }
        })
      },

      /** 关闭/开启某个拼团活动 */
      handleCloseAssemble(row) {
        const tip = row.option_status === 'CAN_OPEN' ? '开启' : '关闭'
        this.$confirm(`确定要${tip}此拼团活动？`, '提示', { type: 'warning' }).then(() => {
          Promise.all([
            row.option_status === 'CAN_OPEN'
              ? API_Promotion.openAssemble(row.promotion_id)
              : API_Promotion.deleteAssemble(row.promotion_id)
          ]).then(response => {
            this.$message.success(`${tip}成功`)
            this.GET_AssembleList()
          }).catch(_ => {})
        }).catch(() => {})
      },

      /** 查看某个拼团活动详情 */
      handleViewAssembleDetail(row) {
        this.$router.push({ name: 'assembleDetail', params: { promotion_id: row.promotion_id }})
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .time-tag {
    display: block;
    width: 80px;
    margin: 5px 0;
  }
  .input-new-tag {
    width: 112px;
  }

  /deep/ input::-webkit-outer-spin-button,
  /deep/ input::-webkit-inner-spin-button {
    -webkit-appearance: none !important;
    margin: 0;
  }
</style>
