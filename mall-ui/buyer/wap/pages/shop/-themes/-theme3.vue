<template>
  <div id="shop-theme-3" class="container">
    <en-shop-header :shop="shop"/>
    <en-shop-nav :shop="shop"/>
    <en-shop-sildes :shop-id="shop.shop_id"/>
    <theme3-coupons :shop-id="shop.shop_id"/>
    <div class="goods-anchor">
      <div class="hot-anchor" @click="handleToTag('hot-goods')">
        <p>热卖</p>
        <span>Hot-Goods</span>
      </div>
      <div class="rec-anchor" @click="handleToTag('rec-goods')">
        <p>推荐</p>
        <span>Recommend</span>
      </div>
      <div class="new-anchor" @click="handleToTag('new-goods')">
        <p>新品</p>
        <span>New Arrival</span>
      </div>
    </div>
    <div class="shop-tags">
      <!--热卖商品-->
      <div class="hot-goods">
        <ul class="list-hot-goods">
          <li v-for="(goods, index) in recGoods" :key="index" class="item-hot-goods">
            <div class="img-hot-goods">
              <a :href="'/goods/' + goods.goods_id">
                <img :src="goods.big" :alt="goods.goods_name">
              </a>
            </div>
            <div class="intro-hot-goods">
              <p><a :href="'/goods/' + goods.goods_id">{{ goods.goods_name }}</a></p>
              <span>RMB：<b>￥{{ goods.price | unitPrice }}</b></span>
              <i>已销售：{{ goods.buy_count }}件</i>
            </div>
          </li>
        </ul>
      </div>
      <!--推荐商品-->
      <div class="rec-goods">
        <ul class="list-rec-goods">
          <li v-for="(goods, index) in newGoods" :key="index" class="item-rec-goods">
            <div class="img-rec-goods">
              <a :href="'/goods/' + goods.goods_id">
                <img :src="goods.big" :alt="goods.goods_name">
              </a>
            </div>
            <div class="intro-rec-goods">
              <i></i>
              <div class="detail-rec-goods">
                <h3><a :href="'/goods/' + goods.goods_id">{{ goods.goods_name }}</a></h3>
                <p>￥{{ goods.price | unitPrice }}<span> (销量：{{ goods.buy_count }})</span></p>
              </div>
            </div>
          </li>
        </ul>
      </div>
      <!--新品上架-->
      <div class="new-goods">
        <ul class="list-new-goods">
          <li v-for="(goods, index) in hotGoods" :key="index" class="item-new-goods">
            <div class="img-new-goods">
              <a :href="'/goods/' + goods.goods_id">
                <img :src="goods.big" :alt="goods.goods_name">
              </a>
            </div>
            <div class="intro-new-goods">
              <h3><a :href="'/goods/' + goods.goods_id">{{ goods.goods_name }}</a></h3>
              <p>RMB：<i>￥{{ goods.price }}</i></p>
            </div>
          </li>
        </ul>
      </div>
      <nuxt-link :to="'./goods-list?shop_id=' + shop_id" class="more-goods">更多本店商品 >></nuxt-link>
    </div>
    <div class="shop-detail">
      <h3>关于店铺</h3>
      <h6 v-html="shop.shop_desc || '暂无简介'"></h6>
    </div>
  </div>
</template>

<script>
  import mixin from './themeMixin'
  import theme3Coupons from './-theme3-coupons'
  export default {
    name: 'shop-theme-3',
    mixins: [mixin],
    components: { theme3Coupons },
    methods: {
      // 滚动到指定标签高度
      handleToTag(tag) {
        this.MixinScrollToTop($('.' + tag).offset().top)
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .goods-anchor {
    display: flex;
    height: 8 * 16px;
    background: #fff;
    padding: 16px;
    overflow: hidden;
    margin: 8px 0 0 0;
    text-align: center;
    div { flex: 1 }
    p {
      color: #fff;
      margin: 2.5 * 16px 0 0 0;
      font-size: 1.2 * 16px;
      font-weight: 400;
    }
    span {
      color: #fff;
      font-family: PingFang SC,Verdana,Helvetica Neue,Microsoft Yahei,Hiragino Sans GB,Microsoft Sans Serif,WenQuanYi Micro Hei,sans-serif;
      font-size: 0.9 * 16px;
      font-weight: 400;
    }
    .hot-anchor { background-color: #3e4b6d }
    .rec-anchor {
      margin: 0 16px;
      background-color: #e3c23f
    }
    .new-anchor { background-color: #2f54b7 }
  }
  .hot-goods {
    width: 100%;
    background: #fff;
    margin: 8px 0 0 0;
    padding: 8px 0;
    ul {
      width: 100%;
      overflow: hidden;
    }
    .item-hot-goods {
      display: flex;
      flex-direction: row-reverse;
      width: 100%;
      overflow: hidden;
      margin: 8px 0 16px 0;
      padding: 8px 0;
      &:nth-child(2n) {
        flex-direction: row;
      }
    }
    .img-hot-goods {
      width: 60%;
      text-align: center;
      img {
        border-radius: 20rem;
        width: 80%;
        box-shadow: 0 1px 16px #dedede;
        padding: 0.3 * 16px;
      }
    }
    .intro-hot-goods {
      width: 33%;
      margin: 4 * 16px 0 0 5%;
      p {
        margin: 0 0 16px;
        font-size: 0.9 * 16px;
        font-weight: 200;
        width: 100%;
        a {
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 5;
          overflow: hidden;
          color: #53a0be
        }
      }
      span {
        font-size: 0.7 * 16px;
        font-weight: 200;
        display: block;
        width: 100%;
      }
      b {
        display: inline;
        color: #3e4b6d;
        font-size: 1.1 * 16px;
      }
      i {
        font-size: 0.7 * 16px;
        margin: 0.3 * 16px 0 0 0;
        color: #bbb;
        font-weight: 200;
        font-style: normal;
      }
    }
  }
  .rec-goods {
    width: 100%;
    background: #fff;
    border-top: 10px solid #e5e5e5;
    padding: 8px 0;
    ul {
      width: 100%;
      overflow: hidden;
    }
    .item-rec-goods {
      width: 100%;
      overflow: hidden;
      margin: 8px 0 16px 0;
      padding: 8px 0;
      position: relative;
    }
    .img-rec-goods {
      overflow: hidden;
      text-align: center;
      margin: 16px;
      border-bottom: 1px dashed #dedede;
      img {
        width: 100%;
        height: 100%;
      }
    }
    .intro-rec-goods {
      position: absolute;
      width: 80%;
      height: 5 * 16px;
      left: 10%;
      bottom: 2 * 16px;
      z-index: 10;
      background-color: rgba(229, 229, 229, .8);
      i {
        float: left;
        width: 4 * 16px;
        height: 4 * 16px;
        margin: 8px;
        display: block;
        background: url(data:image/png;base64,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) no-repeat center center #404a6d;
        background-size: 70%;
      }
      .detail-rec-goods {
        float: left;
        width: 70%;
        h3 {
          color: #040404;
          font-size: 16px;
          margin: 16px 0 0.3 * 16px 0;
        }
        a {
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          overflow: hidden;
        }
        p {
          font-size: 16px;
          color: #ff0048;
          font-weight: 200;
        }
        span {
          font-size: 0.7 * 16px;
          color: #979797;
        }
      }
    }
  }
  .new-goods {
    border-top: 10px solid #e5e5e5;
    padding: 8px 2%;
    position: relative;
    overflow: hidden;
    background: #EFEFEF;
    ul {
      width: 106%;
      overflow: hidden;
    }
    .item-new-goods {
      float: left;
      background: #fff;
      width: 46%;
      margin: 0 2% 8px 0;
      overflow: hidden;
      position: relative;
      padding: 0.6 * 16px 0;
      box-shadow: 0 1px 16px #dedede;
    }
    .img-new-goods {
      img {
        display: block;
        margin: 0 auto;
        width: 157px;
        height: 157px;
      }
    }
    .intro-new-goods {
      h3 {
        font-size: 16px;
        font-weight: 200;
        margin: 0 0 0.3 * 16px 0;
        display: block;
        text-align: left;
        width: 90%;
        padding: 0 5%;
        a {
          min-height: 3 * 16px;
          color: #53a0be;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 3;
          overflow: hidden;
        }
      }
      p {
        font-size: 0.8 * 16px;
        font-weight: 200;
        color: #3e4b6d;
        width: 90%;
        padding: 0 5%;
      }
    }
  }
  .shop-detail {
    text-align: center;
    font-weight: 200;
    width: 90%;
    overflow: hidden;
    margin: 0 0 3 * 16px 0;
    background: #fff;
    padding: 8px 5%;
    border-top: 10px solid #e5e5e5;
    h3 {
      font-size: 16px;
      line-height: 32px;
      font-weight: 200;
      color: #000;
    }
    h6 {
      font-size: 0.7 * 16px;
      line-height: 32px;
      font-weight: 200;
      color: #ccc;
    }
    img {
      object-fit: cover;
    }
  }
</style>
