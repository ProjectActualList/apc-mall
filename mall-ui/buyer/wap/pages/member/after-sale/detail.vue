<template>
  <div id="after-sale-detail">
    <nav-bar title="售后详情"/>
    <div v-if="detail" class="order-detail">
      <div class="order-items">
        <div class="order-item">
          <span>订单编号：</span><span>{{ detail.order_sn }}</span>
        </div>
        <div class="order-item">
          <span>售后单号：</span><span>{{ detail.sn }}</span>
        </div>
        <div class="order-item">
          <span>申请时间：</span><span>{{ detail.create_time | unixToDate }}</span>
        </div>
        <div class="order-item">
          <span>售后状态：</span><span>{{ detail.refund_status_text }}</span>
        </div>
        <div class="order-item">
          <span>申请原因：</span><span>{{ detail.refund_reason || '无' }}</span>
        </div>
        <div class="order-item">
          <span>详细描述：</span><span>{{ detail.customer_remark || '无' }}</span>
        </div>
        <div class="order-item">
          <span>商家回复：</span><span>{{ detail.seller_remark || '无' }}</span>
        </div>
        <div class="order-item">
          <span>退款方式：</span><span>{{ detail.refund_way === 'ORIGINAL' ? '原路退回' : (detail.account_type_text || '无') }}</span>
        </div>
        <div class="order-item">
          <span>退款金额：</span><span>{{ detail.refund_price | unitPrice }}</span>
        </div>
        <span class="hr"></span>
      </div>
    </div>
    <ul v-if="skuList" class="sku-list">
      <li class="sku-item" v-for="(sku, index) in skuList" :key="index">
        <a :href="'/goods/' + sku.goods_id" class="goods-image">
          <img :src="sku.goods_image" :alt="sku.goods_name">
        </a>
        <a :href="'/goods/' + sku.goods_id">
          <span  class="goods-name">{{ sku.goods_name }}</span>
          <p v-if="sku.spec_list" class="sku-spec">{{ sku | formatterSkuSpec }}</p>
        </a>
        <div class="goods-info">
          <p class="price">￥{{ sku.price | unitPrice }}</p>
          <p class="goods-num">x{{ sku.return_num }}</p>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
  import * as API_AfterSale from '@/api/after-sale'
  export default {
    name: 'detail',
    validate({ query }) {
      return !!query.sn
    },
    data() {
      return {
        sn: this.$route.query.sn,
        detail: '',
        skuList: ''
      }
    },
    mounted() {
      this.GET_AfterSaleDetail()
    },
    methods: {
      GET_AfterSaleDetail() {
        API_AfterSale.getAfterSaleDetail(this.sn).then(response => {
          this.detail = response.refund
          this.skuList = response.refund_goods
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  .order-detail {
    padding-top: 46px;
  }
  .hr {
    display: block;
    width: 100%;
    height: 1px;
    background-color: #e5e5e5;
    clear: both;
  }
  .order-items {
    background-color: #fff;
    padding: 0 10px;
    .order-item {
      padding: 5px 0;
      font-size: 14px;
      .price {
        font-size: 16px;
      }
    }
  }
  .sku-box {
    clear: both;
    margin-top: 10px;
    background-color: #fff;
    .shop-info {
      height: 40px;
      line-height: 40px;
      font-size: 14px;
      border-bottom: 1px solid #e5e5e5;
      padding: 0 10px;
      margin: 0 auto;
    }
    .shop-name {
      a {
        color: $color-href;
        &:hover { color: $color-main }
      }
      .van-icon {
        vertical-align: middle;
        margin-right: 5px;
      }
      .van-icon-points-mall {
        font-size: 20px;
      }
    }
    .sku-num {
      float: right;
    }
  }
  .sku-list {
    padding: 0 10px;
  }
  .sku-item {
    display: flex;
    margin: 10px auto;
    min-height: 75px;
    .goods-name {
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 3;
      overflow: hidden;
    }
    .goods-image {
      margin-right: 10px;
      img {
        width: 60px;
        height: 60px;
      }
    }
    .goods-info {
      margin-left: 10px;
      .price {
        font-size: 14px;
      }
    }
  }
</style>
