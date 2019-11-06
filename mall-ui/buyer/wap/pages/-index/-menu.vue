<template>
  <div id="index-menu" class="menu-container">
    <ul>
      <li v-for="(menu, index) in data" :key="index">
        <a :href="menu.url" class="menu-item">
          <img :src="menu.image" class="menu-img">
          <p class="menu-title">{{ menu.navigation_name }}</p>
        </a>
      </li>
    </ul>
  </div>
</template>

<script>
  export default {
    name: 'index-menu',
    props: ['data']
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .menu-container {
    position: relative;
    _height: 160px;
    margin-top: -10px;
    margin-bottom: 10px;
    z-index: 99;
    padding: 5px 10px 10px 10px;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
    background-color: #fff;
    overflow: hidden;
    &:after {
      display: block;
      clear: both;
    }
  }
  .menu-item {
    width: 20%;
    height: 50%;
    float: left;
    display: block;
    box-sizing: border-box;
    color: #686868;
    text-align: center;
    padding-top: 10px;
  }
  .menu-img {
    width: 60%;
    height: 60%;
  }
  .menu-title {
    margin-top: 5px;
  }
</style>
