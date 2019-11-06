<template>
  <van-tabbar v-model="activeIndex" style="box-shadow: 0 0 10px 0 hsla(0,6%,58%,.6);">
    <van-tabbar-item icon="wap-home" to="/">首页</van-tabbar-item>
    <van-tabbar-item icon="wap-nav" to="/category">分类</van-tabbar-item>
    <van-tabbar-item icon="cart" to="/cart" :info="cartBadge ? (cartBadge > 99 ? '99+' : cartBadge) : ''">购物车</van-tabbar-item>
    <van-tabbar-item icon="contact" to="/member">我的</van-tabbar-item>
  </van-tabbar>
</template>

<script>
  import Storage from '@/utils/storage'
  export default {
    name: 'TabBar',
    props: ['active'],
    data() {
      return {
        activeIndex: this.active || 0
      }
    },
    computed: {
      /** 购物车徽章 */
      cartBadge() {
        return this.$store.getters['cart/allCount']
      }
    },
    mounted() {
      if (Storage.getItem('refresh_token')) {
        const shopList  = this.$store.getters['cart/shopList']
        shopList && !shopList.length && this.$store.dispatch('cart/getCartDataAction')
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../assets/styles/color";
  /deep/ {
    .van-tabbar-item--active {
      color: $color-main
    }
  }
</style>
