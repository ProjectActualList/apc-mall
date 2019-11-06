<template>
  <div id="app" class="container">
    <nav-bar :title="error.statusCode === 404 ? '页面不见啦' : '服务器出错'"/>
    <div v-if="error.statusCode === 404" class="error-404">
      <div class="error-left">
        <img src="../assets/images/background-404.jpg">
      </div>
      <div class="error-right">
        <h1>糟糕！您访问的页面不见啦...</h1>
        <span class="error-title">可能是因为：</span>
        <p>网址有错误：<span class="error-message">请检查地址是否完整或存在多余字符</span></p>
        <p>网址已失效：<span class="error-message">可能页面已删除，活动已下线等</span></p>
        <span class="error-title" style="margin-top: 20px">您可以：</span>
        <div style="text-align: center">
          <van-button type="danger" size="small" @click="$router.back()">返回上页</van-button>
          <van-button type="danger" size="small" @click="$router.replace({ path: '/' })">去首页</van-button>
        </div>
      </div>
    </div>
    <div v-else class="error-500">
      <div class="inner-error">
        <p>{{ error.message || '服务器出现错误...' }}</p>
      </div>
      <div class="inner-btns">
        <van-button size="small" @click="$router.back()">返回上页</van-button>
        <van-button size="small" @click="handleReload">刷新页面</van-button>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'error',
    props: ['error'],
    methods: {
      /** 刷新网页 */
      handleReload() {
        window.location.reload()
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../assets/styles/color";
  /deep/ {
    .van-button + .van-button {
      margin-left: 20px;
    }
  }
  .container {
    background-color: #fff;
  }
  .error-404 {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-around;
    width: 100%;
    margin: 0 auto;
    .error-left {
      img {
        width: 100%;
        height: 100%;
      }
    }
    .error-right {
      font-size: 14px;
      .error-title {
        display: inline-block;
        margin-bottom: 10px
      }
      .error-message { color: #ff5722 }
      h1 {
        font-size: 26px;
        margin-bottom: 20px;
      }
      p {
        margin-bottom: 5px;
        margin-left: 30px;
      }
      /deep/ .van-button { width: 90px }
    }
  }
  .error-500 {
    text-align: center;
    .inner-error {
      width: 100%;
      height: 350px;
      text-align: center;
      line-height: 275px;
      font-size: 18px;
      font-weight: 600;
      background: url("../assets/images/backbround-500.png") no-repeat center;
      background-size: 100%;
    }
    .inner-btns {
      /deep/ .van-button {
        &:hover, &:focus, &:active {
          border-color: lighten($color-main, 10%);
          color: lighten($color-main, 10%);
          background-color: #fff;
        }
        &:first-child {
          margin-right: 10px;
        }
      }
    }
  }
</style>
