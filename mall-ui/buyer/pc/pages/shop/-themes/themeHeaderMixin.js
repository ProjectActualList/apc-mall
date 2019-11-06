/**
 * Created by Andste on 2018/7/4.
 * 店铺头部Mixin
 */

import storage from '@/utils/storage'
import * as API_Shop from '@/api/shop'
import * as API_Members from "@/api/members"

export default {
  props: ['shop'],
  data() {
    return {
      shop_id: this.shop.shop_id,
      // 店铺分类【分组】
      categorys: [],
      // 店铺导航
      navs: [],
      // 是否已收藏
      is_collection: false
    }
  },
  mounted() {
    /** 获取店铺分类【分组】 */
    API_Shop.getShopCategorys(this.shop_id).then(response => {
      let categorys = response.filter(item => item.disable === 1)
      categorys = categorys.map(item => {
        item.children = item.children.filter(_item => _item.disable === 1)
        return item
      })
      this.categorys = categorys
    })
    /** 获取店铺导航 */
    API_Shop.getShopNav(this.shop_id).then(response => {
      this.navs = response
    })
    // 如果已登录，获取是否已收藏店铺
    if (storage.getItem('refresh_token')) {
      API_Members.getShopIsCollect(this.shop_id).then(response => {
        this.is_collection = response.message
      })
    }
  },
  methods: {
    /** 收藏店铺 */
    collectionShop() {
      if (!storage.getItem('refresh_token')) {
        this.$message.error('未登录不能收藏店铺！')
        return false
      }
      if (this.is_collection) {
        API_Members.deleteShopCollection(this.shop_id).then(() => {
          this.is_collection = false
          this.$message.success('取消收藏成功！')
        })
      } else {
        API_Members.collectionShop(this.shop_id).then(() => {
          this.is_collection = true
          this.$message.success('收藏成功！')
        })
      }
    }
  }
}

