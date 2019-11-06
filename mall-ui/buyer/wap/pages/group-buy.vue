<template>
  <div id="group-buy">
    <nav-bar title="团购活动" fixed/>
    <van-tabs @click="handleClickCate">
      <van-tab
        v-for="(cate, index) in categories"
        :title="cate.cat_name"
        :key="index"
      >
      </van-tab>
    </van-tabs>
    <en-empty v-if="finished && !groupBuy.length" style="line-height:420px">暂无商品</en-empty>
    <van-list
      v-model="loading"
      :finished="finished"
      @load="onLoad"
      class="group-buy-container"
    >
      <li v-for="(goods, index) in groupBuy" :key="index" class="item-goods">
        <dt>
          <a :href="'/goods/' + goods.goods_id">
            <img :src="goods.img_url" :alt="goods.goods_name">
          </a>
        </dt>
        <dd>
          <div class="name-goods">{{ goods.goods_name }}</div>
          <div class="descrip-goods">
            <p></p>
            <p class="price">￥{{ goods.price | unitPrice }}</p>
            <a :href="'/goods/' + goods.goods_id" class="grab-btn">去团购</a>
          </div>
          <div class="price-goods">
            <div>原价：￥{{ goods.original_price | unitPrice }}</div>
            <div>已售出：<span>{{ goods.show_buy_num }}</span>件</div>
          </div>
        </dd>
      </li>
    </van-list>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_Promotions from '@/api/promotions'
  export default {
    name: 'group-buy',
    head() {
      return {
        title: `团购-${this.site.site_name}`
      }
    },
    async asyncData() {
      // 获取团购分类
      let categories = await API_Promotions.getGroupBuyCategorys()
      categories.unshift({ cat_id: 0, cat_name: '全部', cat_order: 0, active: true })
      categories.sort((x, y) => x.cat_order > y.cat_order)
      return { categories }
    },
    data() {
      return {
        loading: false,
        finished: false,
        params: {
          page_no: 0,
          page_size: 10,
          cat_id: 0
        },
        groupBuy: []
      }
    },
    methods: {
      /** 加载数据 */
      onLoad(page_no) {
        this.params.page_no += 1
        this.GET_GroupBuyGoods()
      },
      /** 选择团购分类 */
      handleClickCate(index) {
        const { categories } = this
        const cate = categories[index]
        this.$set(this, 'categories', categories.map(item => {
          item.active = item.cat_id === cate.cat_id
          return item
        }))
        this.finished = false
        this.groupBuy = []
        this.params.page_no = 1
        this.params.cat_id = cate.cat_id
        this.GET_GroupBuyGoods()
      },
      /** 获取团购商品 */
      GET_GroupBuyGoods() {
        this.loading = true
        const params = JSON.parse(JSON.stringify(this.params))
        if (params.cat_id === 0) delete params.cat_id
        API_Promotions.getGroupBuyGoods(params).then(response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            this.groupBuy.push(...data)
          }
        }).catch(() => {
          this.loading = false
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../assets/styles/color";
  /deep/ {
    .van-tabs {
      position: fixed;
      z-index: 10;
      top: 46px;
      width: 100%;
      .van-tabs__wrap {
        background-color: #fff;
      }
    }
  }
  .group-buy-container {
    padding-top: 46px + 46px;
  }
  .item-goods {
    display: flex;
    border-bottom: solid 1px #dedede;
    padding: 8px;
    background: #fff;
    dt {
      width: 120px;
      height: 100px;
      overflow: hidden;
      text-align: center;
      img {
        width: 100px;
        height: 100px;
      }
    }
    dd {
      flex: 2;
      position: relative;
      padding-left: 8px;
    }
    .name-goods {
      font-size: 14px;
      margin-bottom: 0;
      line-height: 20px;
      height: 38px;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      color: #252525;
    }
    .descrip-goods {
      padding-top: 15px;
    }
    .price-goods {
      display: flex;
      justify-content: space-between;
      margin-top: 10px;
      span {
        color: $color-main;
      }
    }
    .grab-btn {
      padding: 4px 12px;
      color: #fff;
      background: $color-main;
      border-radius: 4px;
      position: absolute;
      right: 5px;
      bottom: 32px;
      font-size: 14px;
    }
  }
</style>
