<template>
  <div>
    <div class="shop-header">
      <div class="w">
        <div class="shop-name">{{ shop.shop_name }}</div>
        <div class="shop-more">
          <div class="item" :class="[is_collection && 'active']" @click="collectionShop">
            <i class="iconfont ea-icon-star"></i>
            <p>收藏</p>
          </div>
          <a :href="'http://wpa.qq.com/msgrd?v=3&uin='+ shop.shop_qq +'&site=qq&menu=yes'" target="_blank" class="item">
            <i class="iconfont ea-icon-service"></i>
            <p>客服</p>
          </a>
          <nuxt-link :to="'./goods-list?shop_id=' + shop.shop_id" class="item">
            <i class="iconfont ea-icon-more"></i>
            <p>更多</p>
          </nuxt-link>
        </div>
      </div>
    </div>
    <div class="shop-nav">
      <div class="w">
        <div class="item category">
          <a :href="'/shop/goods-list?shop_id=' + shop_id">
            <h1>全部分组</h1>
          </a>
          <div class="category-box">
            <div class="inner-category-box">
              <div v-for="cate in categorys" :key="cate.shop_cat_id" class="category-item">
                <a :href="'/shop/goods-list?shop_id=' + shop_id + '&shop_cat_id=' + cate.shop_cat_id">{{ cate.shop_cat_name }}</a>
                <div class="category-item-box">
                  <template v-for="cc in cate.children">
                    <div
                      v-if="cate.children && cate.children.length"
                      :key="cc.shop_cat_id"
                      class="category-item-cc"
                    >
                      <a :href="'/shop/goods-list?shop_id=' + shop_id + '&shop_cat_id=' + cc.shop_cat_id">{{ cc.shop_cat_name }}</a>
                    </div>
                  </template>
                </div>
              </div>
            </div>
          </div>
        </div>
        <nuxt-link :to="'/shop/' + shop_id" class="item">店铺首页</nuxt-link>
        <span>|</span>
        <nuxt-link :to="'/shop/' + shop_id + '#shop-intro'" class="item">店铺简介</nuxt-link>
        <span>|</span>
        <nuxt-link :to="'/shop/' + shop_id + '#shop-info'" class="item">店铺评价</nuxt-link>
        <template v-for="nav in navs">
          <span :key="'span-' + nav.id">|</span>
          <a :href="nav.nav_url" class="item" :key="nav.id" :target="nav.target ? '_blank' : '_self'">{{ nav.name }}</a>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
  import mixin from './themeHeaderMixin'
  export default {
    name: 'theme-2-header',
    props: ['shop'],
    mixins: [mixin]
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  .shop-header {
    width: 100%;
    height: 120px;
    background: url("../../../assets/images/background-brown-paper.jpg") repeat-x left top;
    .w {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
    .shop-name {
      padding: 0 0 0 320px;
      height: 120px;
      line-height: 125px;
      font-size: 30px;
      color: #fff;
      background: url("../../../assets/images/icon-welcome.png") no-repeat left center;
    }
    .shop-more {
      display: flex;
      margin-top: 28px;
      .item {
        text-align: center;
        cursor: pointer;
        margin-left: 30px;
        color: #333;
        &.active {
          color: $color-main;
          i { color: $color-main }
        }
        i {
          background-color: #fff;
          font-size: 26px;
          border-radius: 100%;
          color: #666;
          padding: 3px;
        }
        p { margin-top: 3px }
      }
    }
  }
  .shop-nav {
    display: flex;
    align-items: center;
    width: 100%;
    height: 30px;
    background-color: #333;
    color: #fff;
    h1 {
      font-size: 14px;
      color: #fff;
    }
    span { color: #dddbdb }
    .item {
      font-size: 12px;
      font-weight: 600;
      padding: 0 15px;
      color: #dddbdb
    }
    .item.category {
      position: relative;
      display: inline-block;
      cursor: pointer;
      &:hover {
        .category-box {
          display: block;
        }
      }
    }
    .category-box {
      display: none;
      position: absolute;
      z-index: 99;
      padding-top: 6px;
      left: 15px;
    }
    .inner-category-box {
      width: 152px;
      background-color: rgba(255,255,255,.8);
      box-sizing: border-box;
      .category-item {
        position: relative;
        line-height: 30px;
        height: 30px;
        border-bottom: 1px dashed #333;
        cursor: pointer;
        padding: 0 10px;
        &:last-child { border-bottom: none }
        &:hover {
          background-color: #f2f2f2;
          .category-item-box {
            display: block;
          }
        }
        a {
          display: block;
          color: #333
        }
      }
      .category-item-box {
        display: none;
        position: absolute;
        top: 0;
        left: 100%;
        width: 130px;
        background-color: rgba(96,96,96,.8);
        .category-item-cc {
          border-bottom: 1px dashed #fff;
          padding: 0 10px;
          &:last-child { border-bottom: none }
          &:hover {
            background-color: rgba(96,96,96,1);
          }
          a {
            display: block;
            color: #fff;
          }
        }
      }
    }
  }
</style>
