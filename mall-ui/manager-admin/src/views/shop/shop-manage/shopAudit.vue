<template>
  <div>
    <en-table-layout
      :toolbar="false"
      :tableData="tableData.data"
      :loading="loading"
    >
      <template slot="table-columns">
        <!--店铺名称-->
        <el-table-column prop="shop_name" label="店铺名称"/>
        <!--会员名称-->
        <el-table-column prop="member_name" label="会员名称"/>
        <!--所在地-->
        <el-table-column label="所在地">
          <template slot-scope="scope">{{ scope.row.shop_province + scope.row.shop_city + scope.row.shop_county + scope.row.shop_town }}</template>
        </el-table-column>
        <!--店铺状态-->
        <el-table-column label="店铺状态">
          <template slot-scope="scope">{{ scope.row.shop_disable | statusFilter }}</template>
        </el-table-column>
        <!--操作-->
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleAuditShop(scope.$index, scope.row)">操作</el-button>
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
  import * as API_Shop from '@/api/shop'

  export default {
    name: 'shopAudit',
    data() {
      return {
        /** 列表loading状态 */
        loading: false,

        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10,
          shop_disable: 'APPLY'
        },

        /** 列表数据 */
        tableData: ''
      }
    },
    mounted() {
      this.GET_ShopList()
    },
    filters: {
      statusFilter(val) {
        switch (val) {
          case 'REFUSED': return '审核未通过'
          case 'APPLY': return '待审核'
          case 'OPEN': return '开启中'
          case 'CLOSED': return '已关闭'
          default: return '未知状态'
        }
      }
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_ShopList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_ShopList()
      },

      /** 审核店铺 */
      handleAuditShop(index, row) {
        this.$router.push({ name: 'shopEdit', query: { audit: 1 }, params: { id: row.shop_id, callback: this.GET_ShopList }})
      },

      /** 获取店铺审核列表 */
      GET_ShopList() {
        this.loading = true
        API_Shop.getShopList(this.params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>
