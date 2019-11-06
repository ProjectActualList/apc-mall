<template>
  <div>
    <van-nav-bar left-arrow @click-left="MixinRouterBack">
      <div slot="title">
        <div class="search-box" @click="showSearch = true">
          <van-icon name="search"/>
          <span>{{ params.keyword || '搜索商品' }}</span>
        </div>
      </div>
      <header-shortcut slot="right"/>
    </van-nav-bar>
    <search :show="showSearch" @close="showSearch = false"/>
    <div class="sort-box">
      <div class="inner-sort">
        <a
          v-for="(sort, index) in sorts"
          :key="index"
          :class="[sort.active && 'active']"
          @click="handleClickSort(sort)"
          href="javascript:"
          class="item"
        >
          <span>{{ sort.title }}<i :class="[sort.type]"></i></span>
        </a>
      </div>
    </div>
    <van-list v-model="loading" :finished="finished" @load="onLoad" class="goods-list">
      <a :href="'/goods/' + goods.goods_id" v-for="(goods, index) in goodsList" :key="index" class="goods-item">
        <div class="goods-image">
          <img :src="goods.thumbnail" :alt="goods.name">
        </div>
        <div class="goods-info">
          <div class="goods-name">{{ goods.name }}</div>
          <div class="goods-price">
            <em>￥</em>{{ goods.price | unitPrice('', 'before') }}<em>.{{ goods.price | unitPrice('', 'after') }}</em>
          </div>
          <div class="goods-other">
            <img src="../../assets/images/self.png" v-if="goods.self_operated === 1" class="selfBox" />
            <span>{{ goods.buy_count || 0 }}人已购买</span>
            <span>&nbsp;{{ goods.comment_num || 0 }}条评论</span>
          </div>
          <div class="goods-shop">
            <nuxt-link :to="'/shop/' + goods.seller_id"><van-icon name="shop"/>&nbsp;{{ goods.seller_name }}</nuxt-link>
          </div>
        </div>
      </a>
    </van-list>
    <div v-if="finished && !goodsList.length" class="empty-list">
      <img src="../../assets/images/icon-empty-member.png" alt="">
      <p>暂无商品</p>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import { List } from 'vant'
  Vue.use(List)
  import * as API_Goods from '@/api/goods'
  export default {
    name: 'goods-list',
    data() {
      let { price, sort } = this.$route.query
      sort = sort ? sort.split('_') : ['def', 'asc']
      return {
        // 显示搜索盒子
        showSearch: false,
        // 显示快捷导航盒子
        showShortcutBox: false,
        // 加载中
        loading: false,
        // 是否加载完成
        finished: false,
        // 商品列表
        goodsList: [],
        // 筛选参数
        params: {
          page_no: 0,
          page_size: 20,
          ...this.$route.query
        },
        // 排序
        sorts: [
          { title: '默认', name: 'def', type: 'asc' },
          { title: '销量', name: 'buynum', type: 'asc' },
          { title: '价格', name: 'price', type: 'asc' },
          { title: '好评率', name: 'grade', type: 'asc' },
        ].map(item => {
          item.active = item.name === sort[0]
          if (item.name === sort[0]) {
            item.type = sort[1]
          }
          return item
        })
      }
    },
    beforeRouteUpdate (to, from, next) {
      const { ...props } = to.query
      this.params = props
      this.onLoad()
      next()
    },
    methods: {
      /** 加载数据 */
      onLoad() {
        this.params.page_no += 1
        this.GET_GoodsList()
      },
      /** 排序 */
      handleClickSort(sort) {
        this.goodsList = []
        this.params.page_no = 1
        this.finished = false
        if (sort.active) sort.type = sort.type === 'asc' ? 'desc' : 'asc'
        this.$set(this, 'sorts', this.sorts.map(item => {
          item.active = item.name === sort.name
          return item
        }))
        this.params.sort = `${sort.name}_${sort.type}`
        this.GET_GoodsList()
      },
      /** 获取商品列表 */
      GET_GoodsList() {
        const { params } = this
        Object.keys(params).forEach(key => {
          if (!params[key]) delete params[key]
        })
        this.loading = true
        API_Goods.getGoodsList(params).then(response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            this.goodsList.push(...data)
          }
        }).catch(() => {
          this.loading = false
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  /deep/ {
    .van-nav-bar {
      position: fixed;
      z-index: 100 !important;
      top: 0;
      left: 0;
      right: 0;
    }
    .van-nav-bar__left .van-nav-bar__arrow { color: #666 }
    .van-nav-bar__title { max-width: 75% }
    .search-box {
      display: -webkit-box;
      width: 100%;
      border: none;
      border-radius: 15px;
      height: 30px;
      overflow: hidden;
      background: #f7f7f7;
      font-size: 12px;
      -webkit-box-align: center;
      line-height: 30px;
      position: relative;
      margin-top: 8px;
      .van-icon-search {
        color: #B7B7B7;
        margin: 0 10px;
        font-size: 16px;
      }
    }
  }
  .sort-box {
    position: fixed;
    z-index: 99;
    height: 40px;
    background-color: #fff;
    top: 46px;
    left: 0;
    right: 0;
    .inner-sort {
      font-size: 14px;
      display: flex;
      position: relative;
      padding: 8px 0;
      height: 24px;
      line-height: 24px;
      text-align: center;
    }
    .item {
      display: block;
      width: 100%;
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      padding: 0 5px;
      position: relative;
      span {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        box-sizing: border-box;
        position: relative;
        display: inline-block;
        max-width: 100%;
        padding-right: 13px;
      }
      i {
        position: absolute;
        right: 0;
        top: 50%;
        margin-top: -1px;
        width: 8px;
        height: 5px;
        background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAKBAMAAABPkMOvAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAkUExURUdwTMzMzMzMzMzMzMzMzM3NzdLS0svLy6qqqszMzMzMzMzMzGC86tIAAAALdFJOUwDl9UGdJhG9A2jOR0lNjwAAAEdJREFUCNdj8N4NBlsYVkMYuxgCIQxRBmZtEL3JgIFhNoixk4GBgQXEcAAyOKp3797eAGQwJO7eLQaiGVi1NwWAGQyGwkACAKGfIfA79uAMAAAAAElFTkSuQmCC) no-repeat;
        background-size: 8px auto;
        transform: rotate(180deg);
        &.desc { transform: rotate(0) }
      }
      &.active {
        color: $color-main;
        i {
          background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAKCAYAAAC9vt6cAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MTFBRkQzNUE4RUE5MTFFNjlGOEZBM0ZCRDkxNTQ3ODgiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MEEzQkVCNkM4RUFGMTFFNjlGOEZBM0ZCRDkxNTQ3ODgiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDoxMUFGRDM1ODhFQTkxMUU2OUY4RkEzRkJEOTE1NDc4OCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDoxMUFGRDM1OThFQTkxMUU2OUY4RkEzRkJEOTE1NDc4OCIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PjuQXsYAAACYSURBVHjaYnliabOUgYEhFIhZGUgDv4F4NROQ0CBDMwNUjxbIgHAgfkOGAa9BLgcZcAdqyC8SNIPURoD0MkEF9gFxAQkG5EP1MDAhCU4H4mlEaJ4KxDNgHCY0yTyYyTjAPqjtDLgM+AvEIdBwQQd3oHJ/8RkAAu+B2BeIPyKJfYSKvUdXzITDqTeAOBCIX0BxAFQMAwAEGABzlx8yrIQ8yQAAAABJRU5ErkJggg==)
        }
      }
    }
  }
  .goods-list {
    padding-top: 46px + 40px
  }
  .goods-item {
    display: flex;
    position: relative;
    padding: 5px 0;
  }
  .goods-image {
    width: 120px;
    height: 120px;
    margin-left: 10px;
    flex-shrink: 0;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .goods-info {
    width: 100%;
    min-height: 120px;
    padding: 10px;
    box-sizing: border-box;
    .goods-name {
      font-size: 14px;
      color: #333;
      line-height: 19px;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      height: 38px;
    }
    .goods-price {
      color: #f42424;
      font-size: 18px;
      font-weight: 400;
      margin-top: 13px;
      em {
        font-size: 12px;
      }
    }
    .goods-other{ color: #999 }
    .goods-shop {

    }
  }
  .empty-list {
    display: block;
    text-align: center;
    margin-top: 86px;
    padding-top: 50px;
  }
  .selfBox{
    display: inline-block;
    width: 24px;
    height: 14px;
    margin-top: -2px;
    margin-right: 5px;
    vertical-align: middle;
  }
</style>
