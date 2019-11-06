<template>
  <div id="category">
    <nav-bar title="分类"/>
    <div class="category-container">
      <div class="cat-wrapper" ref="catWrapper">
        <ul>
          <li
            v-for="(cat, index) in categorys"
            :class="['item', cat.selected && 'focus']"
            :key="index"
            @click="handleClickCategory(cat, $event)"
          >
            <a href="javascript:;">{{ cat.name }}</a>
          </li>
        </ul>
      </div>
      <div class="con-wrapper" ref="conWrapper">
        <div class="content">
          <div class="inner-content show">
            <div v-if="!currentCat.children || !currentCat.children.length" class="no-data">
              <img src="../assets/images/icon-empty-member.png">
              <p>暂无数据...</p>
            </div>
            <div v-else class="cont">
              <div v-for="cat in currentCat.children" :key="cat.category_id" class="item">
                <div class="title-item">{{ cat.name }}</div>
                <div class="content-item">
                  <a v-for="cc in cat.children" :href="'/goods?category=' + cc.category_id" :key="cc.category_id" class="cat-item">
                    <div class="img-cat-item">
                      <img :src="cc.image" alt=""/>
                    </div>
                    <div class="name-cat-item">{{ cc.name }}</div>
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <tab-bar :active="1"/>
  </div>
</template>

<script>
  import BScroll from 'better-scroll'
  import * as API_Goods from '@/api/goods'

  export default {
    name: 'category',
    async asyncData() {
      let categorys = await API_Goods.getCategory()
      categorys = categorys.map((item, index) => {
        item.selected = index === 0
        return item
      })
      return {
        // 分类数据
        categorys,
        // 当前内容区显示的数据
        currentCat: categorys[0]
      }
    },
    data() {
      return {
        // 加载状态
        loading: true
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.catScroll = new BScroll(this.$refs.catWrapper, { click: true })
        this.conScroll = new BScroll(this.$refs.conWrapper, { click: true })
      })
    },
    methods: {
      /** 点击左侧切换内容 */
      handleClickCategory(cat, $event) {
        const catEle = $event.target.parentElement
        this.$set(this, 'currentCat', cat)
        this.$set(this, 'categorys', this.categorys.map(item => {
          item.selected = item.category_id === cat.category_id
          return item
        }))
        this.$nextTick(() => {
          this.catScroll.refresh()
          this.catScroll.scrollToElement(catEle, 200)
          this.conScroll.refresh()
          this.conScroll.scrollTo(0, 0, 200)
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /deep/ .van-nav-bar .van-nav-bar__left { display: none }
  .category-container {
    position: fixed;
    top: 46px;
    left: 0;
    width: 100%;
    height: calc(100% - 46px - 50px);
  }
  .cat-wrapper {
    position: absolute;
    left: 0;
    z-index: 1;
    width: 80px;
    height: 100%;
    overflow: hidden;
    font-size: 14px;
    background-color: #f8f8f8;
  }
  .cat-wrapper ul {
    position: absolute;
    width: 100%;
    z-index: 1;
  }
  .cat-wrapper .item {
    text-align: center;
    position: relative;
  }
  .cat-wrapper .item::before {
    content: '';
    height: 92px;
    width: 1px;
    position: absolute;
    left: auto;
    top: 0;
    right: 0;
    bottom: auto;
    background-color: #e5e5e5;
    border: 0 solid transparent;
    border-radius: 0;
    -webkit-border-radius: 0;
    transform: scale(0.5);
    -webkit-transform: scale(0.5);
    -moz-transform: scale(0.5);
    -ms-transform: scale(0.5);
    -o-transform: scale(0.5);
    transform-origin: top left;
    -webkit-transform-origin: top left;
    -moz-transform-origin: top left;
    -ms-transform-origin: top left;
    -o-transform-origin: top left;
  }
  .cat-wrapper .item::after {
    content: '';
    height: 1px;
    width: 200%;
    position: absolute;
    left: 0;
    top: auto;
    right: auto;
    bottom: 0;
    background-color: #e5e5e5;
    border: 0 solid transparent;
    border-radius: 0;
    -webkit-border-radius: 0;
    transform: scale(0.5);
    -webkit-transform: scale(0.5);
    -moz-transform: scale(0.5);
    -ms-transform: scale(0.5);
    -o-transform: scale(0.5);
    transform-origin: top left;
    -webkit-transform-origin: top left;
    -moz-transform-origin: top left;
    -ms-transform-origin: top left;
    -o-transform-origin: top left;
  }
  .cat-wrapper .item a {
    display: block;
    width: 100%;
    padding: 15px 0;
    text-decoration: none;
    font-size: 12px;
    color: #232326;
    overflow: hidden;
  }
  .cat-wrapper .item.focus {
    &::before { content: none }
    background-color: #fff;
    a {
      color: #ff002d;
    }
  }
  .con-wrapper {
    position: absolute;
    right: 0;
    z-index: 1;
    width: calc(100% - 80px);
    height: 100%;
    overflow: hidden;
  }
  .con-wrapper .content {
    position: absolute;
    z-index: 1;
    width: 100%;
  }
  .con-wrapper .inner-content {
    opacity: 0;
    padding: 0 10px;
    transition: opacity .2s linear;
    -webkit-transition: opacity .2s linear;
  }
  .con-wrapper .inner-content.show {
    opacity: 1;
  }
  .inner-content .cont .item {
    margin-bottom: 15px;
  }
  .inner-content .title-item {
    line-height: 30px;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .inner-content .content-item {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    align-items: center;
    align-content: flex-start;
    background-color: #ffffff;
    box-shadow: 0 7px 10px #ccc;
  }
  .content-item .cat-item {
    position: relative;
    width: 33.3%;
    height: 90px;
    text-align: center;
  }
  .content-item .cat-item img {
    height: 50px;
    max-width: 80%;
    margin-top: 10px;
  }
  .content-item .name-cat-item {
    text-overflow: ellipsis;
    white-space: nowrap;
    font-size: 10px;
    overflow: hidden;
  }
  .no-data {
    width: 100%;
    margin-top: 100px;
    text-align: center;
    img {
      display: block;
      margin: 0 auto;
    }
  }
</style>
