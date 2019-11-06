<template>
  <div id="shop-header" class="shop-header">
    <van-nav-bar
      :title="shop.shop_name"
      fixed
      left-arrow
      @click-left="MixinRouterBack"
      @click-right="show_cate = true"
    >
      <i class="nav-cate-icon" slot="right"></i>
    </van-nav-bar>
    <div class="cate-side" :class="[show_cate && 'show']">
      <i class="iconfont ea-icon-close" @click="show_cate = false"></i>
      <ul>
        <li v-for="(cate, index) in categories" :key="index" :class="[cate.lv === 2 && 'lv-2']">
          <nuxt-link :to="'/shop/goods-list?shop_id='+ shop.shop_id + (cate.shop_cat_id ? ('&shop_cat_id='+ cate.shop_cat_id) : '')" replace>{{ cate.shop_cat_name }}</nuxt-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
  import * as API_Shop from '@/api/shop'
  export default {
    name: 'shop-header',
    props: ['shop'],
    data() {
      return {
        // 店铺分组
        categories: '',
        // 展示分组
        show_cate: false
      }
    },
    mounted() {
      /** 获取店铺分类【分组】 */
      API_Shop.getShopCategorys(this.shop.shop_id).then(response => {
        const categories = [{ shop_cat_name: '全部分组', shop_cat_id: 0 }]
        response.forEach(item => {
          categories.push(item)
          if (item.children && item.children.length) {
            item.children.forEach(_item => {
              _item.lv = 2
              categories.push(_item)
            })
          }
        })
        this.categories = categories
      })
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /deep/ .van-nav-bar {
    z-index: 99 !important;
  }
  /deep/ .nav-cate-icon {
    width: 20px;
    height: 20px;
    display: inline-block;
    vertical-align: middle;
    background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAyElEQVQ4T2NkoBAwUqifAWxASkqKAxMT09tZs2ZdzsjIUPj79688ssHMzMwPZ8yY8QCbZWAD0tLS/jMwMGydNWuWT1paWgMDA0M9suL///9/mD17tiA+A0Cabs2aNWsZ1DUOaAY8mD179gKcBlASDtQJREJhgObCDbNmzQqEiYFdkJqaup+RkfHErFmzKlNTUxMYGBhAGCtgZGQ8MGvWLFCYgQF1vEBxIELjHjka7Ykw9OKsWbM2EJWQcBgGMsBgkIQBEf7FqQQA92ZPEc3yf9IAAAAASUVORK5CYII=") no-repeat;
    background-size: 80%;
  }
  .shop-header {
    margin-bottom: 46px;
  }
  .cate-side {
    width: 60vw;
    height: 100vh;
    position: fixed;
    top: 0;
    right: -100vw;
    background: #fff;
    box-shadow: -2px 1px 16px #dedede;
    z-index: 999;
    transition: right ease .3s;
    &.show {
      right: 0;
    }
    .ea-icon-close {
      position: absolute;
      display: block;
      top: 0;
      left: -45px;
      background-color: #fff;
      width: 45px;
      height: 45px;
      text-align: center;
      line-height: 45px;
      box-shadow: -2px 1px 6px #dedede;
    }
    ul {
      margin: 16px 0;
    }
    li {
      width: 85%;
      margin: 0 0 0 5%;
      padding-left: 5%;
      line-height: 28px;
      font-size: 14px;
      font-weigth: 200;
      color: #000;
      &.lv-2 {
        padding-left: 20px;
        width:80%;
      }
    }
    a {
      display: block;
      width: 95%;
      border-bottom: 1px dashed #dedede;
      padding: 0 0 0 5%;
      background-size: 1rem;
    }
  }
</style>
