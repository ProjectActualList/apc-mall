<template>
  <div id="shop-theme-3" class="container">
    <theme3-header :shop="shop"/>
    <en-shop-sildes :shop-id="shop.shop_id"/>
    <theme3-coupons :shop-id="shop.shop_id"/>
    <div class="shop-tags">
      <div class="tags-container">
        <div class="item hot">
          <div class="item-header">
            <h2>店铺热卖</h2>
            <span>HOT-GOODS</span>
            <nuxt-link :to="'./goods-list?shop_id=' + shop_id" class="more">更多&gt;&gt;</nuxt-link>
          </div>
          <ul>
            <template v-for="(goods, index) in hotGoods">
              <li v-if="index < 10" :key="index" class="goods-item">
                <div class="goods-image">
                  <a :href="'/goods/' + goods.goods_id">
                    <img :src="goods.big" :alt="goods.goods_name" :title="goods.goods_name">
                  </a>
                </div>
                <div class="goods-name">
                  <a :href="'/goods/' + goods.goods_id">{{ goods.goods_name }}</a>
                </div>
                <div class="goods-price">
                  <span>RMB：<i class="price">￥{{ goods.price | unitPrice }}</i></span>
                  <span>已售：{{ goods.buy_count }}件</span>
                </div>
              </li>
            </template>
          </ul>
        </div>
        <div class="item new">
          <div class="item-header">
            <h2>店铺新品</h2>
            <span>SHOP-NEW</span>
            <nuxt-link :to="'./goods-list?shop_id=' + shop_id" class="more">更多&gt;&gt;</nuxt-link>
          </div>
          <ul>
            <template v-for="(goods, index) in newGoods">
              <li :key="index" class="goods-item" v-if="index < 10">
                <div class="goods-image">
                  <a :href="'/goods/' + goods.goods_id">
                    <img :alt="goods.goods_name" :src="goods.big" :title="goods.goods_name">
                  </a>
                </div>
                <div class="goods-info">
                  <div class="goods-name">
                    <a :href="'/goods/' + goods.goods_id">{{ goods.goods_name }}</a>
                  </div>
                  <div v-if="index === 0" class="goods-price">
                    <p>RMB: <strong class="price">￥{{ goods.price | unitPrice }}</strong></p>
                    <p>已销售：{{ goods.buy_count }}件</p>
                  </div>
                </div>
              </li>
            </template>
          </ul>
        </div>
        <div class="item rec">
          <div class="item-header">
            <h2>店铺推荐</h2>
            <span>SHOP-RECOMMEND</span>
            <nuxt-link :to="'./goods-list?shop_id=' + shop_id" class="more">更多&gt;&gt;</nuxt-link>
          </div>
          <ul>
            <template v-for="(goods, index) in recGoods">
              <li :key="index" class="goods-item" v-if="index < 8">
                <div class="goods-image">
                  <a :href="'/goods/' + goods.goods_id">
                    <img :src="goods.big" :alt="goods.goods_name" :title="goods.goods_name">
                  </a>
                </div>
                <div class="goods-name">
                  <a :href="'/goods/' + goods.goods_id">{{ goods.goods_name }}</a>
                </div>
                <div class="goods-price">
                  <p>RMB：<strong>￥{{ goods.price | unitPrice }}</strong></p>
                  <p>已销售：{{ goods.buy_count }}件</p>
                </div>
              </li>
            </template>
          </ul>
        </div>
      </div>
    </div>
    <div class="shop-intro w" id="shop-intro">
      <div class="intro-title">
        <h2>关于店铺</h2>
        <h3>Welcome to my shop I wish you a happy shopping</h3>
      </div>
      <div class="intro-body" v-html="shop.shop_desc || '暂无简介'"></div>
    </div>
    <div class="shop-info" id="shop-info">
      <div class="info-title">关于本店</div>
      <div class="info-information">
        <div class="item">
          <h3>{{ shop.shop_name }}</h3>
          <div class="information-same">
            <p>身份认证：身份已认证</p>
            <p>店铺认证：店铺已认证</p>
            <p>创店时间：{{ shop.shop_createtime | unixToDate('yyyy-MM-dd') }}</p>
          </div>
        </div>
        <div class="item">
          <h3>联系方式</h3>
          <div class="information-same">
            <p>所在地址：{{ formatAddress() }}</p>
            <p>详细地址：{{ shop.shop_add }}</p>
            <p>联系电话：{{ shop.link_phone || '无' }}</p>
          </div>
        </div>
        <div class="item">
          <h3>店铺评价</h3>
          <div class="information-same">
            <div>描述相符：<en-shop-star :star="shop.shop_description_credit"/></div>
            <div>服务态度：<en-shop-star :star="shop.shop_service_credit"/></div>
            <div>发货速度：<en-shop-star :star="shop.shop_delivery_credit"/></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import mixin from './themeMixin'
  import theme3Header from './-theme3-header'
  import theme3Coupons from './-theme3-coupons'
  export default {
    name: 'shop-theme-3',
    mixins: [mixin],
    components: { theme3Header, theme3Coupons }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  img {
    width: 100%;
    height: 100%;
  }
  .shop-tags {
    background-color: #fff;
    min-height: 500px;
    .tags-container {
      width: 1210px;
      margin: 20px auto;
    }
    .item {
      margin-top: 10px;
      padding-bottom: 20px;
      .item-header {
        position: relative;
        width: 100%;
        height: 34px;
        color: #333;
        font-size: 22px;
        border-bottom: 1px solid #ffac13;
        h2 {
          display: inline-block;
          font-size: 18px;
        }
        span {
          font-size: 14px;
          color: #ccc;
          margin-left: 10px;
        }
        .more {
          position: absolute;
          right: 0;
          color: #ccc;
          font-size: 12px;
          &:hover { color: #ffac13 }
        }
      }
      ul { clear: both }
      overflow: hidden;
    }
    .item.hot {
      $g_width: (1210px - 80px) / 5;
      .goods-item {
        float: left;
        width: $g_width;
        height: 300px;
        margin-top: 20px;
        margin-right: 20px;
        &:nth-child(5n) { margin-right: 0 }
      }
      .goods-image {
        width: $g_width;
        height: $g_width;
      }
      .goods-name {
        min-height: 36px;
        margin-top: 5px;
        a {
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          overflow: hidden;
        }
      }
      .goods-price {
        display: flex;
        justify-content: space-between;
        margin-top: 5px;
        color: #333;
        .price {
          font-family: georgia;
          color: #f42424;
          font-size: 14px;
        }
      }
    }
    .item.new {
      .item-header { border-bottom: 1px solid #FF5722 }
      ul { margin-top: 10px }
      .goods-item {
        $g_width: ((1210px - 20px) / 2 - 10px) / 3;
        position: relative;
        box-sizing: border-box;
        width: $g_width;
        height: $g_width;
        float: left;
        margin-left: 5px;
        border: 1px solid #FF5722;
        margin-bottom: 5px;
        padding: 5px;
        overflow: hidden;
        &:nth-child(1) {
          width: (1210px - 20px) / 2;
          height: $g_width * 3 + 10px;
          margin-left: 0;
          margin-bottom: 0;
          margin-right: 20px;
          border: none;
          padding: 0;
          .goods-info {
            width: 100%;
            left: 0;
            bottom: 0;
            height: 70px;
          }
          .goods-name {
            min-height: 35px;
            a { font-size: 14px }
          }
        }
        &:nth-child(3n-1) {
          margin-left: 0;
        }
        .goods-info {
          position: absolute;
          left: 5px;
          bottom: 5px;
          width: $g_width - 10px - 2px;
          height: 40px;
          background-color: rgba(0,0,0,.6);
        }
        .goods-image {
          width: 100%;
          height: 100%;
        }
        .goods-name {
          padding: 3px;
          a {
            color: #fff;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            overflow: hidden;
          }
        }
        .goods-price {
          display: flex;
          justify-content: space-between;
          color: #fff;
          padding: 0 5px;
        }
        .price {
          color: #f42424;
        }
      }
    }
    .item.rec {
      .item-header { border-bottom: 1px solid #03a9f4 }
      ul { margin-top: 10px }
      $g_width: (1210px - 30px) / 4;
      .goods-item {
        float: left;
        box-sizing: border-box;
        width: $g_width;
        height: 375px;
        margin-left: 10px;
        margin-bottom: 10px;
        border: 1px solid #ccc;
        padding: 10px;
        &:nth-child(4n+1) { margin-left: 0 }
        &:hover {
          border: 1px solid #03a9f4;
        }
        .goods-image {
          width: $g_width - 20px - 2px;
          height: $g_width - 20px - 2px;
        }
        .goods-name {
          margin-top: 5px;
          min-height: 35px;
          padding-bottom: 5px;
          border-bottom: 1px dashed #ccc;
          a {
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            overflow: hidden;
          }
        }
        .goods-price {
          margin-top: 5px;
          strong { color: #f42424 }
        }
      }
    }
  }
  .shop-intro {
    background-color: #fff;
    text-align: center;
    .intro-title {
      width: 500px;
      margin: 0 auto;
      border-bottom: 1px dashed #ccc;
      padding-bottom: 10px;
      font: 14px/1.5 "Helvetica Neue", Helvetica, Arial, "Microsoft Yahei", "Hiragino Sans GB", "Heiti SC", "WenQuanYi Micro Hei", sans-serif;
      h2 {
        line-height: 30px;
        font-size: 20px;
      }
      h3 {
        font-size: 12px;
        line-height: 30px;
        color: #666
      }
    }
    .intro-body {
      padding: 10px 0;
      min-height: 100px;
      border-bottom: 1px dashed #ccc;
      img {
        object-fit: cover;
      }
    }
  }
  .shop-info {
    width: 750px;
    margin: 0 auto;
    padding: 20px 0;
    .info-title {
      width: 100%;
      font-size: 20px;
      color: #666;
      text-align: center;
      padding: 5px 0 20px;
    }
    .info-information {
      display: flex;
      justify-content: space-between;
      .item {
        width: 200px;
      }
      h3 {
        font: 14px/1.5 "Helvetica Neue", Helvetica, Arial, "Microsoft Yahei", "Hiragino Sans GB", "Heiti SC", "WenQuanYi Micro Hei", sans-serif;
        font-size: 14px;
        padding: 9px 0;
      }
      .information-same {
        border-top: 1px dotted #000;
        clear: both;
        line-height: 22px;
        overflow: hidden;
        padding: 10px 0 0;
      }
    }
  }
</style>
