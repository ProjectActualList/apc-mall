<template>
  <div class="container">
    <el-card>
      <div slot="header" class="chart-header">
        <div class="chart-header-item">
          <span>商品分类</span>
          <en-category-picker clearable @changed="(category) => { params.category_id = category.category_id || 0 }"/>
        </div>
        <div class="chart-header-item">
          <span>查询周期：</span>
          <en-year-month-picker @changed="handleYearMonthChanged"/>
        </div>
        <div class="chart-header-item">
          <span>店铺：</span>
          <en-shop-picker @changed="(shop) => { params.seller_id = shop.shop_id }"/>
        </div>
      </div>
      <en-table-layout
        :toolbar="false"
        :tableData="tableData.data"
        :loading="loading"
      >
        <template slot="table-columns">
          <el-table-column prop="category_name" label="分类名称"/>
          <el-table-column prop="avg_price" :formatter="MixinFormatPrice" label="平均价格"/>
          <el-table-column prop="nosales_goodsnum" label="有销量商品数"/>
          <el-table-column prop="sold_num" label="销量"/>
          <el-table-column prop="sales_money" label="销售额"/>
          <el-table-column prop="goods_totalnum" label="商品总数"/>
          <el-table-column prop="nosales_goodsnum" label="无销量商品数"/>
        </template>
        <el-pagination
          v-if="tableData"
          slot="pagination"
          @size-change="(page_size) => { params.page_size = page_size }"
          @current-change="(page_no) => { params.page_no = page_no }"
          :current-page="tableData.page_no"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="tableData.page_size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="tableData.data_total">
        </el-pagination>
      </en-table-layout>
    </el-card>
  </div>
</template>

<script>
  import * as API_Statistics from '@/api/statistics'

  export default {
    name: 'generalityOverview',
    data() {
      return {
        /** 列表loading状态 */
        loading: false,
        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10,
          year: '',
          month: '',
          cycle_type: 'MONTH',
          category_id: 0,
          seller_id: 0
        },
        /** 列表数据 */
        tableData: ''
      }
    },
    watch: {
      params: {
        handler: 'GET_GeneralityOverview',
        deep: true
      }
    },
    methods: {
      /** 年月份发生变化 */
      handleYearMonthChanged(object) {
        this.params.year = object.year
        this.params.month = object.month
        this.params.cycle_type = object.type
      },
      /** 获取概括总览数据 */
      GET_GeneralityOverview() {
        this.loading = true
        API_Statistics.getGeneralityOverviewData(this.params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .container {
    background-color: #fff;
    padding: 10px;
    /deep/ .el-card__body {
      padding-top: 0;
    }
    /deep/ .container {
      padding: 0;
    }
  }
  .chart-header {
    padding: 0 10px;
  }
  .chart-header-item {
    display: inline-block;
    margin-right: 30px;
  }
</style>
