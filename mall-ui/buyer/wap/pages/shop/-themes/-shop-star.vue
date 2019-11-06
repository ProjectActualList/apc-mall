<template>
  <div class="shop-star">
    <!--<el-rate
      v-model="star"
      disabled
      :colors="['#99A9BF', '#F7BA2A', '#FF9900']"/>-->
  </div>
</template>

<script>
  import Vue from 'vue'
  export default {
    name: 'EnShopStar',
    props: {
      star: {
        type: Number,
        default: 5
      },
      /** 默认颜色 */
      color: {
        type: String,
        default: '#e0e0e0'
      },
      /** 填充色 */
      fillColor: {
        type: String,
        default: '#f9ac02'
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .shop-star {
    display: inline-block;
    position: relative;
    top: -3px;
  }
</style>
