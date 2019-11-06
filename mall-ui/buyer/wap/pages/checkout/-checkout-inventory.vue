<template>
  <div id="inventories" class="inventories">
    <van-cell-group v-for="(shop, shopIndex) in inventories" :key="'shop_' + shopIndex">
      <van-cell>
        <van-icon slot="icon" name="shop" class="ivn-icon"/>
        <div slot="title" class="ivn-name">{{ shop.seller_name }}</div>
      </van-cell>
      <van-cell class="shop-inner-cell">
        <div>
          运费合计：<span class="price">￥{{ shop.price.freight_price | unitPrice }}</span>
           店铺总重：<span class="price">{{ shop.weight }}kg</span>
        </div>
      </van-cell>
      <van-cell v-if="shop.promotion_notice" class="shop-inner-cell">
        <div class="promotion-notice">{{ shop.promotion_notice }}</div>
      </van-cell>
      <van-cell v-for="(sku, skuIndex) in shop.sku_list" :key="'sku_' + skuIndex">
        <div class="sku-item">
          <div class="sku-img">
            <img :src="sku.goods_image" :alt="sku.name">
            <span v-if="!sku.is_ship" class="out-of-stock-tip">该地区无货</span>
          </div>
          <div class="sku-info">
            <strong class="sku-name">{{ sku.name }}</strong>
            <p v-if="sku.spec_list" class="sku-spec">{{ sku | formatterSkuSpec }}</p>
            <p v-if="sku.promotion_tags && sku.promotion_tags.length">
              <span class="sku-act-tag" v-for="(tag, _index) in sku.promotion_tags" :key="'tag_' + _index">{{ tag }}</span>
            </p>
            <p class="sku-price">
              <span class="price">
                <i>￥</i>{{ sku.purchase_price | unitPrice }}
                <span v-if="sku.purchase_price < sku.original_price" class="original-price">￥{{ sku.original_price | unitPrice }}</span>
                <span class="sku-weight">{{ sku.goods_weight }}kg</span>
              </span>
              <span class="sku-num">x{{ sku.num }}</span>
            </p>
          </div>
        </div>
      </van-cell>
      <van-cell v-if="showActBox(shop)" class="act-cell">
        <div class="act-box" @click="handleShowActs(shop)">
          <div class="inner-act">
            <span class="inner-act-tit">赠品</span>
            <ul class="inner-act-list">
              <li v-for="(coupon, couponIndex) in shop.gift_coupon_list" :key="'coupon_' + couponIndex">
                {{ coupon.amount }}的优惠券
              </li>
              <li v-for="(gift, giftIndex) in shop.gift_list" :key="'gift_' + giftIndex">
                价值{{ gift.gift_price }}的{{ gift.gift_name }}
              </li>
            </ul>
          </div>
        </div>
      </van-cell>
    </van-cell-group>
    <van-popup
      v-model="showActs"
      position="bottom"
      class="acts-popup"
    >
      <van-nav-bar title="赠品详情" @click-right="showActs = false">
        <i class="iconfont ea-icon-close" slot="right"></i>
      </van-nav-bar>
      <van-cell-group :border="false">
        <van-cell v-for="(coupon, couponIndex) in surActsShop.gift_coupon_list" :key="'coupon_' + couponIndex">
          <div class="popup-act-item">
            <div class="popup-act-img">
              <img src="../../assets/images/icon-color-coupon.png" alt="">
            </div>
            <div class="popup-act-name">
              价值<span class="price">
              ￥{{ coupon.amount | unitPrice }}</span>
              的优惠券
              [{{ coupon.use_term }}]
            </div>
          </div>
        </van-cell>
        <van-cell v-for="(gift, giftIndex) in surActsShop.gift_list" :key="'gift_' + giftIndex">
          <div class="popup-act-item">
            <div class="popup-act-img">
              <img :src="gift.gift_img" :alt="gift.gift_name">
            </div>
            <div class="popup-act-name">
              价值<span class="price">
              ￥{{ gift.gift_price | unitPrice }}</span>
              的{{ gift.gift_name }}
            </div>
          </div>
        </van-cell>
      </van-cell-group>
    </van-popup>
  </div>
</template>

<script>
  /**
   * 结算页
   * 购物车清单组件
   */
  import * as API_Trade from '@/api/trade'
  export default {
    name: 'checkout-inventory',
    props: ['inventories'],
    data() {
      return {
        surActsShop: '',
        showActs: false
      }
    },
    methods: {
      handleShowActs(shop) {
        this.surActsShop = shop
        this.showActs = true
      },
      showActBox(shop) {
        const { gift_coupon_list, gift_list } = shop
        if (!gift_list && !gift_coupon_list) return false
        return !!(gift_list.length || gift_list.length)
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .inventories {
    margin-top: 10px;
  }
  .ivn-icon {
    font-size: 16px;
  }
  .ivn-name {
    color: #333;
    font-size: 16px;
    font-weight: 700;
    margin-left: 10px;
  }
  .van-hairline:first-child::after {
    left: 0;
  }
  .promotion-notice {
    color: $color-main;
  }
  .sku-item {
    display: flex;
  }
  .sku-img {
    position: relative;
    flex-shrink: 0;
    img {
      width: 75px;
      height: 75px;
    }
  }
  .sku-info {
    flex: 1;
    margin-left: 10px;
    .sku-name {
      color: #333;
      line-height: 20px;
      min-height: 40px;
      font-size: 14px;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }
    .sku-spec {
      font-size: 12px;
      line-height: 15px;
    }
    .sku-price {
      display: flex;
      align-items: center;
      justify-content: space-between;
      line-height: 16px;
      .price {
        font-size: 16px;
        i {
          font-size: 12px;
        }
      }
      .original-price {
        text-decoration-line: line-through;
        color: #ccc;
        font-weight: normal;
        font-size: 12px;
      }
      .sku-num {
        color: #999;
        font-size: 12px;
      }
    }
  }
  .act-cell {
    padding: 0 15px;
  }
  .act-box {
    position: relative;
    margin: 10px 0 15px;
    padding: 10px 0;
    background-color: #fef5f5;
    &::before {
      content: "";
      position: absolute;
      top: -10px;
      left: 33px;
      width: 0;
      height: 0;
      border-style: solid;
      border-width: 5px;
      border-color: transparent transparent #fef5f5;
      z-index: 1;
    }
    .inner-act {
      display: flex;
      color: #999;
      font-size: 12px;
    }
    .inner-act-tit {
      margin: 0 10px;
      color: #666;
      flex-shrink: 0;
    }
    .inner-act-list {
      flex: 1;
      padding-right: 15px;
      overflow: hidden;
      li {
        line-height: 18px;
      }
      &::after {
        content: "";
        display: inline-block;
        vertical-align: middle;
        width: 6px;
        height: 10px;
        background-image: url("data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 12 20%22%3E%3Cpath fill=%22#CCCCCC%22 fill-rule=%22evenodd%22 d=%22M2 20c-.8 0-1.5-.5-1.8-1.2-.3-.8-.2-1.6.4-2.2L7.2 10 .6 3.4c-.8-.8-.8-2 0-2.8.8-.8 2-.8 2.8 0l8 8c.4.4.6 1 .6 1.4 0 .5-.2 1-.6 1.4l-8 8c-.4.4-1 .6-1.4.6z%22/%3E%3C/svg%3E");
        background-repeat: no-repeat;
        background-size: 100%;
        position: absolute;
        right: 5px;
        top: 50%;
        margin-top: -7px;
      }
    }
  }
  .acts-popup {
    height: 100%;
  }
  .popup-act-item {
    display: flex;
    align-items: center;
  }
  .popup-act-img {
    flex-shrink: 0;
    img {
      width: 60px;
      height: 60px;
    }
  }
  .popup-act-name {
    flex: 1;
    margin-left: 10px;
  }
  .sku-act-tag {
    display: inline-block;
    padding: 0 5px;
    line-height: 15px;
    margin-right: 5px;
    border: 1px solid $color-main;
    color: $color-main
  }
  .sku-weight {
    display: block;
    font-size: 12px;
    color: #999;
    font-weight: normal;
    font-family: "Microsoft YaHei", "Microsoft JhengHei", SimSun, verdana, Tahoma, arial,serif;
  }
  .shop-inner-cell {
    padding: 5px 15px;
  }
  .out-of-stock-tip {
    text-align: center;
    display: block;
    position: absolute;
    bottom: 0;
    left: 0;
    width: 75px;
    height: 20px;
    background-color: rgba(0,0,0,0.7);
    color: #fff;
    font-size: 12px;
  }
</style>
