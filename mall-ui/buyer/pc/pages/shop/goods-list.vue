<template>
  <div id="shop-goods-list" class="container">
    <template v-if="shop">
      <theme1-header v-if="shop.shop_theme_path === 'pc_1'" :shop="shop"/>
      <theme2-header v-if="shop.shop_theme_path === 'pc_2'" :shop="shop"/>
      <theme3-header v-if="shop.shop_theme_path === 'pc_3'" :shop="shop"/>
    </template>
    <div class="sort-nav">
      <div class="inner w">
        <div class="left">
          <div class="inner-sort">
            <template v-for="sort in sorts">
              <div class="item" :key="sort.title" :class="[sort.active && 'active', sort.type === 'asc' ? 'up' : 'down']" @click="handleSortChanged(sort)">
                {{ sort.title }}<i class="iconfont ea-icon-arrow-down2"></i>
              </div>
            </template>
          </div>
          <div class="inner-price">
            ￥
            <el-input-number size="mini" :controls="false" v-model="params.min_price"/>
             -
            <el-input-number size="mini" :controls="false" v-model="params.max_price"/>
            <el-button size="mini" class="price-btn" @click="handlePriceConfirm">确认</el-button>
          </div>
        </div>
        <div class="inner-search">
          <el-input style="width: 75%;" placeholder="在当前结果中搜索" size="mini" clearable v-model="params.keyword" @keyup.enter.native="handleSearch('shop')">
            <el-button slot="append" icon="el-icon-search" @click="handleSearch('shop')"></el-button>
          </el-input>
          <el-button size="mini" type="danger" class="search-btn" @click="handleSearch('all')">搜全站</el-button>
        </div>
      </div>
    </div>
    <div class="goods-container">
      <div class="inner w">
        <div v-if="goods && !goods.data.length" style="text-align: center;line-height: 100px">暂无商品</div>
        <ul v-else class="goods-list">
          <li v-for="goods in goods.data" :key="goods.goods_id" class="goods-item">
            <div class="goods-image">
              <a :href="'/goods/' + goods.goods_id">
                <img :src="goods.thumbnail" :alt="goods.name" :title="goods.name">
              </a>
            </div>
            <div class="goods-name">
              <a :href="'/goods/' + goods.goods_id">{{ goods.name }}</a>
            </div>
            <div class="goods-price">
              <span>RMB：<span class="price">￥<strong>{{ goods.price | unitPrice }}</strong></span></span>
              <span>已销售：{{ goods.buy_count }}件</span>
            </div>
            <div class="goods-grade">好评率：<span>{{ goods.grade }}%</span></div>
          </li>
          <span class="clr"></span>
        </ul>
        <div v-if="goods" class="goods-pagination">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page.sync="params.page_no"
            :page-size="params.page_size"
            layout="total, prev, pager, next"
            :total="goods.data_total">
          </el-pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_Shop from '@/api/shop'
  import * as API_Goods from '@/api/goods'
  import { Foundation } from '@/ui-utils'
  const theme1Header = () => import('@/pages/shop/-themes/-theme1-header')
  const theme2Header = () => import('@/pages/shop/-themes/-theme2-header')
  const theme3Header = () => import('@/pages/shop/-themes/-theme3-header')
  import { Pagination, Input, InputNumber } from 'element-ui'
  Vue.use(Pagination).use(Input).use(InputNumber)
  export default {
    name: 'shop-goods-list',
    validate({ query }) {
      return /^\d+$/.test(query.shop_id)
    },
    async asyncData({ query }) {
      const shop = await API_Shop.getShopBaseInfo(query.shop_id)
      return { shop }
    },
    components: { theme1Header, theme2Header, theme3Header },
    data() {
      return {
        sorts: [
          { title: '默认', name: 'def', type: 'asc', active: true },
          { title: '销量', name: 'buynum', type: 'asc', active: false },
          { title: '价格', name: 'price', type: 'asc', active: false },
          { title: '好评率', name: 'grade', type: 'asc', active: false },
        ],
        shop: '',
        goods: '',
        params: {
          page_no: 1,
          page_size: 16,
          ...this.$route.query
        }
      }
    },
    mounted() {
      this.GET_GoodsList()
    },
    methods: {
      /** 当前页数发生改变 */
      handleCurrentChange(page) {
        this.params.page_no = page
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
        this.params.sort = `${sort.name}_${sort.type}`
        this.GET_GoodsList()
      },
      /** 价格区间确认 */
      handlePriceConfirm() {
        const { min_price, max_price } = this.params
        if (min_price > max_price) {
          this.$message.error('价格区间格式有误！')
          return false
        }
        this.GET_GoodsList()
      },
      /** 商品搜索【店内、全站】 */
      handleSearch(type) {
        if (type === 'all') {
          this.$router.push({ path: '/goods', query: { keyword: this.params.keyword } })
        } else {
          this.GET_GoodsList()
        }
      },
      /** 获取店铺商品列表 */
      GET_GoodsList() {
        const params = JSON.parse(JSON.stringify(this.params))
        if (params.shop_id) {
          params.seller_id = params.shop_id
          delete  params.shop_id
        }
        if (params.min_price || params.max_price) {
          params.price = [params.min_price, params.max_price].join('_')
          delete params.min_price
          delete params.max_price
        }
        API_Goods.getGoodsList(params).then(response => {
          this.goods = response
          this.MixinScrollToTop()
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  img { width: 100%; height: 100% }
  .container {
    background-color: #F7F7F7;
  }
  .sort-nav {
    width: 100%;
    .inner { border-bottom: 1px solid #D8D8D8 }
    .left {
      display: flex;
      align-items: center;
      height: 100%
    }
    .inner, .inner-sort {
      display: flex;
      align-items: center;
      justify-content: space-between;
      height: 44px;
      background-color: #F9FCFA;
    }
    .inner-sort .item {
      display: flex;
      align-items: center;
      padding: 0 10px;
      height: 25px;
      border-right: 1px solid #ccc;
      font-size: 14px;
      cursor: pointer;
      &.active { color: $color-main }
      &.down .iconfont { transform: rotate(0) scale(.8) }
      &:last-child { border-right: 0 }
      .iconfont {
        font-size: 12px;
        margin: 0 5px;
        color: $color-main;
        transform: rotate(180deg) scale(.8);
      }
    }
    .inner-price {
      margin-left: 35px;
      /deep/ .el-input-number {
        width: 80px;
      }
      .price-btn { margin-left: 5px }
    }
    .inner-search {
      display: flex;
      margin-right: 30px;
      .search-btn { margin-left: 10px }
    }
  }
  .goods-container {
    .inner {
      background-color: #fff;
    }
  }
  .goods-pagination {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    height: 50px;
  }
  $s_sep: 30px;
  $g_width: (1210px - $s_sep * 5) / 4;
  .goods-list { padding: 10px $s_sep }
  .goods-item {
    float: left;
    width: $g_width;
    min-height: 300px;
    margin-right: $s_sep;
    margin-bottom: 20px;
    &:nth-child(4n) { margin-right: 0 }
  }
  .goods-image {
    width: $g_width;
    height: $g_width;
  }
  .goods-name {
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    overflow: hidden;
    min-height: 36px;
  }
  .goods-price {
    display: flex;
    justify-content: space-between;
    strong { font-size: 16px }
  }
  .goods-grade {
    span {
      color: $color-main
    }
  }
</style>
