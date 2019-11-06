<template>
  <div v-loading="loading" class="container">
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
        border
      >
        <template slot="table-columns">
          <el-table-column prop="goods_name" label="商品名称"/>
          <el-table-column prop="order_num" label="下单量"/>
          <el-table-column prop="num" label="下单商品件数"/>
          <el-table-column prop="price" label="下单金额" :formatter="MixinFormatPrice"/>
        </template>
        <el-pagination
          v-if="tableData"
          slot="pagination"
          @size-change="(size) => { params.page_size = size }"
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
    name: 'goodsSalesDetails',
    data() {
      return {
        loading: false,
        /** 列表数据 */
        tableData: '',
        /** 参数 */
        params: {
          year: '',
          month: '',
          cycle_type: 'MONTH',
          category_id: 0,
          seller_id: 0,
          page_no: 1,
          page_size: 10
        }
      }
    },
    watch: {
      params: {
        handler: 'GET_GoodsSalesDetail',
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

      /** 获取商品销售明细 */
      GET_GoodsSalesDetail() {
        this.loading = true
        API_Statistics.getGoodsSaleDetail(this.params).then(response => {
          this.loading = false
          this.tableData = response
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .container {
    background-color: #fff;
    padding: 10px;
  }
  .chart-header {
    padding: 0 10px;
  }
  .chart-header-item {
    display: inline-block;
    margin-right: 30px;
  }
  .tab-chart {
    height: 300px;
  }
</style>
