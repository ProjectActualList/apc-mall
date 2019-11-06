<template>
  <van-nav-bar
    left-arrow
    @click-left="MixinRouterBack"
    v-bind="$attrs"
    v-on="$listeners"
  />
</template>

<script>
  export default {
    name: 'NavBar'
  }
</script>
