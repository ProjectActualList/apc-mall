<template>
  <div id="shop-list">
    <nav-bar title="店铺列表" fixed/>
    <en-empty v-if="finished && !shopList.length" style="line-height:420px">暂无店铺</en-empty>
    <van-list
      v-model="loading"
      :finished="finished"
      @load="onLoad"
      class="shop-list-container"
    >
      <div v-for="(shop, index) in shopList" :key="index" class="shop-item">
        <div class="shop-hd">
          <div class="shop-logo">
            <img :src="shop.shop_logo">
          </div>
          <div class="shop-info">
            <div class="shop-name">{{ shop.shop_name }}</div>
            <div class="shop-desc">
              <span>关注数：<em>{{ shop.shop_collect }}</em></span>
            </div>
          </div>
          <nuxt-link class="shop-btn" :to="'/shop/' + shop.shop_id">进店</nuxt-link>
        </div>
        <div class="shop-bd">
          <ul class="shop-rec">
            <template v-for="(goods, index) in shop.goods_list">
              <li v-if="index < 3" :key="index" class="shop-rec-item">
                <a :href="'/goods/' + goods.goods_id">
                  <div class="cover">
                    <img :src="goods.small">
                  </div>
                  <p class="item-price">¥{{ goods.price | unitPrice }}</p>
                </a>
              </li>
            </template>
          </ul>
        </div>
        <div class="shop-aside">
          <p class="shop-count">店铺商品：{{ shop.goods_num }}件</p>
        </div>
      </div>
    </van-list>
  </div>
</template>

<script>
  import * as API_Shop from '@/api/shop'
  export default {
    name: 'shopList',
    data() {
      return {
        loading: false,
        finished: false,
        // 店铺列表
        shopList: [],
        // 参数
        params: {
          page_no: 0,
          page_size: 10,
          name: this.$route.query.keyword || ''
        }
      }
    },
    methods: {
      /** 加载数据 */
      onLoad() {
        this.params.page_no += 1
        this.GET_ShopList()
      },
      /** 获取店铺列表 */
      GET_ShopList() {
        this.loading = true
        const params = JSON.parse(JSON.stringify(this.params))
        if (!params.name) delete params.name
        API_Shop.getShopList(params).then(response => {
          this.loading = false
          const { data } = response
          if(!data || !data.length) {
            this.finished = true
          } else {
            this.shopList.push(...data)
          }
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .shop-list-container {
    padding-top: 46px;
  }
  .shop-item {
    padding: 0 10px;
    background: #fff;
    margin-bottom: 10px;
  }
  .shop-hd {
    display: flex;
    align-items: center;
    height: 60px;
    .shop-logo {
      margin-right: 10px;
      width: 90px;
      height: 30px;
      img {
        display: block;
        width: 100%;
        height: 100%;
      }
    }
    .shop-info {
      flex: 1;
      width: 100%;
      overflow: hidden;
    }
    .shop-name {
      color: #333;
      font-size: 14px;
      line-height: 21px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .shop-desc {
      color: #999;
      font-size: 10px;
      line-height: 15px;
      height: 15px;
      overflow: hidden;
      span {
        display: inline-block;
        margin-right: 10px;
      }
    }
    .shop-btn {
      position: relative;
      margin-left: 10px;
      font-size: 14px;
      color: #333;
      text-align: center;
      height: 30px;
      line-height: 30px;
      width: 60px;
      &::before {
        content: "";
        position: absolute;
        z-index: 1;
        pointer-events: none;
        border: 1px solid #ccc;
        top: 0;
        left: 0;
        background: none;
        right: -100%;
        bottom: -100%;
        -webkit-transform: scale(.5);
        -webkit-transform-origin: 0 0;
        border-radius: 8px;
      }
    }
  }
  .shop-bd {
    .shop-rec {
      padding-right: 20px;
      &::after {
        content: "";
        display: block;
        clear: both;
      }
    }
    .shop-rec-item {
      float: left;
      margin: 0 10px 10px 0;
      background-color: #ffffff;
      width: 33.33%;
      box-sizing: border-box;
      &:last-child {
        margin-right: -20px;
      }
      a {
        position: relative;
        display: block;
        padding-top: 100%;
      }
    }
    .cover {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      img {
        display: block;
        width: 111px;
        height: 111px;
        margin: 0 auto;
      }
    }
    .item-price {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      padding: 0 12px;
      height: 20px;
      line-height: 20px;
      text-align: center;
      font-size: 12px;
      color: #fff;
      background-color: rgba(0,0,0,.5);
    }
  }
  .shop-count {
    margin: -2px 0 8px;
    flex: 1;
    color: #999;
    font-size: 12px;
    line-height: 18px;
    text-align: right;
  }
  .shop-aside {
    border-bottom: 1px solid #f1f1f1;
  }
  /deep/ {
    .van-nav-bar--fixed {
      z-index: 99 !important;
    }
  }
</style>
