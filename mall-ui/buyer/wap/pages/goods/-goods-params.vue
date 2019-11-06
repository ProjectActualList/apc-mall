<template>
  <div id="goods-params" class="goods-params">
    <table v-for="params in goodsParams" :key="params.group_id" class="params-table">
      <thead>
      <tr><th colspan="2" style="background-color: #f0f0f0">{{ params.group_name }}</th></tr>
      </thead>
      <tbody>
      <tr v-for="param in params.params" :key="param.param_id">
        <td>{{ param.param_name }}</td>
        <td>{{ param.param_value }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
  /**
   * 商品参数模块
   */
  export default {
    name: "goods-params",
    props: ['goodsParams']
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .goods-params {
    padding: 10px;
    .params-table {
      width: 100%;
      text-align: left;
      border-collapse:collapse;
      th, td {
        border: 1px solid #ddd;
        padding: 8px;
        margin: 0;
        outline: 0;
      }
      td:first-child { width: 150px }
    }
  }
</style>
