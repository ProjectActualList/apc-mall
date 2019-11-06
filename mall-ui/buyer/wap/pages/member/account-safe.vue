<template>
  <div id="account-security">
    <nav-bar title="账户安全"/>
    <div class="security-container">
      <van-cell-group>
        <van-cell title="账户密码" value="修改" is-link to="./change-password" />
        <van-cell title="手机号码" value="修改" is-link to="./change-mobile" />
        <van-cell title="第三方登录" value="查看" is-link to="./account-binding" />
      </van-cell-group>
    </div>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  export default {
    name: 'account-safe',
    head() {
      return {
        title: `账户安全-${this.site.site_name}`
      }
    },
    created() {
      if (!this.$store.getters.user.mobile) {
        this.$router.replace({ name: 'member-bind-mobile' })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .security-container {
    padding-top: 46px;
  }
</style>
