<template>
  <div id="shop-goods-list" class="container" style="background-color: #f7f7f7">
    <shop-header :shop="shop"/>
    <div class="search-box">
      <form method="get" action="/" @submit.prevent="handleOnSubmit">
        <input v-model="params.keyword" type="text" maxlength="20" placeholder="想找什么商品？" style="color: rgb(153, 153, 153);">
        <button type="submit">店内搜</button>
        <button type="button" @click="handleSearchAll">全站搜</button>
      </form>
    </div>
    <div class="sort-box">
      <div
        v-for="(sort, index) in sorts"
        :key="index"
        class="item-sort"
        :class="[sort.active && 'active', sort.type]"
        @click="handleSortChanged(sort)"
      >
        <span>{{ sort.title }}</span>
        <i class="iconfont ea-icon-arrow-down3"></i>
      </div>
    </div>
    <en-empty v-if="finished && !goodsList.length">暂无商品</en-empty>
    <van-list
      v-else
      v-model="loading"
      :finished="finished"
      @load="onLoad"
      class="goods-container"
    >
      <ul>
        <li v-for="(goods, index) in goodsList" :key="index" class="item-goods">
          <div class="goods-list-left">
            <a :href="'/goods/' + goods.goods_id">
              <img :src="goods.thumbnail" :alt="goods.name">
            </a>
          </div>
          <div class="goods-list-right">
            <h3><a :href="'/goods/' + goods.goods_id">{{ goods.name }}</a></h3>
            <p>￥{{ goods.price | unitPrice }}</p>
          </div>
          <div class="goods-list-other">
            <div>
              <span>销量：</span>
              <span class="price">{{ goods.buy_count }}件</span>
            </div>
            <div>
              <span>好评率：</span>
              <span class="price">{{ goods.grade }}%</span>
            </div>
          </div>
        </li>
      </ul>
    </van-list>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_Shop from '@/api/shop'
  import * as API_Goods from '@/api/goods'
  import { Foundation } from '@/ui-utils'
  import ShopHeader from '@/pages/shop/-themes/-shop-header'
  export default {
    name: 'shop-goods-list',
    validate({ query }) {
      return /^\d+$/.test(query.shop_id)
    },
    async asyncData({ query }) {
      const shop = await API_Shop.getShopBaseInfo(query.shop_id)
      return { shop }
    },
    components: { ShopHeader },
    data() {
      return {
        loading: false,
        finished: false,
        sorts: [
          { title: '默认', name: 'def', type: 'asc', active: true },
          { title: '销量', name: 'buynum', type: 'asc', active: false },
          { title: '价格', name: 'price', type: 'asc', active: false },
          { title: '好评率', name: 'grade', type: 'asc', active: false },
        ],
        shop: '',
        goodsList: [],
        params: {
          page_no: 0,
          page_size: 10,
          keyword: '',
          ...this.$route.query
        }
      }
    },
    watch: {
      $route() {
        this.finished = false
        this.goodsList = []
        this.params = {
          ...this.$route.query
        }
        this.params.page_no = 1
        this.GET_GoodsList()
      }
    },
    methods: {
      /** 加载数据 */
      onLoad() {
        this.params.page_no += 1
        this.GET_GoodsList()
      },
      /** 排序发生改变 */
      handleSortChanged(sort) {
        if (sort.active) {
          sort.type = sort.type === 'asc' ? 'desc' : 'asc'
        }
        this.$set(this, 'sorts', this.sorts.map(item => {
          item.active = item.name === sort.name
          return item
        }))
        this.params.page_no = 1
        this.finished = false
        this.goodsList = []
        this.params.sort = `${sort.name}_${sort.type}`
        this.GET_GoodsList()
      },
      /** 提交表单之拦截 */
      handleOnSubmit() {
        delete this.params.category
        delete this.params.sort
        this.params.page_no = 1
        this.finished = false
        this.goodsList = []
        if (!this.params.keyword) delete this.params.keyword
        this.GET_GoodsList()
      },
      /** 搜全站 */
      handleSearchAll() {
        const { keyword } = this.params
        this.$router.push({ name: 'goods', query: { keyword } })
      },
      /** 获取店铺商品列表 */
      GET_GoodsList() {
        this.loading = true
        const params = JSON.parse(JSON.stringify(this.params))
        params.seller_id = params.shop_id
        delete params.shop_id
        if (!params.keyword) delete params.keyword
        if (!params.category || params.category === '0') delete params.category
        API_Goods.getGoodsList(params).then(response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            this.goodsList.push(...data)
          }
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .search-box {
    width: 90%;
    height: 2 * 16px;
    padding: 8px 5%;
    background: #fff;
    input {
      float: left;
      width: 55%;
      border: 1px solid #dedede;
      height: 1.6 * 16px;
      line-height: 1.6 * 16px;
      padding: 1px 5px;
      box-sizing: border-box;
    }
    button {
      width: 22%;
      float: left;
      height: 1.6 * 16px;
      line-height: 1.6 * 16px;
      text-align: center;
      background: #3487ed;
      color: #fff;
      font-size: 0.8 * 16px;
      font-weight: 400;
      border: 1px solid #3487ed;
      &:last-child {
        background: #ff4a00;
        border: 1px solid #ff4a00;
      }
    }
  }
  .sort-box {
    display: flex;
    justify-content: space-between;
    width: 100%;
    overflow: hidden;
    background-color: #fff;
    .item-sort {
      width: 25%;
      text-align: center;
      line-height: 25px;
      color: #53a0be;
      &.active {
        color: $color-main;
      }
      &.desc .ea-icon-arrow-down3 {
        transform: rotate(0);
      }
      .ea-icon-arrow-down3 {
        display: inline-block;
        font-size: 12px;
        transform: rotate(180deg);
      }
    }
  }
  .goods-container {
    padding: 8px 2% 0.8 * 16px 2%;
    position: relative;
    border-top: 1px solid #ECECEC;
    overflow: hidden;
    margin: 0 0 2.2 * 16px 0;
    .item-goods {
      float: left;
      background: #fff;
      width: 48%;
      margin: 0 2% 8px 0;
      overflow: hidden;
      position: relative;
      padding: 0.6 * 16px 0;
      box-sizing: border-box;
    }
    .goods-list-left {
      width: 90%;
      margin: 0 auto;
      max-width: 200px;
      img {
        display: block;
        margin: 0 auto;
        width: 156px;
        height: 156px;
      }
    }
    .goods-list-right {
      margin: 0 5%;
      h3 {
        height: 3 * 16px;
        line-height: 1.3 * 16px;
        overflow: hidden;
        text-align: left;
        a {
          font-size: 16px;
          font-weight: 400;
          min-height: 32px;
          color: #53a0be;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          overflow: hidden;
        }
      }
      p {
        width: 100%;
        height: 16px;
        line-height: 16px;
        margin: 0.3 * 16px 0;
        text-align: right;
        font-size: 16px;
      }
    }
    .goods-list-other {
      padding: 0 10px;
    }
  }
</style>
