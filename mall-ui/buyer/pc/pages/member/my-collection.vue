<template>
  <div id="my-collection">
    <div class="member-nav">
      <ul class="member-nav-list">
        <li>
          <nuxt-link to="./my-collection">商品收藏</nuxt-link>
        </li>
        <li>
          <nuxt-link to="./my-collection?type=shop">店铺收藏</nuxt-link>
        </li>
      </ul>
    </div>
    <div class="collection-container">
      <div v-show="type !== 'shop'" class="goods">
        <empty-member v-if="goodsData.data && !goodsData.data.length">暂无商品收藏</empty-member>
        <template v-else>
          <ul>
            <li
              v-for="goods in goodsData.data"
              :key="goods.goods_id"
              :class="['coll-g-item', goods.show_del_pop && 'del-pop-show']"
            >
              <div class="goods-image">
                <a :href="'/goods/' + goods.goods_id" target="_blank">
                  <img :src="goods.goods_img" :alt="goods.goods_name">
                </a>
                <div class="goods-delete" @click="goods.show_del_pop = 1">
                  <i class="iconfont ea-icon-delete"></i>
                </div>
                <div class="goods-image-btns">
                  <nuxt-link :to="'/shop/' + goods.shop_id" target="_blank">进入店铺</nuxt-link>
                  <a :href="'/goods/' + goods.goods_id" target="_blank">查看商品</a>
                </div>
              </div>
              <div class="goods-name">
                <a :href="'/goods/' + goods.goods_id" target="_blank">
                  {{ goods.goods_name }}
                </a>
              </div>
              <div class="goods-price">
                <div class="price">
                  <span>￥</span>
                  <strong>{{ goods.goods_price | unitPrice }}</strong>
                </div>
                <div v-if="goods.goods_original_price" class="original-price">
                  <span>￥</span>
                  <span>{{ goods.goods_original_price | unitPrice }}</span>
                </div>
              </div>
              <div class="del-pop">
                <div class="del-pop-bg"></div>
                <div class="del-pop-box">
                  <div class="txt">确定删除？</div>
                  <div class="btns">
                    <a href="javascript:;" @click="handleDeleteGoodsColl(goods)">确定</a>
                    <a href="javascript:;" @click="goods.show_del_pop = 0">取消</a>
                  </div>
                </div>
              </div>
            </li>
            <li class="clr"></li>
          </ul>
          <el-pagination
            v-if="goodsData"
            layout="prev, pager, next"
            @current-change="handleGoodsCurrentChange"
            :current-page="goodsData.page_no"
            :page-size="goodsData.page_size"
            :total="goodsData.data_total"/>
        </template>
      </div>
      <div v-show="type === 'shop'" class="shop">
        <empty-member v-if="shopData.data && !shopData.data.length">暂无店铺收藏</empty-member>
        <template v-else>
          <ul>
            <li v-for="shop in shopData.data" :key="shop.shop_id" :class="['coll-s-item', shop.show_del_pop && 'del-pop-show']">
              <div class="shop-card">
                <div class="shop-card-side">
                  <nuxt-link :to="'/shop/' + shop.shop_id">
                    <en-shop-logo :url="shop.logo"/>
                    <!--<img :src="shop.logo" :alt="shop.shop_name">-->
                  </nuxt-link>
                  <a @click="shop.show_del_pop = 1" class="del-btn" href="javascript:void(0)">删除</a>
                </div>
                <div class="shop-card-main">
                  <nuxt-link :to="'/shop/' + shop.shop_id" class="shop-name">{{ shop.shop_name }}</nuxt-link>
                  <div class="shop-other">
                    <p style="margin-bottom: 5px">店铺评分：</p>
                    <p>描述相符: {{ shop.shop_description_credit }}</p>
                    <p>服务态度: {{ shop.shop_service_credit }}</p>
                    <p>发货速度: {{ shop.shop_delivery_credit }}</p>
                  </div>
                </div>
              </div>
              <div class="shop-content">
                <div class="shop-goods-box">
                  <div class="goods-tab clearfix">
                    <div class="tab-item">
                      <nuxt-link :to="'/shop/goods-list?shop_id=' + shop.shop_id">店铺商品</nuxt-link>
                    </div>
                  </div>
                  <nuxt-link :to="'/shop/' + shop.shop_id" class="see-more">查看更多&gt;&gt;</nuxt-link>
                </div>
                <no-ssr>
                  <swiper :options="shopSwiperOptions" class="shop-goods-list">
                    <swiper-slide v-for="goods in shop.goods_list" :key="goods.goods_id" class="goods-item">
                      <div class="goods-image">
                        <a :href="'/goods/' + goods.goods_id">
                          <img :src="goods.thumbnail" :alt="goods.goods_name">
                        </a>
                      </div>
                      <div class="goods-price">
                        <div class="price">
                          <span>￥</span>
                          <strong>{{ goods.price | unitPrice }}</strong>
                        </div>
                        <div v-if="goods.original_price" class="original-price">
                          <span>￥{{ goods.original_price | unitPrice }}</span>
                        </div>
                      </div>
                    </swiper-slide>
                  </swiper>
                </no-ssr>
              </div>
              <div class="del-pop">
                <div class="del-pop-bg"></div>
                <div class="del-pop-box">
                  <div class="txt">确定删除？</div>
                  <div class="btns">
                    <a href="javascript:;" @click="handleDeleteShopColl(shop)">确定</a>
                    <a href="javascript:;" @click="shop.show_del_pop = 0">取消</a>
                  </div>
                </div>
              </div>
            </li>
          </ul>
          <el-pagination
            v-if="shopData"
            layout="prev, pager, next"
            @current-change="handleShopCurrentChange"
            :total="shopData.data_total"/>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
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
        type: this.$route.query.type,
        /** 商品收藏参数 */
        params_goods: {
          page_no: 1,
          page_size: 30
        },
        /** 店铺收藏参数 */
        params_shop: {
          page_no: 1,
          page_size: 10
        },
        /** 商品数据 */
        goodsData: '',
        /** 店铺数据 */
        shopData: '',
        // 店铺swiper配置
        shopSwiperOptions: {
          slidesPerView: 5,
          slidesPerGroup: 5
        }
      }
    },
    mounted() {
      this.GET_Collection(this.type)
    },
    watch: {
      $route: function () {
        const { type } = this.$route.query
        this.type = type
        if (type !== 'shop' && !this.goodsData) this.GET_Collection('goods')
        if (type === 'shop' && !this.shopData) this.GET_Collection('shop')
      }
    },
    methods: {
      /** 删除商品收藏 */
      handleDeleteGoodsColl(goods) {
        API_Members.deleteGoodsCollection(goods.goods_id).then(() => {
          this.GET_Collection('goods')
        })
      },
      /** 商品收藏当前页发生改变 */
      handleGoodsCurrentChange(page) {
        this.params_goods.page_no = page
        this.GET_Collection('goods')
      },
      /** 删除店铺收藏 */
      handleDeleteShopColl(shop) {
        API_Members.deleteShopCollection(shop.shop_id).then(() => {
          this.GET_Collection('shop')
        })
      },
      /** 店铺收藏当前页发生改变 */
      handleShopCurrentChange(page) {
        this.params_shop.page_no = page
        this.GET_Collection('shop')
      },
      /** 获取收藏 */
      GET_Collection(type) {
        if (type !== 'shop') {
          API_Members.getGoodsCollection(this.params_goods).then(response => {
            response.data.map(item => {
              // 初始化是否显示删除遮罩标识
              item.show_del_pop = 0
              return item
            })
            this.goodsData = response
            this.MixinScrollToTop()
          })
        } else {
          API_Members.getShopCollection(this.params_shop).then(response => {
            response.data.map(item => {
              // 初始化是否显示删除遮罩标识
              item.show_del_pop = 0
              return item
            })
            this.shopData = response
            this.MixinScrollToTop()
          })
        }
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .collection-container {
    position: relative;
    margin-top: 10px;
    /deep/ .el-pagination { text-align: right }
  }
  .coll-g-item {
    position: relative;
    float: left;
    width: 150px;
    height: 226px;
    margin-right: 14px;
    text-align: center;
    margin-bottom: 14px;
    &:nth-child(6n) { margin-right: 0 }
    .goods-image {
      border: #eee 1px solid;
      margin-bottom: 11px;
      height: 148px;
      overflow: hidden;
      position: relative;
      img {
        width: 100%;
        height: 100%;
      }
      .goods-delete {
        display: none;
        position: absolute;
        top: 0;
        right: 0;
        width: 25px;
        height: 25px;
        background: rgba(0,0,0,.5);
        color: #fff;
        cursor: pointer;
        text-align: center;
        line-height: 25px;
        &:hover { background-color: #f40 }
      }
      .goods-image-btns {
        display: none;
        position: absolute;
        left: 0;
        bottom: 0;
        width: 100%;
        height: 25px;
        a {
          display: inline-block;
          width: 50%;
          line-height: 25px;
          background: rgba(0,0,0,.5);
          color: #fff;
          cursor: pointer;
          &:hover { background-color: #f40 }
          &:last-child {
            border-left: 1px solid #fff;
            margin-right: -1px;
          }
        }
      }
    }
    .goods-name {
      height: 23px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      color: #3c3c3c;
    }
    .goods-price {
      word-wrap: break-word;
      word-break: normal;
      height: 23px;
      line-height: 23px;
      overflow: hidden;
      .price {
        display: inline;
        span {
          color: #f40;
          margin-right: 0;
        }
        strong {
          color: #f40;
          font-weight: 700;
        }
      }
      .original-price {
        margin-left: 3px;
        text-decoration: line-through;
        white-space: nowrap;
        font-weight: 400;
        display: inline;
        color: #6c6c6c;
        span {
          color: #9c9c9c;
        }
      }
    }
    &:hover:not(.del-pop-show) {
      .goods-delete, .goods-image-btns {
        display: block;
      }
    }
  }
  .coll-s-item {
    display: flex;
    position: relative;
    min-height: 279px;
    border-bottom: #e5e5e5 1px solid;
    &:last-child { border-bottom: none };
    .shop-card {
      width: 200px;
      margin-right: 30px;
      padding-top: 20px;
      position: relative;
    }
    .shop-card-side {
      display: flex;
      align-items: center;
      justify-content: space-between;
      a {
        display: inline-block;
      }
      img {
        display: block;
        width: 160px;
        height: 50px;
      }
      .del-btn {
        color: $color-main;
      }
    }
    .shop-card-main {
      position: relative;
    }
    .shop-other {
      margin-top: 10px;
    }
    .shop-name {
      display: block;
      line-height: 25px;
      margin-bottom: 2px;
      height: 26px;
      width: 190px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      font-size: 16px;
      font-weight: 600;
      color: #3c3c3c;
      &:hover { color: $color-main }
    }
    .shop-tools {
      display: inline-block;
      margin-left: 20px;
      float: right;
      .ea-icon-delete {
        font-size: 20px;
        line-height: normal;
      }
    }
    .shop-content {
      width: 990px - 300px;
    }
    .shop-goods-box {
      padding-top: 20px;
      padding-bottom: 16px;
      overflow: hidden;
    }
    .goods-tab {
      float: left;
      border: #dcdcdc 1px solid;
      .tab-item {
        float: left;
        padding: 0 13px;
        height: 26px;
        line-height: 26px;
        text-align: center;
        color: #6c6c6c;
        cursor: pointer;
        border-right: #dcdcdc 1px solid;
        &.active {
          background-color: #f3f3f3;
          color: $color-main;
        }
        &:last-child { border-right: none }
      }
    }
    .see-more {
      float: right;
      color: #6c6c6c;
      padding-right: 10px;
      &:hover { color: $color-main }
    }
    .shop-goods-list {
      .goods-list {
        overflow: hidden;
        width: 100%;
        height: 205px;
      }
      .goods-item {
        position: relative;
        float: left;
        height: 205px;
        text-align: center;
      }
      .goods-image {
        width: 132px;
        height: 132px;
        border: #eee 1px solid;
        margin-bottom: 8px;
        img {
          width: 100%;
          height: 100%;
        }
      }
      .goods-price {
        height: 25px;
        line-height: 25px;
        div {
          display: inline;
          color: #6c6c6c;
        }
        .price {
          color: #f40;
          margin-right: 0;
          strong {
            color: #f40;
            font-weight: 700;
          }
        }
        .original-price {
          text-decoration: line-through;
          white-space: nowrap;
          font-weight: 400;
          span {
            color: #9c9c9c;
          }
        }
      }
    }
  }
  .del-pop {
    display: none;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 10;
    .del-pop-bg {
      width: 100%;
      height: 100%;
      background-color: #000;
      opacity: .5;
      z-index: 11;
    }
    .del-pop-box {
      position: absolute;
      top: 50%;
      left: 0;
      margin-top: -30px;
      width: 100%;
      z-index: 12;
      a {
        display: inline-block;
        width: 52px;
        height: 22px;
        line-height: 22px;
        border: #c5c5c5 1px solid;
        background-color: #fff;
        cursor: pointer;
        margin-left: 3px;
        margin-right: 3px;
        &:first-child {
          border-color: #ff4200;
          background-color: #ff4200;
          color: #fff;
        }
      }
    }
    .txt {
      height: 25px;
      line-height: 25px;
      color: #fff;
      margin-bottom: 7px;
      text-align: center;
    }
    .btns { text-align: center }
  }
  .del-pop-show .del-pop {
    display: block;
  }
</style>
