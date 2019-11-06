<template>
  <div style="background: #f7f7f7">
    <van-nav-bar
      fixed
      left-arrow
      title="购物车"
      @click-left="MixinRouterBack"
    >
      <header-shortcut slot="right"/>
    </van-nav-bar>
    <en-empty v-if="!shopList || !shopList.length">暂无货品</en-empty>
    <div v-else class="cart-container">
      <div class="shop-item" v-for="(shop, index) in shopList" :key="index">
        <div class="inner-shop-item">
          <div class="title-shop-item">
            <div v-if="shop.invalid === 1" style="width: 10%"></div>
            <div v-else class="shop-check" @click="handleCheckShop(shop)">
              <van-icon :name="shop.checked ? 'checked' : 'check'"/>
            </div>
            <div class="shop-name">{{ shop.seller_name }}</div>
            <div class="shop-other">
              <div class="receive-coupons">
                <nuxt-link :to="'./cart/coupons?shop_id=' + shop.seller_id">领券</nuxt-link>
              </div>
            </div>
          </div>
          <div class="promotion-notice">{{ shop.promotion_notice }}</div>
          <div class="content-shop-item">
            <div
              v-for="(sku, index) in shop.sku_list"
              :key="index"
              class="sku-item-li"
              :class="[sku.invalid === 1 && 'invalid']"
            >
              <div v-if="sku.error_message" class="error-message">{{ sku.error_message }}</div>
              <van-swipe-cell :right-width="90">
                <div class="sku-item">
                  <div v-if="sku.invalid === 1" class="sku-check">已失效</div>
                  <div v-else class="sku-check" @click="handleCheckSku(sku)">
                    <van-icon :name="sku.checked ? 'checked' : 'check'"/>
                  </div>
                  <a :href="'/goods/' + sku.goods_id" class="sku-image">
                    <img :src="sku.goods_image" :alt="sku.name">
                    <span v-if="!sku.is_ship" class="out-of-stock-tip">该地区无货</span>
                  </a>
                  <div class="sku-content">
                    <a :href="'/goods/' + sku.goods_id" class="sku-name">{{ sku.name }}</a>
                    <div v-if="sku.spec_list" class="sku-spec">
                      <span>{{ sku | formatterSkuSpec }}</span>
                    </div>
                    <p v-if="sku.promotion_tags && sku.promotion_tags.length">
                      <span class="sku-act-tag" v-for="(tag, _index) in sku.promotion_tags" :key="'tag_' + _index">{{ tag }}</span>
                    </p>
                    <div class="sku-tools">
                      <div class="sku-price">
                        <strong>￥{{ sku.purchase_price | unitPrice }}</strong>
                      </div>
                      <div v-if="sku.invalid !== 1" class="sku-num">
                        <a class="sku-symbol symbol-less minus unable" href="javascript:;" @click.stop="handleUpdateSkuNum(sku, '-')">-</a>
                        <input type="tel" class="sku-num-input" size="4" :value="sku.num" maxlength="4" @change="handleUpdateSkuNum(sku, $event)">
                        <a class="sku-symbol symbol-add add" href="javascript:;" @click.stop="handleUpdateSkuNum(sku, '+')">+</a>
                      </div>
                    </div>
                  </div>
                </div>
                <span slot="right" class="del-sku" @click="handleDelete(sku)">删除</span>
              </van-swipe-cell>
              <div class="act-box">
                <div
                  v-if="sku.single_list && sku.single_list.length"
                  @click="handleChangeActivity(sku)"
                  class="act-item"
                >
                  <div>
                    <span class="act-item-title">促销</span>
                    <span class="act-item-cont">{{ selectedActivity(sku.single_list) }}</span>
                  </div>
                  <div v-if="sku.single_list.length > 1" class="act-item-select">
                    <span>{{ sku.single_list.length }}个可选</span>
                    <i
                      class="iconfont"
                      :class="[
                      cur_sku_id === sku.sku_id
                      ? 'ea-icon-arrow-up'
                      : 'ea-icon-arrow-down'
                      ]"
                    ></i>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="cat-bottom-bar">
      <div class="inner-bar">
        <div class="all-check" @click="handleCheckAll">
          <van-icon :name="all_checked ? 'checked' : 'check'"/>全选
        </div>
        <a
          v-show="checkedCount"
          href="javascript:void(0)"
          class="del-btn"
          @click="handleBatchDelete"
        >删除已选</a>
        <div class="cat-bar-price">
          <span>合计：</span>
          <span class="price">¥{{ cartTotal.total_price | unitPrice }}</span>
          <div v-if="cartTotal.cash_back" class="back-price">
            返现：<span class="price">-￥{{ cartTotal.cash_back | unitPrice }}</span>
          </div>
        </div>
        <van-button
          type="default"
          :disabled="!checkedCount"
          @click="handleCheckout"
        >去结算</van-button>
      </div>
    </div>
    <van-actionsheet
      v-model="showActivityActionsheet"
      :actions="activity_options"
      @select="handleActivitySelect"
      cancel-text="取消"
    />
  </div>
</template>

<script>
  import { mapActions, mapGetters } from 'vuex'
  import * as API_Trade from '@/api/trade'
  import { RegExp } from '@/ui-utils'
  export default {
    name: 'cart-index',
    middleware: 'auth-user',
    head() {
      return {
        title: `购物车-${this.site.site_name}`
      }
    },
    data() {
      return {
        /** 当前操作的输入框的值【变化之前】 */
        current_input_value: 1,
        activity_options: [],
        showActivityActionsheet: false,
        cur_sku_id: null
      }
    },
    watch: {
      showActivityActionsheet(newVal) {
        newVal === false && (this.cur_sku_id = null)
      }
    },
    mounted() {
      !this.shopList.length && this.getCartData()
    },
    computed: {
      // 是否全部选中
      all_checked() {
        return !!this.checkedCount && this.checkedCount === this.allCount
      },
      ...mapGetters({
        shopList: 'cart/shopList',
        skuList: 'cart/skuList',
        checkedCount: 'cart/checkedCount',
        allCount: 'cart/allCount',
        cartTotal: 'cart/cartTotal'
      })
    },
    methods: {
      /** 勾选、取消勾选商品 */
      handleCheckSku(sku) {
        this.checkSkuItem({ sku_id: sku.sku_id, checked: sku.checked ? 0 : 1 })
      },
      /** 勾选、取消勾选店铺 */
      handleCheckShop(shop) {
        this.checkShopSku({ shop_id: shop.seller_id, checked: shop.checked ? 0 : 1 })
      },
      /** 全选、取消全选 */
      handleCheckAll() {
        this.checkAll(this.all_checked ? 0 : 1)
      },
      /** 打开促销选择 */
      handleChangeActivity(sku) {
        const options = sku.single_list.map(item => ({
          name: item.title,
          activity_id: item.activity_id,
          is_check: item.is_check,
          promotion_type: item.promotion_type,
          className: item.is_check === 1 ? 'checked' : '',
          sku
        }))
        const no_act = sku.single_list.filter(item => item.is_check === 1)
        options.push({
          name: '不参加活动',
          className: no_act[0] ? '' : 'checked',
          sku
        })
        this.activity_options = options
        this.showActivityActionsheet = true
        this.cur_sku_id = sku.sku_id
      },
      /** 展示已选择促销*/
      selectedActivity(list) {
        const _list = list.filter(item => item.is_check === 1)
        if (_list[0]) {
          return _list[0].title
        }
        return '不参加活动'
      },
      /** 确认促销选择 */
      handleActivitySelect(item) {
        const { seller_id, single_list = [], sku_id } = item.sku
        if (item.name === '不参加活动') {
          this.cleanActivity({seller_id, sku_id})
        } else {
          const { promotion_type, activity_id } = item
          const params = {
            seller_id,
            sku_id,
            activity_id,
            promotion_type
          }
          this.changeActivity(params)
        }
        this.showActivityActionsheet = false
      },
      /** 更新商品数量 */
      handleUpdateSkuNum(sku, symbol) {
        if (typeof symbol !== 'string') {
          let _num = parseInt(symbol.target.value)
          if (isNaN(_num) || _num < 1) {
            this.$message.error('输入格式有误，请输入正整数！')
            return
          }
          if (_num >= sku.enable_quantity) {
            this.$message.error('超过最大库存！')
            return
          }
          this.updateSkuNum({sku_id: sku.sku_id, num: _num})
        } else {
          if (symbol === '-' && sku.num < 2) return
          if (symbol === '+' && sku.num >= sku.enable_quantity) {
            this.$message.error('超过最大库存！')
            return
          }
          let _num = symbol === '+' ? sku.num + 1 : sku.num - 1
          this.updateSkuNum({sku_id: sku.sku_id, num: _num})
        }
      },
      /** 输入框被焦点【记录当前值】 */
      handleSkuNumFocus(sku) {
        this.current_input_value = sku.num
      },
      /** 输入框值发生改变 */
      handleSkuNumChanged(event, sku) {
        const _value = event.target.value
        if (!RegExp.integer.test(_value)) {
          this.$message.error('您的输入不合法！')
          event.target.value = this.current_input_value
          return
        }
        if (Number(_value) === sku.num) return
        this.current_input_value = sku.num
        this.updateSkuNum({sku_id: sku.sku_id, num: Number(_value)})
      },
      /** 删除 */
      handleDelete(sku) {
        this.$confirm('确定要删除这个货品吗？', () => {
          this.deleteSkuItem(sku.sku_id).then(response => {
            this.$message.success('删除成功！')
          })
        })
      },
      /** 批量删除 */
      handleBatchDelete() {
        this.$confirm('确定要删除这些货品吗？', () => {
          // 筛选出已选中的
          const _sku_list = this.skuList.filter(item => item.checked).map(item => item.sku_id)
          this.deleteSkuItem(_sku_list).then(response => {
            this.$message.success('删除成功！')
          })
        })
      },
      /** 清空购物车 */
      handleCleanCart() {
        this.$confirm('确定要清空购物车吗？', () => {
          this.cleanCart().then(() => this.$message.success('购物车已清空！'))
        })
      },
      /** 去结算 */
      handleCheckout() {
        if (!this.checkedCount) return false
        this.$router.push({ path: '/checkout' })
      },
      /** vuex/cart */
      ...mapActions({
        // 获取购物车数据
        getCartData: 'cart/getCartDataAction',
        // 更新货品数量
        updateSkuNum: 'cart/updateSkuNumAction',
        // 选择、取消选择货品
        checkSkuItem: 'cart/checkSkuItemAction',
        // 选择、取消选择店铺内所有商品
        checkShopSku: 'cart/checkShopSkuAction',
        // 全选、取消全选
        checkAll: 'cart/checkAllAction',
        // 删除货品
        deleteSkuItem: 'cart/deleteSkuItemAction',
        // 清空购物车
        cleanCart: 'cart/cleanCartAction',
        // 更换促销活动
        changeActivity: 'cart/changeActivityAction',
        // 不参加活动
        cleanActivity: 'cart/cleanActivityAction',
      })
    }
  }
</script>



<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .cart-container {
    padding: 46px 0 50px 0;
  }
  .shop-item {
    padding: 10px;
  }
  .inner-shop-item {
    background-color: #fff;
    overflow: hidden;
  }
  .title-shop-item {
    display: flex;
    position: relative;
    width: 100%;
    height: 44px;
    background-color: #fff;
  }
  .shop-check {
    display: flex;
    justify-content: space-around;
    align-items: center;
    width: 10%;
    height: 100%;
  }
  .shop-name {
    width: 60%;
    height: 100%;
    background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAADo0lEQVR4Xu1Z7VEUQRDtjUCJQIlAiECJQI1AicC+CMQI7hGBGIEagRCBEIEYgRDBWm+qx5pab3d6llv2rnb6DwU319P9+vXX0MjCpVm4/1IBqAxYOAI1BRZOgFoEawrUFFg4AjUFFk6AcV1AVS9E5N2OgfcFwPtSm4pTQFWfisgvEeHPXZI7ETkEwJ9uGQMAUf5sN5y4b5r24A9TfwqA7HTLGAC+ichrERlFObdlBQdVNdr0HcCbgq+W1QBVfW705x1vAfDi2UVV6fRXM+SgJA2KGKCqKiJrEbkHsFM1QFWZ+09EZAUA3qiUAvBTRI52if7R0aQzXQM43joAHfofA7j2XvIY51SVgWGAKOwGt5573QxQVdLqg4j8BsBasHOiqnT6mYicA2C6ZqUEAPZ+Ov4JwFlW8wwHkiDdAjj0mOACQFVfiUjstW56eQzY5pkxaeoFII6+NwCYazsrqsra9MJbqL0A/LHRt6jFzIFS0qrvABzkbMgC8JAhI3f5FJ/brsKAUbLDmgeA0WPmFA56dJaMxh4AWrs0u2hYsXxprXJwKbGCFVdq7hWDfVtVuYSxxV0BuBwCws6GhQ3AoI8eANwjZoJ8dhrrdJYTh1NxCs0uPEkdyM4sHgBiClwC6F1/N7wTDLbLzqPK4GbZaW/ZvV9V2bLZurNgeQBIZ4DeLqCqpFz6IsN2xMj+90DRiX5k88bx2oClQ2n7vQBwuikNkujz4yyzsgBQS0LtkFY2aoactRn8o4jEPfzcRmZ+TBAIWshZc4bjdJwkb8wJ9m0CFXUH0Awobp/R+VQ3mcmpNOwkxhLqjiNwNvr8nhcArr50goZGoZHdlTiMyWkRGjhP58kuikd3KMKqSvAIeCpdW4Juz7uAC4B4k11OhLl3p8ILz9IHEmMGI8qukMq9RZpre4w0gaRjTKGu7itGNd0+bTbh+TQgvCPoLtlVigBIgGDkaDQd4MTVuxobNblEhfND1d5ShHSPurnU9LZHA5lns7o7Qfj36ygA+pTt499HA7BarQK127blglT0FL0toMiYpmlCGqzXa6ZKsYwGQFXjhJhtNcVWOb+QttPcxLf1FKgAVAbUFKg1wApLLYK5VdZZ1IuP1S6QvFbXNph5+alzQA8CD54Em6bhdjjX/wmP2rYN/wmeMwWKi9cUX6gAPHYNmCKKc+gcXQPmMHaKOysAU6C6TzorA/YpWlPYWhkwBar7pLMyYJ+iNYWtlQFToLpPOhfPgL/iyMtQ1HAvqQAAAABJRU5ErkJggg==') no-repeat left center;
    background-size: 26px;
    background-position-y: 10px;
    line-height: 44px;
    padding-left: 30px;
    font-size: 14px;
    overflow: hidden;
  }
  .shop-other {
    width: 30%;
    line-height: 44px;
    text-align: right;
  }
  .receive-coupons {
    display: inline-block;
    height: 44px;
    margin-right: 10px;
    a {
      border-left: 1px solid #999;
      font-size: 14px;
      padding-left: 5px;
    }
  }
  .shop-check , .sku-check {
    .van-icon {
      font-size: 18px;
      &.van-icon-checked {
        color: $color-main
      }
    }
  }
  .sku-item {
    position: relative;
    display: flex;
    width: 100%;
    min-height: 90px;
    padding: 5px 0;
  }
  .del-sku {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 90px;
    height: 100%;
    color: #fff;
    background-color: $color-main;
  }
  .sku-check {
    display: flex;
    justify-content: space-around;
    align-items: center;
    width: 15%;
  }
  .sku-image {
    position: relative;
    width: 25%;
    height: 100%;
    overflow: hidden;
    img {
      width: 80px;
      height: 80px;
      margin-top: 5px;
      border: 1px solid #e3e5e9;
    }
    .out-of-stock-tip {
      text-align: center;
      display: block;
      position: absolute;
      bottom: 0;
      left: 0;
      width: 80px;
      height: 20px;
      background-color: rgba(0,0,0,0.7);
      color: #fff;
    }
  }
  .sku-content {
    width: 58%;
    height: 100%;
    padding-right: 10px;
    .sku-name {
      display: -webkit-box;
      text-overflow: ellipsis;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      word-break: break-all;
      font-size: 13px;
      height: 30px;
      margin: 3px 0;
      line-height: 15px;
      overflow: hidden;
    }
    .sku-spec {
      width: 100%;
      min-height: 27px;
      select, option {
        font-size: 12px;
      }
    }
    .sku-tools {
      position: relative;
      height: 24px;
      margin-top: 2px;
    }
    .sku-price {
      color: $color-main;
      font-size: 13px;
      line-height: 24px;
      strong {
        font-style: normal;
        font-size: 16px;
        margin-left: 2px;
        font-weight: normal;
      }
    }
    .sku-num {
      display: flex;
      position: absolute;
      top: 0;
      right: 0;
      width: 84px;
      box-sizing: border-box;
      white-space: nowrap;
      height: 100%;
      font-size: 0;
      border: 1px solid #cbcbcb;
      border-radius: 3px;
      .sku-symbol {
        width: 25px;
        height: 22px;
      }
      .symbol-less {
        background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAXklEQVRYR+3VsQ3AMAhEUd88NPFEWS0jJcXNg9tIkdwRu/gMAMcTEmqLS4vnNwIggAACCCCwn0BEXJLOii+Zmbft/u79ESgO8Ng+pgEqNp/13O8GEEAAAQQQQOBvgQE/TBAhHZBqPwAAAABJRU5ErkJggg==') no-repeat center;
        background-size: 70%;
        border-right: 1px solid #cbcbcb;
      }
      .symbol-less.disabled {
        background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAX0lEQVRYR+3VwQ2AQAhEUaYjDfRjaxbEQSvCq4nJ3nD38CkAhhcSZJNLk+cbARBAAAEEEFhPIDNPSUfHl6yqKyL2d++PQGcAM7vdfRsG6Nh81HO9G0AAAQQQQACBvwUewk4NITNiPgEAAAAASUVORK5CYII=') no-repeat center;
        background-size: 70%;
      }
      .symbol-add {
        background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAjklEQVRYR+2W0QmAMAxEzQS6Rwq6giO4aUdwBIXrPJH+iaLSllKE63fae3mBUOkaH2mc3xGgyICqehHpAcy5oywCcM5ZDAaQ/U72xRhMABqgARqoYkBVNxEZc1frxz0PYDnX3DZhTQAz20MI0ytASudVRkAAGqABGvibgdXMhut6TWmi6FecEvRUS4DmBg4+2Wgham7wbgAAAABJRU5ErkJggg==') no-repeat center;
        background-size: 70%;
        border-left: 1px solid #cbcbcb;
      }
      .symbol-add.disabled {
        background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAj0lEQVRYR+2W0QmAMAxErxPoJgrJAo7gph3BARrQiSr9E0WhLaUI1++09/ICoQ6dj+ucDwJUGQgheACDqi6lo6wCMLOYgkWk+J3iiymYADRAAzTQxICZ7QCm0tX6dS/G6FV1vdY8NmFLAACHiMyfADmdNxkBAWiABmjgbwY2AON9veY0UfUrzgl6qyVAdwMnrxRlIYH8bHYAAAAASUVORK5CYII=') no-repeat center;
        background-size: 70%;
      }
      .sku-num-input {
        border: none;
        outline: none;
        background: #fff;
        height: 22px;
        text-align: center;
        color: #232326;
        font-size: 12px;
        -webkit-appearance: none;
      }
    }
  }
  /deep/ .van-submit-bar {
    .all-check {
      display: flex;
      align-items: center;
      margin-left: 10px;
    }
    .van-icon-check, .van-icon-checked {
      font-size: 18px;
      margin-right: 3px;
    }
    .van-icon-checked {
      color: $color-main;
    }
    .del-btn {
      font-size: 12px;
      margin-left: 10px;
      color: #F44F44;
    }
  }
  /deep/ {
    .van-actionsheet {
      .checked {
        color: $color-main
      }
    }
  }
  .act-box {
    padding-left: 15%;
    margin-bottom: 5px;
  }
  .promotion-notice {
    padding-left: 15%;
    color: $color-main;
  }
  .act-item {
    display: flex;
    justify-content: space-between;
    position: relative;
    padding: 10px;
    margin-top: 5px;
    margin-right: 10px;
    background-color: #FDF3F3;
    border-radius: 2px;
    &::before {
      content: ' ';
      position: absolute;
      top: -5px;
      left: 15px;
      width: 10px;
      height: 10px;
      background-color: #FDF3F3;
      transform: rotate(45deg);
    }
  }
  .act-item-title {
    color: #333;
    font-size: 14px;
  }
  .act-item-cont {
    margin-left: 10px;
  }
  .act-item-cont, .act-item-select {
    color: #7f828b;
    font-size: 12px;
  }
  .act-item-select {
    .iconfont {
      vertical-align: -2px;
    }
  }
  .sku-item-li {
    border-bottom: 1px dashed #dcdcdc;
    &.invalid .sku-item {
      filter: grayscale(1);
    }
    &:last-child {
      border-bottom: none;
      .act-item {
        margin-bottom: 10px;
      }
    }
  }
  .cat-bottom-bar {
    left: 0;
    bottom: 0;
    width: 100%;
    z-index: 100;
    position: fixed;
    user-select: none;
    .inner-bar {
      height: 50px;
      display: flex;
      font-size: 14px;
      align-items: center;
      background-color: #fff;
    }
    /deep/ {
      .all-check {
        display: flex;
        align-items: center;
        margin-left: 10px;
      }
      .van-icon-check, .van-icon-checked {
        font-size: 18px;
        margin-right: 3px;
      }
      .van-icon-checked {
        color: $color-main;
      }
      .del-btn {
        font-size: 12px;
        margin-left: 10px;
        color: #F44F44;
      }
      .van-button {
        width: 110px;
        height: 100%;
        background-color: $color-main;
        color: #ffffff;
        border-color: $color-main;
        border-radius: 0;
        font-size: 16px;
        &.van-button--disabled {
          color: #999;
          background-color: #e8e8e8;
          border: none;
        }
      }
    }
    .cat-bar-price {
      flex: 1;
      font-weight: 500;
      text-align: right;
      color: #333;
      padding-right: 12px;
      span {
        display: inline-block;
      }
      .back-price {
        color: #777;
        font-size: 12px;
      }
    }
  }
  .error-message {
    color: red;
    padding-left: 15%;
  }
  .sku-act-tag {
    display: inline-block;
    padding: 0 5px;
    line-height: 15px;
    margin-right: 5px;
    border: 1px solid $color-main;
    color: $color-main
  }
</style>
