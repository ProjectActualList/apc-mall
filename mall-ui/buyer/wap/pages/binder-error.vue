<template>
  <div>
    <nav-bar title="绑定出错"/>
    <div id="binder-error" class="binder-error w">
      <h1>{{ message }}</h1>
      <div class="btns">
        <van-button type="danger" size="small" @click="$router.back()">返回上页</van-button>
        <van-button type="danger" size="small" @click="$router.push({ path: '/' })">去首页</van-button>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'binder-error',
    data() {
      return {
        message: this.$route.query.message
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .binder-error {
    h1 {
      font-size: 20px;
      line-height: 200px;
      text-align: center;
    }
    .btns {
      text-align: center;
      button {
        width: 80px;
        margin-right: 10px;
      }
    }
  }
</style>
