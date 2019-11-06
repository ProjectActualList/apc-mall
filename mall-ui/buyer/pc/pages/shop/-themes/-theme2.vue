<template>
  <div id="shop-theme-2">
    <theme2-header :shop="shop"/>
    <en-shop-sildes :shop-id="shop.shop_id"/>
    <theme2-coupons :shop-id="shop.shop_id"/>
    <div class="shop-tags">
      <div class="tags-container">
        <div class="item hot">
          <ul>
            <li class="tag-title">
              <h2>本季の热卖</h2>
              <span>HOT-GOODS</span>
              <nuxt-link :to="'./goods-list?shop_id=' + shop_id" title="查看更多"></nuxt-link>
            </li>
            <template v-for="(goods, index) in hotGoods">
              <li :key="index" class="goods-item" v-if="index < 7">
                <a :href="'/goods/' + goods.goods_id">
                  <img :src="goods.big" :alt="goods.goods_name" :title="goods.goods_name">
                </a>
                <div class="goods-info">
                  <div class="goods-name"><a :href="'/goods/' + goods.goods_id">{{ goods.goods_name }}</a></div>
                  <div class="goods-price">
                    <span>RMB：</span>
                    <span class="price">{{ goods.price | unitPrice('￥') }}</span>
                    <span class="buy-count">已售：{{ goods.buy_count }}件</span>
                  </div>
                </div>
              </li>
            </template>
          </ul>
        </div>
        <div class="item new">
          <div class="tag-title">
            <h2>店铺の新品</h2>
            <span>SHOP-NEW</span>
            <nuxt-link :to="'./goods-list?shop_id=' + shop_id" title="查看更多"></nuxt-link>
          </div>
          <ul>
            <template v-for="(goods, index) in newGoods">
              <li :key="index" class="goods-item" v-if="index < 8">
                <div class="new-img">
                  <a :href="'/goods/' + goods.goods_id">
                    <img :src="goods.big" :alt="goods.goods_name" :title="goods.goods_name">
                  </a>
                </div>
                <div class="new-intro">
                  <h2>
                    <a :href="'/goods/' + goods.goods_id">{{ goods.goods_name }}</a>
                  </h2>
                  <p>
                    <span>RMB：<i>{{ goods.price | unitPrice('￥') }}</i></span>
                    <span class="hot-sale">已销售：{{ goods.buy_count }}件</span>
                  </p>
                </div>
                <div class="new-background"></div>
              </li>
            </template>
          </ul>
        </div>
        <div class="item rec">
          <div class="tag-title">
            <h2>店铺の推荐</h2>
            <span>SHOP-REC</span>
            <nuxt-link :to="'./goods-list?shop_id=' + shop_id" title="查看更多"></nuxt-link>
          </div>
          <ul>
            <template v-for="(goods, index) in recGoods">
              <li :key="index" class="goods-item" v-if="index < 6">
                <div class="goods-image">
                  <img :src="goods.big" :alt="goods.goods_name" :title="goods.goods_name">
                </div>
                <div class="goods-info">
                  <a :href="'/goods/' + goods.goods_id" class="goods-name">{{ goods.goods_name }}</a>
                  <p v-if="goods.goods_original_price">原价：<span class="goods-original-price">{{ goods.goods_original_price | unitPrice('￥') }}</span></p>
                  <p>价格：<span class="goods-price">{{ goods.price | unitPrice('￥') }}</span></p>
                  <p>已销售：<span>{{ goods.buy_count }}</span>件</p>
                  <a :href="'/goods/' + goods.goods_id" class="goods-btn">查看详情</a>
                </div>
                <i class="icon-tag-rec"></i>
              </li>
            </template>
          </ul>
        </div>
        <div class="item">
          <div class="shop-intro-title">
            <span class="font-shop">店</span>
            <div class="font-other">
              <span class="font-shop-l">铺简介</span>
              <span class="font-shop-en">SHOP-INTRODUCTION</span>
            </div>
          </div>
          <div class="shop-intro-body" v-html="shop.shop_desc || '暂无简介'"></div>
        </div>
      </div>
    </div>
    <div class="shop-intro" id="shop-intro">
      <div class="inner">
        <div class="service-box">
          <h2>联系客服</h2>
          <div class="service-bg"></div>
        </div>
        <div class="infomation-box">
          <div class="item info" id="shop-info">
            <h3>{{ shop.shop_name }}</h3>
            <p>身份认证：身份已认证</p>
            <p>店铺认证：店铺已认证</p>
            <p>创店时间：{{ shop.shop_createtime | unixToDate('yyyy-MM-dd') }}</p>
          </div>
          <div class="item contact">
            <h3>联系方式</h3>
            <p>所在地址：{{ formatAddress() }}</p>
            <p>详细地址：{{ shop.shop_add }}</p>
            <p>联系方式：{{ shop.link_phone || '无' }}</p>
          </div>
          <div class="item evaluation">
            <h3>店铺评价</h3>
            <div>描述相符：<en-shop-star :star="shop.shop_description_credit"/></div>
            <div>服务态度：<en-shop-star :star="shop.shop_service_credit"/></div>
            <div>发货速度：<en-shop-star :star="shop.shop_delivery_credit"/></div>
          </div>
          <div class="item collection" @click="collectionShop"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import mixin from './themeMixin'
  import theme2Header from './-theme2-header'
  import theme2Coupons from './-theme2-coupons'
  export default {
    name: 'shop-theme-2',
    mixins: [mixin],
    components: { theme2Header, theme2Coupons }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .shop-tags {
    padding: 30px 0;
    background-color: #E2DDCA;
  }
  .tags-container {
    $c_width: 1000px;
    $t_width: 639px;
    $t_height: 90px;
    width: $c_width;
    margin: 0 auto;
    background-color: #fff;
    box-shadow: 0 0 10px 2px #ccc;
    overflow: hidden;
    .item {
      position: relative;
      padding: 20px;
      overflow: hidden;
    }
    .tag-title {
      display: flex;
      align-items: center;
      position: relative;
      width: $t_width;
      height: $t_height;
      margin-bottom: 10px;
      background: url(../../../assets/images/background-brown-paper-little.jpg) repeat left top;
      h2 {
        margin-left: 20px;
        font-size: 22px;
        color: #897459;
        font-weight: bold;
        font-family: 微软雅黑;
      }
      span {
        margin-left: 5px;
        color: #fff;
        font-size: 12px;
        font-weight: 200;
      }
      a {
        display: block;
        position: absolute;
        right: 10px;
        top: 50%;
        width: 20px;
        height: 20px;
        background: url(../../../assets/images/icon-shop-more.png);
        background-size: 100%;
      }
    }
    .item.hot {
      $r_width: $c_width - 40px - $t_width - 10px;
      $m_width: ($t_width - 10px) / 2;
      $b_width: ($t_width - 20px) / 3;
      $c_height: $r_width * 2 + 10px;
      height: $c_height;
      ul { position: relative }
      .goods-item {
        position: absolute;
        overflow: hidden;
        img {
          width: 100%;
          height: 100%;
        }
        &:nth-child(2), &:nth-child(3) {
          top: 0;
          right: 0;
          width: $r_width;
          height: $r_width;
        }
        &:nth-child(3) { top: $r_width + 10px }
        &:nth-child(4), &:nth-child(5) {
          width: $m_width;
          height: $m_width;
        }
        &:nth-child(4) { margin-left: 0 }
        &:nth-child(5) { margin-left: $m_width + 10px }
        &:nth-child(6), &:nth-child(7), &:nth-child(8) {
          top: $t_height + 10px + $m_width + 10px;
          width: $b_width;
          height: $b_width;
          .goods-info {
            height: 85px;
            transform: translateY(85px);
          }
          .buy-count {
            position: relative;
            display: block;
            padding: 0 10px;
          }
        }
        &:nth-child(7) { margin-left: $b_width + 10px }
        &:nth-child(8) { margin-left: $b_width * 2 + 20px }
      }
      .goods-item:hover .goods-info {
        transform: translateY(0);
      }
      .goods-info {
        position: absolute;
        left: 0;
        bottom: 0;
        width: 100%;
        height: 70px;
        background-color: rgba(0,0,0,.6);
        transform: translateY(70px);
        transition: all .3s ease;
      }
      .goods-name {
        padding: 3px 10px;
        a {
          font-size: 14px;
          color: #fff;
          font-weight: 200;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          overflow: hidden;
        }
      }
      .goods-price {
        position: relative;
        padding: 3px 10px;
        color: #fff;
        .price {
          font-size: 18px;
          color: #ffff33;
          font-weight: 200;
        }
        .buy-count {
          position: absolute;
          right: 10px;
          font-size: 12px;
        }
      }
    }
    .item.new {
      .tag-title {
        width: 100%;
        height: 90px;
        background: #efebdf;
        h2 { color: #897459 }
        span { color: #60776d }
      }
      .goods-item {
        border: 1px solid #ececec;
        float: left;
        margin-bottom: 10px;
        margin-right: 10px;
        overflow: hidden;
        padding: 5px;
        position: relative;
        width: 220px;
        &:nth-child(4n) { margin-right: 0 }
        img {
          width: 220px;
          height: 220px;
        }
        h2 {
          width: 220px;
          height: 40px;
          line-height: 20px;
          margin-top: 10px;
          border-bottom: 1px dashed #f1f1f1;
          a {
            font-size: 12px;
            color: #868686;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            overflow: hidden;
          }
        }
        p {
          width: 220px;
          height: 30px;
          line-height: 30px;
          overflow: hidden;
          color: #868686;
          i {
            font-size: 16px;
            color: #ff001e;
            font-weight: 200;
          }
        }
        .hot-sale {
          float: right;
          margin-top: 2px;
        }
      }
    }
    .item.rec {
      .tag-title {
        width: 100%;
        height: 90px;
        background: #FFC107;
        h2 { color: #fff }
        span { color: #fff }
      }
      ul { width: 100% }
      .goods-item {
        position: relative;
        $g_width: ($c_width - 40px -10px) / 2;
        box-sizing: border-box;
        float: left;
        width: $g_width;
        height: 210px;
        padding: 10px;
        margin-bottom: 10px;
        background-color: #fff;
        border: 1px solid #ddd;
        overflow: hidden;
        .icon-tag-rec {
          display: block;
          position: absolute;
          left: 0;
          top: -60px;
          width: 60px;
          height: 60px;
          background: url(../../../assets/images/icons-shop-tag.png) no-repeat left -212px;
          transition: all .3s ease;
        }
        &:hover .icon-tag-rec { top: 0 }
        &:nth-child(2n) { margin-left: 10px }
        .goods-image {
          float: left;
          width: 188px;
          height: 188px;
          margin-top: -1px;
          border: 1px solid #ddd;
          img {
            width: 178px;
            height: 178px;
            margin-left: 5px;
            margin-top: 5px;
          }
        }
        .goods-info {
          position: relative;
          float: right;
          width: $g_width - 190px - 10px - 20px - 2px;
          height: 190px;
          margin-left: 10px;
        }
        .goods-name {
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 3;
          overflow: hidden;
          width: 100%;
          color: #333;
          font-size: 14px;
          min-height: 50px;
          margin-bottom: 10px;
          &:hover { color: #f42424 }
        }
        p { margin-top: 8px }
        .goods-original-price { text-decoration: line-through }
        .goods-price { color: #f42424 }
        .goods-btn {
          display: block;
          position: absolute;
          bottom: 5px;
          left: 0;
          width: 150px;
          height: 25px;
          line-height: 25px;
          text-align: center;
          color: #666;
          border: 1px solid #ddd;
          margin-top: 10px;
          transition: all .3s ease;
          &:hover {
            border: 1px solid #f42424;
            color: #f42424;
          }
        }
      }
    }
  }
  .shop-intro-title {
    display: flex;
    justify-content: center;
    width: 100%;
    height: 80px;
    .font-shop {
      display: block;
      width: 60px;
      height: 60px;
      line-height: 60px;
      text-align: center;
      color: #fff;
      background-color: #644139;
      font-size: 28px;
      font-family: "Microsoft YaHei";
      border-radius: 100%;
      margin-right: 5px;
    }
    .font-shop-l {
      display: block;
      height: 40px;
      line-height: 40px;
      font-family: "Microsoft YaHei";
      color: #644139;
      font-size: 24px;
    }
    .font-shop-en {
      display: block;
      height: 20px;
      line-height: 20px;
      font-size: 12px;
      font-weight: 200;
      color: #ccc;
    }
    .font-other { height: 60px }
    .shop-intro-body {
      img {
        object-fit: cover;
      }
    }
  }
  .shop-intro {
    width: 100%;
    min-height: 550px;
    background-color: #dac1ab;
    .inner {
      width: 950px;
      height: 550px;
      margin: 0 auto;
      clear: both;
    }
    .service-box {
      float: left;
      position: relative;
      width: 150px;
      height: 550px;
      margin-right: 120px;
      h2 {
        width: 20px;
        position: absolute;
        right: 0;
        top: 50%;
        margin-top: -100px;
        z-index: 10;
        background: #D3B09C;
        text-align: center;
        font-size: 20px;
        line-height: 25px;
        color: #fff;
        padding: 50px 10px;
        cursor: pointer;
      }
      .service-bg {
        position: absolute;
        top: 0;
        right: 20px;
        width: 2px;
        background: #D3B09C;
        height: 550px;
        display: block;
        overflow: hidden;
      }
    }
    .infomation-box {
      width: 550px;
      height: 420px;
      float: left;
      position: relative;
      margin-top: 65px;
      .item {
        width: 184px;
        height: 89px;
        padding: 60px 45px;
        float: left;
        margin: 0 1px 1px 0;
        background: #c39b82;
        &.contact { margin: 0 0 1px 0 }
        &.evaluation { margin: 0 1px 0 0 }
        &.collection {
          cursor: pointer;
          margin: 0 1px 0 0;
          background: url(../../../assets/images/icon-shop-collection.png) no-repeat center center #c39b82;
        }
        h3 { color: #fff; font-size: 16px }
        p {
          line-height: 20px;
          font-size: 12px;
          color: #fff;
          font-weight: 400;
          margin-top: 5px;
        }
      }
    }
  }
</style>
