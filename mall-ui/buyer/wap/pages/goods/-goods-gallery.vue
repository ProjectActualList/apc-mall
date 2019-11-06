<template>
  <van-swipe :autoplay="3000" class="goods-swipe">
    <van-swipe-item v-for="(gallery, index) in data" :key="index">
      <img :src="gallery.original" @click="handlePreviewImage(index)">
    </van-swipe-item>
  </van-swipe>
</template>

<script>
  import { ImagePreview } from 'vant'
  export default {
    name: 'goods-gallery',
    props: ['data'],
    methods: {
      /** 商品图预览 */
      handlePreviewImage(index) {
        ImagePreview(this.data.map(item => item.original), index)
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .goods-swipe {
    width: 100%;
    height: 375px;
    img {
      width: 100%;
      height: 100%;
    }
  }
</style>
