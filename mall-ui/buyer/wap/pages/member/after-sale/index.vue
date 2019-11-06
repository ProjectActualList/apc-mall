<template>
  <div id="after-sale" style="background-color: #f7f7f7">
    <nav-bar title="售后列表"/>
    <div class="after-sale-container">
      <empty-member v-if="finished && !afterSaleList.length">暂无订单</empty-member>
      <van-list
        v-else
        v-model="loading"
        :finished="finished"
        @load="onLoad"
      >
        <div class="order-item" v-for="(order, index) in afterSaleList" :key="index">
          <div class="sn-order-item">
            售后单号：{{ order.sn }}
          </div>
          <div class="info-order-item">
            <p><span>售后状态：</span><em style="color: #3985ff;">{{ order.refund_status_text }}</em></p>
            <p><span>退款金额：</span><em class="price">￥{{ order.refund_price | unitPrice }}</em></p>
            <div class="order-btns">
              <nuxt-link :to="'./after-sale/detail?sn=' + order.sn">查看详情</nuxt-link>
            </div>
          </div>
        </div>
      </van-list>
    </div>
  </div>
</template>

<script>
  import * as API_AfterSale from '@/api/after-sale'
  export default {
    name: 'after-sale-index',
    data() {
      return {
        loading: false,
        finished: false,
        afterSaleList: [],
        params: {
          page_no: 0,
          page_size: 10
        }
      }
    },
    methods: {
      /** 加载售后数据 */
      onLoad() {
        this.params.page_no += 1
        this.GET_AfterSale()
      },
      /** 获取售后列表 */
      GET_AfterSale() {
        this.loading = true
        API_AfterSale.getAfterSale(this.params).then(response => {
          const { data } = response
          if(!data || !data.length) {
            this.finished = true
          } else {
            this.afterSaleList.push(...data)
          }
          this.loading = false
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  .after-sale-container {
    padding-top: 46px;
  }
  .order-item {
    margin-bottom: 10px;
    background-color: #fff;
    padding: 0 10px 10px 10px;
    .sn-order-item {
      position: relative;
      height: 45px;
      line-height: 45px;
      font-size: 14px;
      color: #333;
      &::after {
        content: "";
        position: absolute;
        z-index: 1;
        pointer-events: none;
        background-color: #e5e5e5;
        height: 1px;
        left: 0;
        right: 0;
        bottom: 0;
        margin: 0 -10px;
      }
    }
    .info-order-item {
      display: block;
      padding: 10px 0;
      position: relative;
      &::after {
        content: "";
        height: 0;
        display: block;
        border-bottom: 1px solid #e5e5e5;
        position: absolute;
        left: 0;
        right: 0;
        bottom: 0;
      }
      p {
        line-height: 20px;
        font-size: 12px;
        color: #999;
        span { color: #666 }
      }
    }
    .order-btns {
      position: absolute;
      top: 17px;
      right: 0;
      a {
        display: inline-block;
        width: 65px;
        height: 30px;
        margin-left: 5px;
        text-align: center;
        color: #fff;
        line-height: 30px;
        z-index: 1;
        border-radius: 2px;
        background: #e4393c;
        -webkit-tap-highlight-color: rgba(0,0,0,0);
        outline: 0;
        border: none;
      }
    }
  }
</style>
