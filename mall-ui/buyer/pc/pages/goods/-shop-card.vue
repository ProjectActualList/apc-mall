<template>
  <div id="shop-card" class="shop-card">
    <nuxt-link :to="'/shop/' + shopId" class="shop-title">{{ shopBaseInfo.shop_name }}</nuxt-link>
    <img :src="shopBaseInfo.shop_logo" :alt="shopBaseInfo.shop_name" class="shop-logo">
    <div class="shop-rate">
      <div class="rate-item">
        <h5>描述</h5>
        <span>{{ shopBaseInfo.shop_description_credit }}分</span>
      </div>
      <div class="rate-item">
        <h5>服务</h5>
        <span>{{ shopBaseInfo.shop_service_credit }}分</span>
      </div>
      <div class="rate-item">
        <h5>发货</h5>
        <span>{{ shopBaseInfo.shop_delivery_credit }}分</span>
      </div>
    </div>
    <div class="shop-btns">
      <nuxt-link :to="'/shop/' + shopId" class="shop-btn into">进入商家店铺</nuxt-link>
      <a href="javascript:;" @click="collectionShop" class="shop-btn collection">
        {{ collected ? '已收藏' : '收藏店铺' }}({{ shopBaseInfo.shop_collect }})
      </a>
    </div>
    <div class="shop-address">
      <dl>
        <dt>店铺名称：</dt>
        <dd>{{ shopBaseInfo.shop_name }}</dd>
      </dl>
      <dl>
        <dt>所&ensp;在&ensp;地：</dt>
        <dd>{{ shopRegions }}</dd>
      </dl>
      <dl v-if="shopBaseInfo.shop_qq">
        <dt>在线咨询：</dt>
        <dd>
          <a :href="'http://wpa.qq.com/msgrd?v=3&uin='+ shopBaseInfo.shop_qq +'&site=qq&menu=yes'" target="_blank">
            <i class="iconfont ea-icon-service"></i>客服QQ
          </a>
        </dd>
      </dl>
    </div>
    <div class="shop-contact">
      <i class="iconfont ea-icon-call-phone"></i>
      {{ shopBaseInfo.link_phone || '暂无联系方式' }}
    </div>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import * as API_Shop from '@/api/shop'
  import * as API_Members from '@/api/members'
  import Storage from '@/utils/storage'
  export default {
    name: "shop-card",
    props: ['shopId'],
    data() {
      return {
        // 店铺基本信息
        shopBaseInfo: '',
        // 是否已收藏此店铺
        collected: false
      }
    },
    mounted() {
      API_Shop.getShopBaseInfo(this.shopId).then(response => {
        this.shopBaseInfo = response
      })
      // 如果用户已登录，获取是否已收藏此店铺
      Storage.getItem('refresh_token') && API_Members.getShopIsCollect(this.shopId).then(response => {
        this.collected = response.message
      })
    },
    computed: {
      shopRegions() {
        if (!this.shopBaseInfo) return ''
        const d = this.shopBaseInfo
        return `${d.shop_province}-${d.shop_city}`
        // return `${d.shop_province}-${d.shop_city}-${d.shop_county}${d.shop_town ? ('-' + d.shop_town) : ''}`
      },
      ...mapGetters(['user'])
    },
    methods: {
      /** 收藏店铺 */
      collectionShop() {
        if (this.collected) {
          this.$message.error('您已经收藏过这个店铺啦！')
          return false
        }
        if (!this.user) {
          this.$message.error('请先登录！')
          return false
        }
        API_Members.collectionShop(this.shopId).then(() => {
          this.$message.success('收藏店铺成功！')
          this.collected = true
          this.shopBaseInfo.shop_collect += 1
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .shop-card {
    position: relative;
    width: 210px;
    height: 400px;
    box-shadow: 0 0 10px 0 #cccccc;
    .shop-title {
      display: block;
      height: 30px;
      line-height: 30px;
      background-color: #f5f5f5;
      padding-left: 10px;
      color: #333;
    }
    .shop-logo {
      width: 190px;
      height: 50px;
      margin-left: 10px;
      margin-top: 5px;
    }
    .shop-rate {
      display: flex;
      justify-content: space-between;
      padding: 0 10px;
      margin-top: 8px;
      .rate-item {
        width: (210px - 20px) / 3;
        text-align: center;
      }
      h5 {
        color: #777;
        height: 16px;
        line-height: 16px;
      }
      span {
        color: darken($color-main, 10%);
        height: 16px;
        line-height: 16px;
        font-weight: normal;
      }
    }
    .shop-btns {
      border-bottom: 1px solid #e6e6e6;
      font-size: 0;
      margin-top: 10px;
      padding-bottom: 12px;
      text-align: center;
      overflow: hidden;
      .shop-btn {
        background-color: #f5f5f5;
        border: 1px solid #ccc;
        color: #333;
        display: inline-block;
        font-size: 12px;
        height: 20px;
        line-height: 20px;
        padding: 3px 10px;
        text-align: center;
        vertical-align: top;
        font-weight: normal;
      }
      .into {
        background-color: #333;
        border-color: #333;
        color: #fff;
        margin-right: 10px;
      }
    }
    .shop-address {
      padding: 5px 10px;
      dl {
        margin: 10px 0;
      }
      dt, dd {
        display: inline-block;
        vertical-align: top;
        font-size: 12px;
      }
      dt {
        width: 60px;
        color: #666;
      }
      dd {
        color: #333;
      }
    }
    .shop-contact {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      height: 39px;
      border-top: 1px dashed #e2e2e2;
      line-height: 39px;
      text-align: center;
    }
  }
</style>
