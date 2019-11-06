<template>
  <div id="shop-theme-1">
    <theme1-header :shop="shop"/>
    <en-shop-sildes :shop-id="shop.shop_id"/>
    <theme1-coupons :shop-id="shop.shop_id"/>
    <div class="shop-tags">
      <div class="tags-container">
        <div class="item hot">
          <ul>
            <template v-for="(goods, index) in hotGoods">
              <li :key="index" class="goods-item" v-if="index < 5">
                <div class="w">
                  <div class="goods-info">
                    <h3>店家热卖<span>Hot-Recommend</span></h3>
                    <a :href="'/goods/' + goods.goods_id" class="goods-name">{{ goods.goods_name }}</a>
                    <div class="goods-price">
                      <span>RMB：<strong>￥{{ goods.price | unitPrice }}</strong></span>
                      <span>已销售：{{ goods.buy_count }}件</span>
                    </div>
                    <a :href="'/goods/' + goods.goods_id" class="goods-btn">查看详情</a>
                  </div>
                  <div class="goods-image">
                    <a :href="'/goods/' + goods.goods_id">
                      <img :src="goods.big" :alt="goods.goods_name" :title="goods.goods_name">
                    </a>
                  </div>
                </div>
              </li>
            </template>
          </ul>
        </div>
        <div class="item new">
          <ul>
            <template v-for="(goods, index) in newGoods">
              <li :key="index" class="goods-item" v-if="index < 8">
                <div class="goods-image">
                  <a :href="'/goods/' + goods.goods_id">
                    <img :src="goods.big" :alt="goods.goods_name" :title="goods.goods_name">
                  </a>
                </div>
                <div class="goods-info">
                  <a :href="'/goods/' + goods.goods_id" class="goods-name">{{ goods.goods_name }}</a>
                  <div class="goods-price">
                    <span>RMB：<strong>￥{{ goods.price | unitPrice }}</strong></span>
                    <span>已销售：{{ goods.buy_count }}件</span>
                  </div>
                </div>
              </li>
            </template>
            <li class="middle-item">
              <div class="shop-logo">
                <img :src="shop.shop_logo" :alt="shop.shop_name">
              </div>
              <h3>新品</h3>
              <p>追求卓越品质,打造一流品牌</p>
            </li>
          </ul>
        </div>
        <div class="item rec">
          <h3>掌柜推荐</h3>
          <ul>
            <template v-for="(goods, index) in recGoods">
              <li :key="index" class="goods-item" v-if="index < 8">
                <div class="goods-image">
                  <a :href="'/goods/' + goods.goods_id">
                    <img :src="goods.big" :alt="goods.goods_name" :title="goods.goods_name">
                  </a>
                </div>
                <a :href="'/goods/' + goods.goods_id" class="goods-name">{{ goods.goods_name }}</a>
                <div class="goods-price">
                  <span>RMB：<strong>￥{{ goods.price | unitPrice }}</strong></span>
                  <span>已销售：{{ goods.buy_count }}件</span>
                </div>
              </li>
            </template>
          </ul>
        </div>
      </div>
    </div>
    <div class="shop-intro" id="shop-intro">
      <div class="w">
        <div class="intro-title">店铺简介</div>
        <div class="intro-body" v-html="shop.shop_desc || '暂无简介'"></div>
      </div>
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
  import theme1Header from './-theme1-header'
  import theme1Coupons from './-theme1-coupons'
  export default {
    name: 'shop-theme-1',
    mixins: [mixin],
    components: { theme1Header, theme1Coupons }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  img {
    width: 100%;
    height: 100%;
  }
  .shop-tags {
    background-color: #fff;
    overflow: hidden;
    min-height: 300px;
    .item {
      position: relative;
      overflow: hidden
    }
    .item.hot {
      width: 100%;
      li {
        height: 450px;
        background-color: #fff;
        &:nth-child(2n) {
          background-color: #F2F0EC;
          .w { flex-direction: row-reverse }
        }
        .w {
          display: flex;
          width: 980px;
        }
      }
      .goods-info {
        width: 450px;
        height: 450px;
        h3 {
          font: 12px/1.5 tahoma,arial,宋体;
          width: 400px;
          margin: 80px 0 10px 0;
          color: #484848;
          font-size: 28px;
          font-weight: bold;
          height: 60px;
          line-height: 60px;
          span {
            margin-left: 10px;
            color: #ddd;
          }
        }
      }
      .goods-image {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 500px;
        height: 450px;
        img {
          width: 300px;
          height: 300px;
        }
      }
      .goods-name {
        line-height: 30px;
        width: 400px;
        font-size: 16px;
        margin-bottom: 30px;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
      }
      .goods-price {
        display: flex;
        justify-content: space-between;
        width: 400px;
        height: 50px;
        line-height: 50px;
        font-size: 14px;
        strong { color: #f42424 }
      }
      .goods-btn {
        display: block;
        width: 150px;
        height: 28px;
        text-align: center;
        line-height: 28px;
        border: 1px solid #b0b0b0;
        transition: all .3s ease;
        margin-top: 10px;
        &:hover {
          background-color: #757575;
          border-color: #757575;
          color: #fff
        }
      }
    }
    .item.new {
      background-color: #F7F7F7;
      min-height: 300px;
      ul {
        position: relative;
        width: 950px;
        margin: 20px auto;
        border: 1px solid #ccc;
        padding-top: 10px;
        overflow: hidden;
      }
      $g_width: (950px - 40px) / 3;
      .goods-item {
        float: left;
        position: relative;
        width: $g_width;
        height: $g_width;
        margin-left: 10px;
        margin-bottom: 10px;
        overflow: hidden;
        &:nth-child(5) {
          margin-left: $g_width + 20px;
        }
        &:hover {
          .goods-info {
            transform: translateX(0);
          }
        }
      }
      .middle-item {
        position: absolute;
        left: $g_width + 20px;
        top: $g_width + 20px;
        width: $g_width;
        height: $g_width;
        margin: 0;
        text-align: center;
        .shop-logo {
          text-align: center;
          img { width: 100px }
        }
        h3 {
          font: 28px/60px tahoma,arial,宋体;
          height: 80px;
          line-height: 80px;
          text-align: center;
          font-size: 40px;
          font-weight: 200;
        }
        p {
          height: 80px;
          line-height: 80px;
          text-align: center;
        }
      }
      .goods-image {
        width: 100%;
        height: 100%;
      }
      .goods-info {
        position: absolute;
        left: 0;
        bottom: 0;
        width: 100%;
        height: 60px;
        background-color: rgba(0,0,0,.6);
        transition: all .3s ease;
        transform: translateY(60px);
      }
      .goods-name {
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
        padding: 3px 10px;
        color: #fff3f3;
        &:hover { color: #f42424 }
      }
      .goods-price {
        display: flex;
        justify-content: space-between;
        padding: 0 10px;
        color: #aaaaaa;
        strong { color: #f42424 }
      }
    }
    .item.rec {
      background-color: #fff;
      min-height: 300px;
      margin-bottom: 30px;
      h3 {
        font: 28px/60px tahoma,arial,宋体;
        width: 500px;
        height: 100px;
        line-height: 100px;
        text-align: center;
        margin: 0 auto;
        font-size: 30px;
        background: url(../../../assets/images/background-shop-waves.png) no-repeat center -7px;
      }
      ul {
        width: 950px;
        margin: 0 auto;
      }
      $g_width: (950px - 30px) / 4;
      .goods-item {
        float: left;
        width: $g_width;
        height: 300px;
        margin-left: 10px;
        margin-bottom: 10px;
        &:nth-child(4n+1) {
          margin-left: 0;
        }
        .goods-image {
          width: $g_width;
          height: $g_width;
        }
        .goods-name {
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          overflow: hidden;
          margin-top: 5px;
          min-height: 35px;
        }
        .goods-price {
          display: flex;
          justify-content: space-between;
          border-top: 1px dashed #ccc;
          padding-top: 3px;
          margin-top: 5px;
          strong { color: #f42424 }
        }
      }
    }
  }
  .shop-intro {
    .w {
      width: 950px;
      border-bottom: 1px dashed #ccc;
      padding-bottom: 20px;
    }
    .intro-title, .intro-body {
      text-align: center;
    }
    .intro-title {
      font-size: 20px;
      color: #666;
      padding: 5px 0;
    }
    .intro-body {
      margin-top: 20px;
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
