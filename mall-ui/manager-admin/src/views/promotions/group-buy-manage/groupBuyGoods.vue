<template>
  <en-table-layout
    :tableData="tableData.data"
    :loading="loading"
  >
    <template slot="table-columns">
      <el-table-column prop="gb_name" label="团购名称"/>
      <el-table-column prop="gb_title" label="活动标题"/>
      <el-table-column label="商品名称">
        <template slot-scope="scope">
          <a :href="MixinBuyerDomain + '/goods/' + scope.row.goods_id" target="_blank" class="goods-name">{{ scope.row.goods_name }}</a>
        </template>
      </el-table-column>
      <el-table-column label="活动开始时间">
        <template slot-scope="scope">{{ scope.row.start_time | unixToDate }}</template>
      </el-table-column>
      <el-table-column label="活动结束时间">
        <template slot-scope="scope">{{ scope.row.end_time | unixToDate }}</template>
      </el-table-column>
      <el-table-column prop="gb_status_text" label="团购活动状态"/>
      <el-table-column label="操作" width="230">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.gb_status === 0"
            size="mini"
            type="primary"
            @click="handlePassGoods(scope.$index, scope.row)">通过</el-button>
          <el-button
            v-if="scope.row.gb_status === 0"
            size="mini"
            type="warning"
            @click="handleRejectGoods(scope.$index, scope.row)">拒绝</el-button>
          <el-button
            size="mini"
            type="primary"
            @click="handleViewMember(scope.$index, scope.row)">查看</el-button>
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
  import * as API_Promotion from '@/api/promotion'

  export default {
    name: 'groupBuyGoods',
    data() {
      return {
        /** 列表loading状态 */
        loading: false,

        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10,
          act_id: this.$route.params.id
        },

        /** 列表数据 */
        tableData: ''
      }
    },
    mounted() {
      this.GET_GroupBuyGoodsList()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_GroupBuyGoodsList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_GroupBuyGoodsList()
      },

      /** 通过 */
      handlePassGoods(index, row) {
        this.$confirm('确定要通过这个活动吗？', '提示', { type: 'warning' }).then(() => {
          const { act_id, gb_id } = row
          API_Promotion.reviewGroupBuyGoods(act_id, { status: 1, gb_id }).then(() => {
            this.$message.success('已审核通过！')
            this.GET_GroupBuyGoodsList()
          })
        }).catch(() => {})
      },

      /** 拒绝 */
      handleRejectGoods(index, row) {
        this.$confirm('确定要拒绝这个活动吗？', '提示', { type: 'warning' }).then(() => {
          const { act_id, gb_id } = row
          API_Promotion.reviewGroupBuyGoods(act_id, { status: 2, gb_id }).then(() => {
            this.$message.success('已拒绝！')
            this.GET_GroupBuyGoodsList()
          })
        }).catch(() => {})
      },

      /** 查看详情 */
      handleViewMember(index, row) {
        this.$router.push({ path: `/promotions/group-buy-manage/group-buy-goods-info/${row.gb_id}` })
      },

      /** 获取团购活动详情商品列表 */
      GET_GroupBuyGoodsList() {
        this.loading = true
        API_Promotion.getGroupBuyGoods(this.params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .goods-name {
    color: #4183c4;
    &:hover { color: #f42424 }
  }
</style>
