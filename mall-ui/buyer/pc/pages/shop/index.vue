<template>
  <div id="shop-list">
    <en-header></en-header>
    <div class="shop-list-container w">
      <div class="sort-bar">
        <a href="javascript:;" :class="['sort-item', !params.order && 'selected']" @click="handleSortShopList(0)">默认</a>
        <a href="javascript:;" :class="['sort-item', params.order && 'selected']" @click="handleSortShopList(1)">信用</a>
      </div>
      <div v-if="!shopList || !shopList.data.length" style="text-align:center;line-height:200px">暂无数据</div>
      <ul v-else class="shop-list">
        <li v-for="shop in shopList.data" :key="shop.shop_id" class="shop-item">
          <div class="shop-info">
            <div class="shop-member-face">
              <en-shop-logo :url="shop.shop_logo" class="member-face" />
            </div>
            <div class="shop-member-name">
              <nuxt-link :to="'/shop/' + shop.shop_id" class="shop-name">{{ shop.shop_name }}</nuxt-link>
              <p>店主：{{ shop.member_name }}</p>
            </div>
            <div class="shop-other-info">
              <div class="goods-num">商品数量{{ shop.goods_num }}件</div>
              <div class="shop-add-score">
                <div class="shop-address" :title="formatAddress(shop)">{{ formatAddress(shop) }}</div>
                <div class="shop-score">
                  <span class="shop-score-tit">店铺动态评分：</span>
                  <span class="shop-score-arrow-span"><i class="shop-score-arrow"></i></span>
                  <div class="shop-score-box">
                    <div>描述相符：<en-shop-star :star="shop.shop_description_credit"/></div>
                    <div>服务态度：<en-shop-star :star="shop.shop_service_credit"/></div>
                    <div>发货速度：<en-shop-star :star="shop.shop_delivery_credit"/></div>
                  </div>
                </div>
              </div>
              <div class="expanded-opt">
                <a
                  href="javascript:;"
                  class="expanded-opt-a"
                  @click="shop.goods_on = !shop.goods_on"
                >
                  {{ shop.goods_on ? '收起' : '展开' }}店铺商品
                  <i class="iconfont ea-icon-right-d-arrow" :class="[shop.goods_on && 'on']"></i>
                </a>
              </div>
            </div>
          </div>
          <div :class="['shop-goods', shop.goods_on && 'on']">
            <template v-if="!shop.goods_list || !shop.goods_list.length">
              <p class="no-goods">哎呀！店铺还没商品呢，去<nuxt-link :to="'/shop/' + shop.shop_id">店铺首页</nuxt-link>看看试试吧！</p>
            </template>
            <ul v-else>
              <template v-for="(goods, index) in shop.goods_list">
                <li v-if="index < 5" :key="goods.goods_id" class="goods-item">
                  <a :href="'/goods/' + goods.goods_id" target="_blank">
                    <img :src="goods.thumbnail" class="goods-image">
                    <p class="goods-price price">￥{{ goods.price | unitPrice }}</p>
                    <p class="goods-name">{{ goods.goods_name }}</p>
                  </a>
                </li>
              </template>
            </ul>
          </div>
        </li>
      </ul>
      <el-pagination
        v-if="shopList"
        @current-change="handleCurrentPageChange"
        :current-page.sync="shopList.page_no"
        :page-size="shopList.page_size"
        layout="total, prev, pager, next"
        :total="shopList.data_total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import { Pagination } from 'element-ui'
  Vue.use(Pagination)
  import * as API_Shop from '@/api/shop'
  import ShopStar from '@/pages/shop/-themes/-shop-star'
  import * as qs from 'qs'
  export default {
    name: 'shopList',
    components: {
      'en-shop-star': ShopStar
    },
    async asyncData({ query }) {
      if (query.keyword) query.name = query.keyword
      const shopList = await API_Shop.getShopList(query)
      if (shopList.data) {
        shopList.data = shopList.data.map(item => {
          item.goods_on = !!(item.goods_list && item.goods_list.length)
          return item
        })
      }
      return { shopList }
    },
    data() {
      return {
        // 店铺列表
        shopList: '',
        // 参数
        params: this.$route.query
      }
    },
    methods: {
      /** 当前分页发生改变 */
      handleCurrentPageChange(page_no) {
        this.params.page_no = page_no
        location.href = `/shop?${qs.stringify(this.params)}`
      },
      /** 店铺列表排序 */
      handleSortShopList(order) {
        if (order === 0) {
          delete this.params.order
        } else {
          this.params.order = order
        }
        location.href = `/shop?${qs.stringify(this.params)}`
      },
      /** 格式化地址信息 */
      formatAddress(shop) {
        return `${shop.shop_province} ${shop.shop_city} ${shop.shop_county} ${shop.shop_town}`
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .shop-list-container {
    .sort-bar {
      width: 100%;
      border-bottom: solid 1px #E7E7E7;
      display: inline-block;
      height: 37px;
      margin-top: 10px;
      text-align: right;
    }
    .sort-item {
      display: inline-block;
      height: 37px;
      line-height: 37px;
      text-align: center;
      padding: 0 15px;
      margin-left: 5px;
      border: 1px solid #E7E7E7;
      border-bottom: none;
      &.selected {
        background-color: #E7E7E7;
        border-color: #d8d8d8
      }
    }
    .shop-item {
      border-bottom: solid 1px #E7E7E7;
      padding: 20px 0;
      vertical-align: top;
      &:last-child { border-bottom: none }
    }
    .shop-info {
      height: 70px;
      margin-bottom: 10px;
    }
    .shop-member-face {
      float: left;
      .member-face {
        display: block;
        width: 130px;
        height: 50px;
        background-color: #fff;
      }
    }
    .shop-member-name {
      float: left;
      padding: 10px 0;
      margin-left: 10px;
      font-size: 14px;
      .shop-name {
        color: $color-main;
        font-weight: 700;
        margin-bottom: 10px;
      }
    }
    .shop-other-info {
      float: right;
      .goods-num {
        float: left;
        width: 120px;
        line-height: 70px;
      }
      .shop-add-score {
        float: left;
        width: 150px;
      }
      .shop-address {
        overflow: hidden;
        text-overflow:ellipsis;
        white-space: nowrap;
        margin: 10px 0;
      }
      .shop-score {
        position: relative;
      }
      .shop-score-tit {
        color: $color-main;
      }
      .shop-score-arrow-span {
        background: #FFF;
        vertical-align: middle;
        display: inline-block;
        width: 12px;
        height: 11px;
        border: solid 1px #AAA;
        border-radius: 3px;
        position: relative;
        z-index: 1;
      }
      .shop-score-arrow {
        font-size: 0;
        line-height: 0;
        width: 0;
        height: 0;
        border-width: 4px;
        border-color: $color-main transparent transparent transparent;
        border-style: solid dashed dashed dashed;
        position: absolute;
        z-index: 1;
        top: 4px;
        left: 2px;
      }
      .shop-score:hover .shop-score-arrow {
        border-color: transparent transparent $color-main transparent;
        border-style: dashed dashed solid dashed;
        top: 0;
      }
      .shop-score:hover .shop-score-box {
        display: block;
      }
      .shop-score-box {
        width: 180px;
        background-color: #FFF;
        display: none;
        padding: 8px;
        border: solid 1px #AAA;
        position: absolute;
        z-index: 5;
        top: 20px;
        left: -30px;
        box-shadow: 2px 2px 1px rgba(153,153,153,0.5)
      }
      .expanded-opt {
        float: left;
        width: 120px;
        line-height: 70px;
        text-align: center;
        .expanded-opt-a {
          text-decoration-line: underline;
        }
        .ea-icon-right-d-arrow {
          font-size: 12px;
          margin-left: 3px;
          display: inline-block;
          transform: rotate(90deg);
          transition: all ease .3s;
          &.on {
            transform: rotate(-90deg);
          }
        }
      }
    }
    .shop-goods {
      position: relative;
      width: 1210px - 80px;
      margin-left: 80px;
      height: 0;
      transition: all ease .3s;
      border: solid 1px transparent;
      background: transparent;
      box-sizing: border-box;
      overflow: hidden;
      &.on {
        height: 232px;
        border-color: #E7E7E7;
        background-color: #F8F8F8;
      }
      .no-goods {
        text-align: center;
        line-height: 232px;
        background: url(../../assets/images/icon-empty-member.png) no-repeat 380px center;
        a {
          color: #0366d6;
          &:hover { color: $color-main }
        }
      }
    }
    .goods-item {
      float: left;
      width: 165px;
      margin-left: 50px;
      margin-top: 10px;
      overflow: hidden;
    }
    .goods-image {
      width: 165px;
      height: 165px;
    }
    .goods-price {
      margin-top: 5px;
    }
    .goods-name {
      overflow: hidden;
      text-overflow:ellipsis;
      white-space: nowrap;
    }
  }
  /deep/ .el-pagination {
    text-align: right;
    margin-bottom: 20px;
  }
</style>
