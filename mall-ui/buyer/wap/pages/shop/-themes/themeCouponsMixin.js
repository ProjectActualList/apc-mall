/**
 * Created by Andste on 2018/7/22.
 */

import * as API_Promotions from '@/api/promotions'
import * as API_Members from '@/api/members'
import Storage from '@/utils/storage'

export default {
  props: ['shop-id'],
  data() {
    return {
      coupons: ''
    }
  },
  mounted() {
    /** 获取店铺优惠券列表 */
    API_Promotions.getShopCoupons(this.shopId).then(response => {
      this.coupons = response
    })
  },
  methods: {
    /** 领取优惠券 */
    handleReceiveCoupon(coupon) {
      if (!Storage.getItem('refresh_token')) {
        this.$message.error('请先登录！')
        return false
      }
      API_Members.receiveCoupons(coupon.coupon_id).then(() => {
        this.$message.success('领取成功！')
      })
    },
  }
}
