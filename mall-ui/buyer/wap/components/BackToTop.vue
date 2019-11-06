<template>
  <div v-show="show" class="back-to-top" @click="handleToTop">
    <van-icon name="upgrade"/>
  </div>
</template>

<script>
  export default {
    name: 'EnBackToTop',
    data() {
      return {
        show: false
      }
    },
    mounted() {
      window.addEventListener('scroll', this.showToTop)
    },
    methods: {
      showToTop() {
        this.show = window.scrollY > 200
      },
      handleToTop() {
        $('body,html').animate({ scrollTop: 0 }, 300)
      }
    },
    destroyed() {
      window.removeEventListener('scroll', this.showToTop)
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .back-to-top {
    position: fixed;
    z-index: 1000;
    bottom: 100px;
    right: 15px;
    width: 10vw;
    height: 10vw;
    background-color: #ffffff;
    border-radius: 100%;
    text-align: center;
    box-shadow: 0 0 10px #ccc;
    /deep/ .van-icon {
      line-height: 10vw;
      font-size: 5vw;
      font-weight: 600;
      color: #787878;
    }
  }
</style>
