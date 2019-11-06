<template>
  <div id="group-buy">
    <nav-bar title="拼团" fixed/>
    <van-tabs @click="handleClickCate">
      <van-tab
        v-for="(cate, index) in categories"
        :title="cate.name"
        :key="index"
      >
      </van-tab>
    </van-tabs>
    <en-empty v-if="finished && !assemble.length" style="line-height:420px">暂无商品</en-empty>
    <van-list
      v-model="loading"
      :finished="finished"
      @load="onLoad"
      class="group-buy-container"
    >
      <li v-for="(goods, index) in assemble" :key="index" class="item-goods">
        <dt>
          <a :href="`/goods/${goods.goods_id}?sku_id=${goods.sku_id}&from_nav=assemble`">
            <img :src="goods.thumbnail" :alt="goods.goods_name">
          </a>
        </dt>
        <dd>
          <a :href="`/goods/${goods.goods_id}?sku_id=${goods.sku_id}&from_nav=assemble`">
            <div class="name-goods">{{ goods.goods_name }}</div>
          </a>
          <div class="assembld">
            已团{{ goods.buy_count }}件
          </div>
          <div class="descrip-goods">
            <p class="price">
              <span>￥{{ goods.sales_price | unitPrice }}</span>
              <span class="original_price">￥{{ goods.origin_price | unitPrice }}</span>
            </p>
            <a :href="`/goods/${goods.goods_id}?sku_id=${goods.sku_id}&from_nav=assemble`" class="grab-btn">去开团</a>
          </div>
        </dd>
      </li>
    </van-list>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_Promotions from '@/api/promotions'
  import * as API_Goods from '@/api/goods'
  export default {
    name: 'assemble',
    head() {
      return {
        title: `拼团-${this.site.site_name}`
      }
    },
    async asyncData() {
      // 获取商品分类
      let categories = await API_Goods.getCategory()
      categories.unshift({ category_id: 0, name: '全部', active: true })
      return { categories }
    },
    data() {
      return {
        loading: false,
        finished: false,
        params: {
          page_no: 0,
          page_size: 10,
          category_id: 0
        },
        assemble: []
      }
    },
    methods: {
      /** 加载数据 */
      onLoad(page_no) {
        this.params.page_no += 1
        this.GET_AssembleGoods()
      },
      /** 选择团购分类 */
      handleClickCate(index) {
        const { categories } = this
        const cate = categories[index]
        this.$set(this, 'categories', categories.map(item => {
          item.active = item.category_id === cate.category_id
          return item
        }))
        this.finished = false
        this.assemble = []
        this.params.page_no = 1
        this.params.category_id = cate.category_id
        this.GET_AssembleGoods()
      },
      /** 获取拼团商品 */
      GET_AssembleGoods() {
        this.loading = true
        const params = JSON.parse(JSON.stringify(this.params))
        if (params.category_id === 0) delete params.category_id
        API_Promotions.getAssembleList(params).then(response => {
          this.loading = false
          if (!response || !response.length) {
            this.finished = true
          } else {
            this.assemble.push(...response)
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
    /* 已团n件 */
    .assembld {
      text-align: left;
      color: #cccccc;
    }
    .descrip-goods {
      padding-top: 15px;
      display: flex;
      flex-direction: row;
      flex-wrap: nowrap;
      justify-content: space-between;
      align-items: center;
      .price {
        font-size: 12px;
        color: #f42424;
        /*原价*/
        .original_price {
          text-decoration:line-through;
          display: inline-block;
          margin-left: 10px;
        }
      }
      .grab-btn {
        padding: 1px 9px;
        color: #fff;
        background: $color-main;
        border-radius: 4px;
        font-size: 14px;
      }
    }
  }
</style>







