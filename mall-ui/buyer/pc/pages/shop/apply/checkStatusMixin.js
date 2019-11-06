/**
 * Created by Andste on 2018-12-03.
 */

import * as API_Shop from '@/api/shop'
export default {
  async mounted() {
    const res = await API_Shop.getApplyShopInfo()
    console.log(res)
    if (res['shop_disable'] === 'APPLY') {
      this.$router.replace({path: '/shop/apply'})
    }
  }
}
