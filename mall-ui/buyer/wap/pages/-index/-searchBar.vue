<template>
  <div class="search">
    <div class="search-container">
      <div class="search-bg" :style="{ opacity }"></div>
      <div class="search-main">
        <div class="search-logo">{{ site.site_name }}</div>
        <div class="search-input" @click="showSearch = true">
          <div class="inner-search-input">
            <van-icon name="search" color="#999"/>
            <span>搜索商品/店铺</span>
          </div>
        </div>
        <a href="/member" class="search-icon"><van-icon name="contact" color="#fff"/></a>
      </div>
    </div>
    <search :show="showSearch" @close="showSearch = false"/>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  export default {
    name: 'SearchBar',
    data() {
      return {
        opacity: 0,
        showSearch: false
      }
    },
    computed: {
      ...mapGetters(['site'])
    },
    mounted() {
      window.addEventListener('scroll', () => {
        const sy = window.scrollY
        if (sy >= 100) {
          this.opacity = 0.8
        } else {
          this.opacity = sy / 100
        }
      })
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .search-container {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 100;
    width: 100%;
    height: 50px;
    .search-bg {
      position: absolute;
      z-index: -1;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background-color: rgb(201, 21, 35);
      transition: opacity .2s ease-in;
    }
    .search-main {
      display: flex;
      width: 100%;
      height: 100%;
      > div, .search-icon {
        position: relative;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
  }
  .search-logo {
    width: 20%;
    font-size: 15px;
    color: #fff;
    font-weight: 400;
  }
  .search-input {
    width: 65%;
  }
  .inner-search-input {
    display: flex;
    align-items: center;
    width: 100%;
    height: 30px;
    background-color: #fff;
    border-radius: 15px;
    color: #999;
    /deep/ .van-icon {
      font-size: 14px;
      margin: 0 10px;
    }
  }
  .search-icon {
    width: 15%;
    text-align: center;
    font-size: 22px;
  }
</style>
