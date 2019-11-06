<template>
  <div id="index-banner" class="banner-container">
    <no-ssr>
      <swiper :options="swiperOption">
        <swiper-slide v-for="(focus, index) in data" :key="index">
          <en-operation :opt="{type: focus.operation_type, value: focus.operation_param}">
            <img :src="focus.pic_url">
          </en-operation>
        </swiper-slide>
        <div class="swiper-pagination swiper-pagination-white" slot="pagination"></div>
      </swiper>
    </no-ssr>
  </div>
</template>

<script>
  import * as API_Home from '@/api/home'
  import EnOperation from "@/components/Operation";
  export default {
    name: 'index-banner',
    props: ['data'],
    components: {EnOperation},
    data() {
      return {
        swiperOption: {
          autoplay: true,
          loop: true,
          // effect: 'fade',
          pagination: {
            el: '.swiper-pagination',
            dynamicBullets: true,
            clickable: true
          }
        }
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .banner-container {
    position: relative;
    height: 183px;
  }
  .swiper-container {
    height: 100%;
  }
  /deep/ .swiper-pagination-index {
    position: relative;
    .custom-pagination-inner {
      display: block;
      position: absolute;
      z-index: 1;
      left: 50%;
      bottom: 20px;
      font-size: 0;
      padding: 4px 8px;
      border-radius: 12px;
      background-color: hsla(0, 0%, 100%, 0.4);
      .custom-pagination-btn {
        display: inline-block;
        margin-right: 10px;
        width: 12px;
        height: 12px;
        border-radius: 100%;
        background-color: #fff;
        cursor: pointer;
      }
      .custom-pagination-btn.__active__ {
        background-color: #f42424;
      }
      .custom-pagination-btn.__last__ {
        margin-right: 0;
      }
    }
  }
  #index-banner .swiper-slide {
    height: 180px;
    img {
      width: 100%;
      height: 100%;
    }
  }
</style>
