<template>
  <en-table-layout
    :tableData="tableData.data"
    :loading="loading"
  >
    <div slot="toolbar" class="inner-toolbar">
      <div class="toolbar-btns"></div>
      <div class="toolbar-search">
        <en-table-search
          @search="searchEvent"
          @advancedSearch="advancedSearchEvent"
          advanced
        >
          <template slot="advanced-content">
            <el-form ref="advancedForm" :model="advancedForm" label-width="80px">
              <el-form-item label="商品名称">
                <el-input size="medium" v-model="advancedForm.goods_name" clearable></el-input>
              </el-form-item>
              <el-form-item label="商品编号">
                <el-input size="medium" v-model="advancedForm.goods_sn" clearable></el-input>
              </el-form-item>
              <el-form-item label="店铺名称">
                <el-input size="medium" v-model="advancedForm.seller_name" clearable></el-input>
              </el-form-item>
              <el-form-item label="商品类别">
                <en-category-picker @changed="categoryChanged"/>
              </el-form-item>
            </el-form>
          </template>
        </en-table-search>
      </div>
    </div>

    <template slot="table-columns">
      <el-table-column label="商品图片" width="120">
        <template slot-scope="scope">
          <a :href="MixinBuyerDomain + '/goods/' + scope.row.goods_id" target="_blank">
            <img :src="scope.row.thumbnail" class="goods-image"/>
          </a>
        </template>
      </el-table-column>
      <el-table-column prop="sn" label="商品编号" width="200"/>
      <el-table-column label="商品名称">
        <template slot-scope="scope">
          <a :href="MixinBuyerDomain + '/goods/' + scope.row.goods_id" class="goods_name" target="_blank">
            {{ scope.row.goods_name }}
          </a>
        </template>
      </el-table-column>
      <el-table-column prop="seller_name" label="店铺名称" width="150"/>
      <el-table-column label="商品价格" width="120">
        <template slot-scope="scope">{{ scope.row.price | unitPrice('￥') }}</template>
      </el-table-column>
      <el-table-column label="商品状态" width="80" :formatter="marketStatus"/>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.is_auth === 1"
            size="mini"
            :type="scope.row.market_enable === 0 ? 'primary' : 'danger'"
            @click="handleOperateGoods(scope.$index, scope.row)">{{ scope.row.market_enable === 0 ? '上架' : '下架' }}</el-button>
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
  import * as API_goods from '@/api/goods'

  export default {
    name: 'goodsList',
    data() {
      return {
        /** 列表loading状态 */
        loading: false,

        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10
        },

        /** 商品列表数据 */
        tableData: '',

        /** 高级搜索数据 */
        advancedForm: {
          goods_name: '',
          goods_sn: '',
          seller_name: '',
          category_path: ''
        }
      }
    },
    mounted() {
      this.GET_GoodsList()
    },
    activated() {
      this.GET_GoodsList()
    },
    methods: {

      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_GoodsList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_GoodsList()
      },

      /** 单个商品上架、下架操作确认 */
      handleOperateGoods(index, row) {
        if (row.market_enable === 0) {
          this.$confirm('确定要上架这个商品吗？', '提示', { type: 'warning' }).then(() => {
            API_goods.upGoods(row.goods_id).then(response => {
              row.market_enable = 1
              this.$message.success('上架商品成功！')
            })
          }).catch(() => {})
        } else {
          this.$prompt('请输入下架原因', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            inputPattern: /.+/,
            inputErrorMessage: '请填写下架原因！'
          }).then(({ value }) => {
            API_goods.underGoods(row.goods_id, value).then(() => {
              row.market_enable = 0
              this.$message.success('下架商品成功！')
            })
          }).catch(() => {})
        }
      },

      /** 销售状态格式化 */
      marketStatus(row, column, cellValue) {
        const { market_enable, is_auth } = row
        if (is_auth === 1) {
          return row.market_enable === 1 ? '售卖中' : '已下架'
        } else {
          return is_auth === 2 ? '审核拒绝' : '待审核'
        }
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        this.params = {
          ...this.params,
          keyword: data
        }
        this.params.page_no = 1
        Object.keys(this.advancedForm).forEach(key => delete this.params[key])
        this.GET_GoodsList()
      },

      /** 高级搜索事件触发 */
      advancedSearchEvent() {
        this.params = {
          ...this.params,
          ...this.advancedForm
        }
        delete this.params.keyword
        this.params.page_no = 1
        this.GET_GoodsList()
      },

      /** 高级搜索中 分类选择组件值发生改变 */
      categoryChanged(data) {
        this.advancedForm.category_path = data.category_path || ''
      },

      GET_GoodsList() {
        this.loading = true
        API_goods.getGoodsList(this.params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => (this.loading = false))
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
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
  .goods_name {
    color: #4183c4;
    &:hover {
      color: #f42424;
    }
  }
</style>
