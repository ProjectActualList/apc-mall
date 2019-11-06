
<template>
  <el-table :data="skuList" style="width: 100%">
    <el-table-column label="商品名称" class-name="goods-name-img">
      <template slot-scope="scope">
        <a :href="'/goods/' + scope.row.goods_id" target="_blank">
          <img :src="scope.row[image]">
        </a>
        <div style="display: inline-block">
          <a :href="'/goods/' + scope.row.goods_id" target="_blank" class="goods-name">{{ scope.row[name] }}</a>
          <p v-if="scope.row.spec_list" class="sku-spec">{{ scope.row | formatterSkuSpec }}</p>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="商品单价" width="120">
      <template slot-scope="scope">{{ scope.row[price] | unitPrice('￥') }}</template>
    </el-table-column>
    <el-table-column label="数量" width="90">
      <template slot-scope="scope">{{ scope.row[num] }}</template>
    </el-table-column>
    <el-table-column label="商品小计" width="120">
      <template slot-scope="scope">{{ (scope.row[total] || (scope.row[num] * scope.row[price])) | unitPrice('￥') }}</template>
    </el-table-column>
  </el-table>
</template>

<script>
  export default {
    name: 'member-sku-list',
    props: {
      skuList: {
        type: Array
      },
      image: {
        default: 'goods_image'
      },
      name: {
        default: 'goods_name'
      },
      price: {
        default: 'price'
      },
      num: {
        default: 'num'
      },
      total: {
        default: 'total'
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .el-table {
    border: 1px solid #ebeef5;
    border-bottom: none;
    font-size: 12px;
  }
  .el-table /deep/ .el-table__header .cell { text-align: center }
  .el-table /deep/ .el-table__body .cell { text-align: center }
  .el-table /deep/ .el-table__body .goods-name-img .cell {
    display: flex;
    align-items: center;
    text-align: left;
    .goods-name {
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 1;
      overflow: hidden;
    }
    img {
      width: 50px;
      height: 50px;
      margin-right: 10px;
    }
  }
</style>
