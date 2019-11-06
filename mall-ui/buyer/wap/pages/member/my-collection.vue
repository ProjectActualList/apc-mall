<template>
  <div id="my-collection">
    <nav-bar title="我的收藏"/>
    <van-tabs v-model="tabActive" :line-width="100">
      <van-tab title="收藏的商品"/>
      <van-tab title="收藏的店铺"/>
    </van-tabs>
    <div class="collection-container">
      <template v-if="tabActive === 0">
        <empty-member v-if="finished_goods && !goodsList.length">暂无收藏的商品</empty-member>
        <van-list
          v-else
          v-model="loading_goods"
          :finished="finished_goods"
          @load="onLoad"
        >
          <div v-for="(goods, index) in goodsList" :key="index" class="goods-item">
            <div class="goods-image">
              <a :href="'/goods/' + goods.goods_id">
                <img :src="goods.goods_img" :alt="goods.goods_name">
              </a>
            </div>
            <div class="goods-detail">
              <a :href="'/goods/' + goods.goods_id" class="goods-detail-name">{{ goods.goods_name }}</a>
              <div class="goods-detail-price">
                <span class="price">￥{{ goods.goods_price | unitPrice }}</span>
              </div>
              <div class="goods-detail-info"></div>
              <div class="goods-detai-buttons">
                <span class="btn-item delete" @click="handleDeleteGoodsColl(goods)">取消收藏</span>
              </div>
            </div>
          </div>
        </van-list>
      </template>
      <template v-else>
        <empty-member v-if="finished_shop && !shopList.length">暂无收藏的店铺</empty-member>
        <van-list
          v-else
          v-model="loading_shop"
          :finished="finished_shop"
          @load="onLoad"
        >
          <div v-for="(shop, index) in shopList" :key="index" class="shop-item">
            <div class="shop-logo">
              <img :src="shop.logo" :alt="shop.shop_name">
            </div>
            <div class="shop-tools">
              <span class="shop-name">{{ shop.shop_name }}</span>
              <div class="shop-btns">
                <a href="javascript:;" @click="handleDeleteShopColl(shop)">取消收藏</a>
                <a :href="'/shop/' + shop.shop_id">进入店铺</a>
              </div>
            </div>
          </div>
        </van-list>
      </template>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import { List } from 'vant'
  Vue.use(List)
  import * as API_Members from '@/api/members'
  export default {
    name: 'my-collection',
    head() {
      return {
        title: `我的收藏-${this.site.site_name}`
      }
    },
    data() {
      return {
        // 收藏类型 0商品 1店铺
        tabActive: this.$route.query.type !== 'shop' ? 0 : 1,
        // 商品收藏参数
        params_goods: {
          page_no: 0,
          page_size: 10
        },
        // 店铺收藏参数
        params_shop: {
          page_no: 0,
          page_size: 10
        },
        // 加载中 商品
        loading_goods: false,
        // 加载中 店铺
        loading_shop: false,
        // 是否加载完成 商品
        finished_goods: false,
        // 是否加载完成 店铺
        finished_shop: false,
        // 商品数据
        goodsList: [],
        // 店铺数据
        shopList: []
      }
    },
    watch: {
      tabActive: function (newVal) {
        if (newVal === 0 && !this.goodsList.length) {
          this.params_goods.page_no = 0
          this.goodsList = []
          this.finished_goods = false
          this.onLoad()
        }
        if (newVal === 1 && !this.shopList.length) {
          this.params_shop.page_no = 0
          this.shopList = []
          this.finished_shop = false
          this.onLoad()
        }
      }
    },
    methods: {
      /** 加载数据 */
      onLoad() {
        if (this.tabActive === 0) {
          this.params_goods.page_no += 1
        } else {
          this.params_shop.page_no += 1
        }
        this.GET_Collection()
      },
      /** 删除商品收藏 */
      handleDeleteGoodsColl(goods) {
        this.$confirm('确定要删除这个商品收藏吗？', () => {
          API_Members.deleteGoodsCollection(goods.goods_id).then(() => {
            this.params_goods.page_no = 1
            this.goodsList = []
            this.finished_goods = false
            this.GET_Collection()
          })
        })
      },
      /** 删除店铺收藏 */
      handleDeleteShopColl(shop) {
        this.$confirm('确定要删除这个店铺收藏吗？', () => {
          API_Members.deleteShopCollection(shop.shop_id).then(() => {
            this.params_shop.page_no = 1
            this.shopList = []
            this.finished_shop = false
            this.GET_Collection()
          })
        })
      },
      /** 获取收藏 */
      GET_Collection() {
        if (this.tabActive === 0) {
          this.loading_goods = true
          API_Members.getGoodsCollection(this.params_goods).then(response => {
            this.loading_goods = false
            const { data } = response
            if (!data || !data.length) {
              this.finished_goods = true
            } else {
              this.goodsList.push(...data)
            }
          }).catch(() => { this.loading = false })
        } else {
          this.loading_shop = true
          API_Members.getShopCollection(this.params_shop).then(response => {
            this.loading_shop = false
            const { data } = response
            if (!data || !data.length) {
              this.finished_shop = true
            } else {
              this.shopList.push(...data)
            }
          })
        }
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  img {
    width: 100%;
    height: 100%;
  }
  .goods-item {
    position: relative;
    display: flex;
    height: 120px;
    padding: 0 10px;
    margin-top: 15px;
  }
  .goods-image {
    width: 105px;
    margin-right: 8px;
    align-self: center;
  }
  .goods-detail {
    width: calc(100% - 105px - 8px);
    border-bottom: 1px solid #e3e3e3;
  }
  .goods-detail-name {
    font-size: 14px;
    margin: 10px 0;
    min-height: 36px;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    overflow: hidden;
  }
  .goods-detail-price {
    font-size: 14px;
    margin: 5px 0;
  }
  .goods-detai-buttons {
    .btn-item {
      display: inline-block;
      margin-right: 10px;
      width: 80px;
      line-height: 20px;
      color: #333;
      text-align: center;
      border: 1px solid #888;
      font-size: 12px;
    }
  }
  .shop-item {
    position: relative;
    display: flex;
    padding: 0 10px;
    margin-top: 15px;
  }
  .shop-logo {
    width: 198px;
    height: 78px;
    border: 1px solid #e3e3e3;
    background-color: #fff;
  }
  .shop-tools {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    width: calc(100% - 200px);
    height: 80px;
  }
  .shop-name {
    display: block;
    height: 30px;
    color: #333;
    text-align: center;
    line-height: 30px;
    font-size: 16px;
  }
  .shop-btns {
    display: flex;
    justify-content: center;
    a {
      display: inline-block;
      width: 60px;
      height: 20px;
      line-height: 20px;
      text-align: center;
      font-size: 12px;
      border: 1px solid #686868;
      &:first-child {
        margin-right: 10px;
      }
    }
  }
</style>
