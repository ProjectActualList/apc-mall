<template>
  <div class="container">
    <index-search-bar/>
    <index-banner :data="banner"/>
    <index-menu :data="menus"/>
    <index-seckill/>
    <div v-if="floorList" class="floor-container">
      <div v-for="(item, index) in floorList" :key="index" :class="'item-' + item.tpl_id" class="floor-item">
        <component
          :is="templates[item.tpl_id]"
          :data="item"
        ></component>
      </div>
    </div>
    <tab-bar/>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as IndexComponents from '@/pages/-index'
  import * as API_Home from '@/api/home'
  import templates, { templateArray } from './-index/templates'
  export default {
    name: 'index',
    async asyncData() {
      const values = await Promise.all([
        API_Home.getFloorData('WAP'),
        API_Home.getSiteMenu('MOBILE'),  // Andste_TODO 2018/7/9: 这里为什么又是MOBILE
        API_Home.getFocusPictures('WAP')
      ])
      const floor = values[0]
      return {
        floorList: floor.page_data ? global.JSON.parse(floor.page_data) : [],
        menus: values[1],
        banner: values[2]
      }
    },
    components: { ...IndexComponents },
    data() {
      return {
        /** 首页卡片tab x坐标 */
        card_news_tab_x: 0,
        floorList: '',
        templates,
        templateArray,
        menus: [],
        banner: []
      }
    },
    mounted() {
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "./-index/templates/floor-mobile";
  .container {
    background-color: #F9F9F9;
    padding-bottom: 20px;
    margin-bottom: 50px;
  }
  .floor-container {
    width: 100%;
    margin: 0 auto;
  }
  .floor-item:after {
    content: ' ';
    display: block;
    clear: both;
  }
</style>
