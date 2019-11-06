<template>
  <div v-if="shop">
    <theme1 v-if="shop.shop_theme_path === 'pc_1'" :shop="shop"/>
    <theme2 v-if="shop.shop_theme_path === 'pc_2'" :shop="shop"/>
    <theme3 v-if="shop.shop_theme_path === 'pc_3'" :shop="shop"/>
  </div>
</template>

<script>
  const theme1 = () => import('@/pages/shop/-themes/-theme1').then(m => m.default || m)
  const theme2 = () => import('@/pages/shop/-themes/-theme2').then(m => m.default || m)
  const theme3 = () => import('@/pages/shop/-themes/-theme3').then(m => m.default || m)
  import * as API_Common from '@/api/common'
  import * as API_Shop from '@/api/shop'
  export default {
    name: 'shopDetail',
    validate({ params }) {
      return /^\d+$/.test(params.id)
    },
    async asyncData({ params }) {
      const shop = await API_Shop.getShopBaseInfo(params.id)
      return { shop }
    },
    components: { theme1, theme2, theme3 },
    data() {
      return {
        shop_id: this.$route.params.id
      }
    },
    head () {
      return {
        title: `${this.shop.shop_name}-${this.site.site_name}`
      }
    },
    mounted() {
      /** 记录浏览量 */
      API_Common.recordViews(window.location.href)
    },
    methods: {
      GET_ShopData() {
        API_Shop.getShopBaseInfo(this.shop_id).then(response => {
          this.shop = response
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>

</style>
