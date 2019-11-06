<template>
  <div style="display: inline-block">
    <el-select
      v-model="shop_id"
      placeholder="选择店铺"
      @change="handleShopChanged"
      style="width: 150px"
      filterable
      clearable
      v-bind="$attrs"
    >
      <el-option
        v-for="item in shopList"
        :key="item.shop_id"
        :label="item.shop_name"
        :value="item.shop_id"/>
    </el-select>
  </div>
</template>

<script>
  import * as API_Shop from '@/api/shop'

  export default {
    name: 'EnShopPicker',
    data() {
      return {
        shop_id: '',
        shopList: []
      }
    },
    created() {
      API_Shop.getShopList().then(response => (this.shopList = response.data))
    },
    methods: {
      handleShopChanged() {
        const shop = this.shopList.filter(item => item.shop_id === this.shop_id)
        this.$emit('changed', shop[0] || { shop_id: 0, shop_name: '全平台' })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>

</style>
