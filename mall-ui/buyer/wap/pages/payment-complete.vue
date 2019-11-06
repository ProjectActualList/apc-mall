<template>
  <div id="payment-complete" class="w" style="text-align: center">
    <van-nav-bar title="支付完成"/>
    <h1 style="line-height: 100px"><i class="el-icon-success"></i>支付完成</h1>
    <div class="cp-btns">
      <nuxt-link to="/member/my-order" replace>
        <van-button type="danger" size="small">查看订单</van-button>
      </nuxt-link>
      <nuxt-link to="/" replace>
        <van-button type="danger" size="small">返回首页</van-button>
      </nuxt-link>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'payment-complete'
  }
</script>

<style type="text/scss" lang="scss" scoped>
  h1 {
    color: #67C23A;
    .el-icon-success {
      margin-right: 10px;
    }
  }
  .cp-btns {
    margin: 20px auto;
    text-align: center;
    a + a {
      margin-left: 10px;
    }
  }
</style>
