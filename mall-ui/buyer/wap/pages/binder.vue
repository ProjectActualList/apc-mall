<template>
  <div id="binder" class="binder-container w"></div>
</template>

<script>
  import Storage from '@/utils/storage'
  import * as API_Connect from '@/api/connect'
  export default {
    name: 'binder',
    mounted() {
      const { uuid } = this.$route.query
      if (uuid) Storage.setItem('uuid', uuid, { expires: 30 })
      // 如果有刷新token，说明是在已登录的情况下绑定或换绑
      if (Storage.getItem('refresh_token')) {
        const uuid_connect = Storage.getItem('uuid_connect')
        API_Connect.loginBindConnect(uuid_connect).then(response => {
          const { access_token, refresh_token } = response
          this.$store.dispatch('user/setAccessTokenAction', access_token)
          this.$store.dispatch('user/setRefreshTokenAction', refresh_token)
          Storage.setItem('uuid', uuid_connect, { expires: 30 })
          this.$router.replace({ name: 'member-account-binding' })
        }).catch(() => {
          this.$router.replace({ name: 'member-account-binding' })
        })
        return false
      }
      this.$dialog.alert({
        title: '授权成功',
        message: '当前已授权成功，请您：',
        showCancelButton: true,
        confirmButtonText: '注册',
        cancelButtonText: '登录',
        beforeClose: (action, done) => {
          if (action === 'confirm') {
            done()
            this.$router.push({ name: 'register', query: { form: 'connect' } })
          } else {
            done()
            this.$router.push({ name: 'login', query: { form: 'connect' } })
          }
        }
      })
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../assets/styles/color";
  .binder-container {
    min-height: 200px;
  }
</style>
