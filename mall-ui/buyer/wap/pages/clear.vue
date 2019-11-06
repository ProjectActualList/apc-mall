<template>
  <div id="clear">
    <nav-bar title="清除数据"/>
    <div class="big-btn">
      <van-button type="danger" @click="handleClearAll">清除所有数据</van-button>
    </div>
    <p v-for="(cookie, index) in cookies" :key="index" class="cookie-p">
      <span>{{ cookie.key }}：</span>
      <span>{{ cookie.value }}</span>
    </p>
  </div>
</template>

<script>
  import Storage from '@/utils/storage'
  export default {
    name: 'clear',
    data() {
      return {
        cookies: ''
      }
    },
    mounted() {
      let cookies = document.cookie.split('; ')
      this.cookies = cookies.map(item => {
        const c = item.split('=')
        return { key: c[0], value: c[1] }
      })
    },
    methods: {
      handleClearAll() {
        this.$confirm('确认要清除所有数据吗？', () => {
          Storage.removeItem('user')
          Storage.removeItem('uid')
          Storage.removeItem('uuid')
          Storage.removeItem('uuid_connect')
          Storage.removeItem('access_token')
          Storage.removeItem('refresh_token')
          Storage.removeItem('forward')
          Storage.removeItem('is_wechat_auth')
          this.$alert('清除成功，即将刷新页面！', () => {
            location.href = '/'
          })
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .big-btn {
    text-align: center;
    line-height: 200px;
  }
  .cookie-p {
    padding: 0 10px;
    line-height: 14px;
    border-bottom: 1px solid #ccc;
    margin-top: 5px;
  }
</style>
