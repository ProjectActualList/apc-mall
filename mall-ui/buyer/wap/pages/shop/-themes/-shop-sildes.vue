<template>
  <no-ssr>
    <swiper :options="swiperOptions">
      <swiper-slide v-for="(silde, index) in sildes" :key="index">
        <a :href="silde.silde_url">
          <img :src="silde.img">
        </a>
      </swiper-slide>
    </swiper>
  </no-ssr>
</template>

<script>
  import Vue from 'vue'
  import * as API_Shop from '@/api/shop'
  export default {
    name: 'shop-sildes',
    props: ['shop-id'],
    data() {
      return {
        swiperOptions: {
          autoplay: true,
          loop: true
        },
        sildes: []
      }
    },
    mounted() {
      API_Shop.getShopSildes(this.shopId).then(response => { this.sildes = response })
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .swiper-container {
    width: 100%;
    height: 160px;
    img {
      width: 100%;
      height: 100%;
    }
  }
</style>
