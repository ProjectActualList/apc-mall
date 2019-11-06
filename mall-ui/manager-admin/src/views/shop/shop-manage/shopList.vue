<template>
  <div>
    <en-table-layout
      :tableData="tableData.data"
      :loading="loading"
    >
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
        </div>
        <div class="toolbar-search">
          <en-table-search
            @search="searchEvent"
            @advancedSearch="advancedSearchEvent"
            advanced
            advancedWidth="465"
            placeholder="请输入店铺名称"
          >
            <template slot="advanced-content">
              <el-form ref="advancedForm" :model="advancedForm" label-width="80px">
                <el-form-item label="店铺名称">
                  <el-input size="medium" v-model="advancedForm.shop_name" clearable></el-input>
                </el-form-item>
                <el-form-item label="会员名称">
                  <el-input size="medium" v-model="advancedForm.member_name" clearable></el-input>
                </el-form-item>
                <el-form-item label="店铺状态">
                  <el-select v-model="advancedForm.shop_disable" placeholder="请选择" clearable>
                    <el-option label="全部" value="ALL"/>
                    <el-option label="已开启" value="OPEN"/>
                    <el-option label="已关闭" value="CLOSED"/>
                    <el-option label="待审核" value="APPLY"/>
                    <el-option label="审核未通过" value="REFUSED"/>
                  </el-select>
                </el-form-item>
                <el-form-item label="注册日期">
                  <el-date-picker
                    v-model="advancedForm.shop_time_range"
                    type="daterange"
                    align="center"
                    :editable="false"
                    unlink-panels
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    value-format="timestamp"
                    :picker-options="{ disabledDate(time) { return time.getTime() > Date.now() }, shortcuts: MixinPickerShortcuts }">
                  </el-date-picker>
                </el-form-item>
              </el-form>
            </template>
          </en-table-search>
        </div>
      </div>
      <template slot="table-columns">
        <!--店铺ID-->
        <el-table-column prop="shop_id" label="店铺ID"/>
        <!--店铺名称-->
        <el-table-column prop="shop_name" label="店铺名称"/>
        <!--会员名称-->
        <el-table-column prop="member_name" label="会员名称"/>
        <!--创建时间-->
        <el-table-column prop="shop_createtime" :formatter="MixinUnixToDate" label="创建时间"/>
        <!--店铺状态-->
        <el-table-column label="店铺状态">
          <template slot-scope="scope">{{ scope.row.shop_disable | statusFilter }}</template>
        </el-table-column>
        <!--操作-->
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.shop_disable !== 'APPLY'"
              size="mini"
              type="primary"
              @click="handleEditShop(scope.$index, scope.row)">修改</el-button>
            <el-button
              v-if="scope.row.shop_disable === 'APPLY'"
              size="mini"
              type="primary"
              @click="$router.push({ path: `/shop/shop-manage/edit/${scope.row.shop_id}?audit=1` })">审核</el-button>
            <el-button
              v-if="scope.row.shop_disable === 'OPEN'"
              size="mini"
              type="danger"
              @click="handleCloseShop(scope.$index, scope.row)">关闭</el-button>
            <el-button
              v-if="scope.row.shop_disable === 'CLOSED'"
              size="mini"
              type="success"
              @click="handleOpenShop(scope.$index, scope.row)">开启</el-button>
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
    name: 'shopList',
    data() {
      return {
        /** 列表loading状态 */
        loading: false,
        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10,
          shop_disable: 'OPEN'
        },
        /** 列表数据 */
        tableData: '',
        /** 高级搜索数据 */
        advancedForm: {}
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

      /** 编辑店铺 */
      handleEditShop(index, row) {
        this.$router.push({ name: 'shopEdit', params: { id: row.shop_id, callback: this.GET_ShopList }})
      },

      /** 关闭店铺 */
      handleCloseShop(index, row) {
        this.$confirm('确定要关闭这个店铺吗？', '提示', { type: 'warning' }).then(() => {
          API_Shop.closeShop(row.shop_id).then(response => {
            this.$message.success('关闭成功！')
            this.GET_ShopList()
          })
        }).catch(() => {})
      },

      /** 开启店铺 */
      handleOpenShop(index, row) {
        this.$confirm('确定要开启这个店铺吗？', '提示', { type: 'warning' }).then(() => {
          API_Shop.recoverShop(row.shop_id).then(response => {
            this.$message.success('开启成功！')
            this.GET_ShopList()
          })
        }).catch(() => {})
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        this.params = {
          ...this.params,
          keyword: data
        }
        Object.keys(this.advancedForm).forEach(key => delete this.params[key])
        this.params.page_no = 1
        this.GET_ShopList()
      },

      /** 高级搜索事件触发 */
      advancedSearchEvent() {
        this.params = {
          ...this.params,
          ...this.advancedForm
        }
        delete this.params.start_time
        delete this.params.end_time
        const { shop_time_range } = this.advancedForm
        if (shop_time_range) {
          this.params.start_time = parseInt(shop_time_range[0] /= 1000)
          this.params.end_time = parseInt(shop_time_range[1] /= 1000)
        }
        delete this.params.keyword
        delete this.params.shop_time_range
        this.params.page_no = 1
        this.GET_ShopList()
      },

      /** 获取店铺列表 */
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
