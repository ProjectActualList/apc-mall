<template>
  <div class="header-other">
    <div class="inner-header-other" :style="{width: width + 'px'}">
      <div class="left-heander-other">
        <en-logo/>
        <span class="logo-title">{{ title }}</span>
      </div>
      <div class="right-heander-other">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'EnHeaderOther',
    props: {
      title: {
        type: String,
        required: true
      },
      width: {
        default: 1000
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .header-other {
    .inner-header-other {
      position: relative;
      margin: 0 auto;
      padding: 20px 0;
      height: 80px;
      /deep/ #logo {
        float: left;
        width: 245px;
        height: 60px;
      }
      .logo-title {
        font-size: 23px;
        float: left;
        display: block;
        margin: 25px 5px;
      }
      .right-heander-other {
        font-size: 16px;
        float: right;
        color: #999;
      }
    }
  }
</style>
