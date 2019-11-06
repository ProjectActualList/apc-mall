<template>
  <div>
    <en-table-layout
      pagination
      :tableData="tableData"
      :loading="loading">
      <template slot="table-columns">
        <el-table-column label="商品名称">
          <template slot-scope="scope">
            <a :href="`${MixinBuyerDomain}/goods/${scope.row.goods_id}`" target="_blank" style="color: #00a2d4;">{{ scope.row.goods_name }}</a>
          </template>
        </el-table-column>
        <el-table-column label="活动时间">
          <template slot-scope="scope">
            <span>{{ scope.row.start_day | unixToDate}}</span>
          </template>
        </el-table-column>
        <el-table-column label="审核状态">
          <template slot-scope="scope">
            <span>{{ scope.row.status_text }}</span>
            <br>
            <el-button
              v-if="scope.row.status === 'FAIL'"
              type="text"
              @click="lookReason(scope.row)">（查看原因）</el-button>
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
  import * as API_LimitTime from '@/api/limitTime'
  import { CategoryPicker } from '@/components'

  export default {
    name: 'activityGoodsData',
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
        pageData: [],

        /** 限时抢购活动ID */
        activityID: ''
      }
    },
    watch: {
      activityID: 'GET_ActivityGoodsList'
    },
    beforeRouteUpdate(to, from, next) {
      this.activityID = to.params.id
      next()
    },
    activated() {
      this.activityID = this.$route.params.id
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_ActivityGoodsList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_ActivityGoodsList()
      },

      /** 获取活动商品信息*/
      GET_ActivityGoodsList() {
        this.loading = true
        const _params = {
          ...this.params,
          seckill_id: this.activityID
        }
        API_LimitTime.getLimitTimeGoodsList(_params).then(response => {
          this.loading = false
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
          this.tableData = response.data
        })
      },

      /** 查看原因 */
      lookReason(row) {
        this.$alert(row.fail_reason, '驳回原因')
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /deep/ .el-table td:not(.is-left) {
    text-align: center;
  }

  /*平铺*/
  div.base-info-item {
    h4 {
      margin-bottom: 10px;
      padding:0 10px;
      border: 1px solid #ddd;
      background-color: #f8f8f8;
      font-weight: bold;
      color: #333;
      font-size: 14px;
      line-height: 40px;
      text-align: left;
    }
    .el-form-item {
      margin-left: 5%;
      width: 22%;
      min-width: 300px;
    }
    .el-form-item__content {
      margin-left: 120px;
      text-align: left;
    }
    p.goods-group-manager {
      padding-left: 12.3%;
      text-align: left;
      color: #999;
      font-size: 13px;
    }

    /*积分提示*/
    p.exchange-tip {
      margin:0;
      padding: 10px;
      padding-left: 15%;
      text-align: left;
      color: #8a6d3b;
      background: #fcf8e3;
      border: 1px solid #faebcc;
    }
  }
</style>
