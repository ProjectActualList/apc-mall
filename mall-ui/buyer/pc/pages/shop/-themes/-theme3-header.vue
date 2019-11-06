<template>
  <div class="shop-header">
    <div class="w">
      <div class="shop-name">{{ shop.shop_name }}</div>
      <div class="shop-header-right">
        <div class="shop-nav">
          <div class="item category">
            <a :href="'/shop/goods-list?shop_id=' + shop_id">
              <h3>全部分组</h3>
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
  </div>
</template>

<script>
  import mixin from './themeHeaderMixin'
  export default {
    name: 'theme-3-header',
    props: ['shop'],
    mixins: [mixin]
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  img {
    width: 100%;
    height: 100%;
  }
  .shop-header {
    width: 100%;
    height: 100px;
    background-color: #fff;
    .w {
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
    .shop-name {
      font-size: 28px;
      color: #000
    }
    .shop-header-right {
      display: flex;
      align-items: center;
    }
    .shop-nav {
      margin-top: 20px;
      .item {
        display: inline-block;
        font-size: 14px;
        color: #666;
        padding: 0 10px;
        &:not(.category):hover { color: #ff5e5e }
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
        left: 10px;
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
    .shop-more {
      display: flex;
      align-items: center;
      margin-top: 20px;
      .item {
        text-align: center;
        cursor: pointer;
        margin-left: 30px;
        color: #333;
        &.active {
          color: $color-main;
          i {
            color: $color-main;
            border-color: $color-main;
          }
        }
        i {
          background-color: #fff;
          font-size: 26px;
          border-radius: 100%;
          color: #666;
          padding: 2px;
          border: 1px solid #ccc;
        }
        p { margin-top: 3px }
      }
    }
  }
</style>
