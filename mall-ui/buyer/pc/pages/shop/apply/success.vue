<template>
  <div id="success" class="success-container">
    <div class="w inner">
      <img src="../../../assets/images/icon-apply-shop-success.png">
      <span class="tip">申请开店成功，等待审核......</span>
    </div>
    <a href="/" class="back-index">返回首页</a>
  </div>
</template>

<script>
  export default {
    name: "success",
    middleware: 'auth-seller'
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .success-container {
    .inner {
      display: flex;
      align-items: center;
      width: 850px;
      height: 300px;
    }
    .tip {
      color: #c94136;
      font-size: 36px;
      padding-left: 20px;
      font-family: '微软雅黑';
      line-height: 300px;
      margin-top: 50px;
    }
    .back-index {
      display: block;
      width: 120px;
      height: 30px;
      padding: 15px 30px;
      text-align: center;
      line-height: 30px;
      color: #fff;
      font-size: 28px;
      background-color: #c94136;
      border-radius: 5px;
      margin: 50px auto;
      font-family: 微软雅黑;
    }
  }
</style>
