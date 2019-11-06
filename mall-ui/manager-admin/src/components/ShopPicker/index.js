/**
 * 店铺选择组件
 * 返回shop_id
 */
import Vue from 'vue'
import ShopPicker from './src/main'

ShopPicker.install = () => {
  Vue.component(ShopPicker.name, ShopPicker)
}

export default ShopPicker
