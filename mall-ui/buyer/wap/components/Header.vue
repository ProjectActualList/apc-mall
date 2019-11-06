<template>
  <div id="header">
    <div class="top-header w">
      <en-logo/>
      <en-search/>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'EnHeader'
  }
</script>

<style type="text/scss" lang="scss" scoped>
  #header {
    .top-header {
      display: flex;
      justify-content: space-between;
      height: 70px;
      padding-top: 30px;
      /deep/ #logo {
        width: 240px;
        height: 60px;
      }
    }
  }
</style>
