<template>
  <nuxt-child></nuxt-child>
</template>

<script>
  export default {
    name: 'after-sale',
    head() {
      return {
        title: `售后服务-${this.site.site_name}`
      }
    }
  }
</script>

