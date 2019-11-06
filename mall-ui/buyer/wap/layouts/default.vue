<template>
  <div>
    <nuxt id="wrapper"/>
    <en-back-to-top/>
  </div>
</template>
<script>
  import Storage from '@/utils/storage'
  import * as API_Connect from '@/api/connect'
  import jwt_decode from 'jwt-decode'
  export default {
    name: 'defalt',
    async mounted() {
      // 如果是微信浏览器
      if (this.MixinIsWeChatBrowser()) {
        const forward = Storage.getItem('forward')
        if (forward) {
          Storage.removeItem('forward')
          location.href = forward
        }
        // 如果没有授权
        if (!Storage.getItem('is_wechat_auth')) {
          Storage.setItem('forward', location.href)
          location.href = API_Connect.wechatAuthUrl
        }
        // 如果已授权，并且未登录，则去自动登录
        if (Storage.getItem('is_wechat_auth') && !Storage.getItem('refresh_token')) {
          try {
            const res = await API_Connect.weChatAutoLogin(Storage.getItem('uuid_connect'))
            const { uid, access_token, refresh_token } = res
            if (res.uid && access_token && refresh_token) {
              const expires = new Date(jwt_decode(refresh_token).exp * 1000)
              Storage.setItem('uid', uid, { expires })
              this.$store.dispatch('user/setAccessTokenAction', access_token)
              this.$store.dispatch('user/setRefreshTokenAction', refresh_token)
            }
          } catch (e) {
            if (e.response.data.code === 'E133') {
              Storage.removeItem('is_wechat_auth')
              Storage.removeItem('uuid_connect')
              location.href = '/'
            }
          }
        }
      }
      // 如果是首页，并且有uuid，那么替换掉cookie中的uuid，并且移除url中的uuid
      const { name, query } = this.$route
      if (name === 'index' && query.uuid) {
        Storage.setItem('uuid', query.uuid, { expires: 30 })
        location.href = '/'
        return
      }
      if (Storage.getItem('refresh_token')) {
        this.$store.dispatch('user/getUserDataAction')
      }
    }
  }
</script>
<style type="text/scss" lang="scss">
  #wrapper {
    min-height: 100vh
  }
</style>
