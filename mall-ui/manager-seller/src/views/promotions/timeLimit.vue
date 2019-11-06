<template>
  <div>
    <en-table-layout
      toolbar
      pagination
      :tableData="tableData"
      :loading="loading"
    >
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns"></div>
        <div class="toolbar-search">
          <en-table-search @search="searchEvent"></en-table-search>
        </div>
      </div>
      <template slot="table-columns">
        <el-table-column prop="seckill_name" label="活动名称"/>
        <el-table-column label="活动时间" width="150">
          <template slot-scope="scope">
            <span>{{ scope.row.start_day | unixToDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="报名截止时间" width="150">
          <template slot-scope="scope">
            <span>{{ scope.row.apply_end_time | unixToDate }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="seckill_rule" label="报名条件"/>
        <el-table-column label="报名状态" :formatter="marketStatus" width="80"/>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button
              type="primary"
              v-if="scope.row.is_apply === 0"
              @click="handleSignUpTimeLimt(scope.row)">报名
            </el-button>
            <el-button
              type="primary"
              v-else
              @click="activityGoodsInfo(scope.row)">查看商品
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
  import * as API_limitTime from '@/api/limitTime'
  import { CategoryPicker } from '@/components'

  export default {
    name: 'timeLimit',
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
        tableData: [],

        /** 列表分页数据 */
        pageData: []
      }
    },
    mounted() {
      this.GET_LimitActivityList()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_LimitActivityList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_LimitActivityList()
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        delete this.params.keywords
        this.params = {
          ...this.params,
          keywords: data
        }
        this.GET_LimitActivityList()
      },

      /** 报名状态格式化 */
      marketStatus(row, column, cellValue) {
        switch (row.is_apply) {
          case 0: return '未报名'
          case 1: return '已报名'
          case 2: return '已截止'
        }
      },

      /** 获取限时活动列表*/
      GET_LimitActivityList() {
        this.loading = true
        API_limitTime.getLimitTimeActivityList(this.params).then(response => {
          this.loading = false
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
          this.tableData = response.data
        })
      },

      /** 报名 */
      handleSignUpTimeLimt(row) {
        this.$router.push({ name: 'addTimeLimit', params: { id: row.seckill_id, callback: this.GET_LimitActivityList }})
      },

      /** 活动商品信息 已报名*/
      activityGoodsInfo(row) {
        this.$router.push({ path: `/promotions/activity-goods-data/${row.seckill_id}` })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /deep/ div.toolbar {
    height: 70px;
    padding: 20px 0;
  }

  /deep/ .el-table td:not(.is-left) {
    text-align: center;
  }

  .inner-toolbar {
    display: flex;
    width: 100%;
    justify-content: space-between;
  }

  .toolbar-search {
    margin-right: 10px;
  }

  .goods-image {
    width: 50px;
    height: 50px;
  }

</style>
