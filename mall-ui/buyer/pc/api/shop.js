/**
 * 店铺相关API
 */

import request, { Method } from '@/utils/request'

/**
 * 获取店铺列表
 * @param params
 */
export function getShopList(params) {
  return request({
    url: 'shops/list',
    method: Method.GET,
    params
  })
}

/**
 * 会员初始化店铺信息
 */
export function initApplyShop() {
  return request({
    url: 'shops/apply',
    method: Method.POST,
    needToken: true
  })
}

/**
 * 获取店铺信息
 */
export function getApplyShopInfo() {
  return request({
    url: 'shops/apply',
    method: Method.GET,
    needToken: true
  })
}

/**
 * 会员申请开店步骤
 * @param step
 * @param params
 */
export function applyShopStep(step, params) {
  return request({
    url: `shops/apply/step${step}`,
    method: Method.PUT,
    needToken: true,
    data: params
  })
}

/**
 * 获取店铺基本信息
 * @param shop_id
 */
export function getShopBaseInfo(shop_id) {
  return request({
    url: `shops/${shop_id}`,
    method: Method.GET,
    loading: false
  })
}

/**
 * 获取店铺幻灯片
 * @param shop_id
 */
export function getShopSildes(shop_id) {
  return request({
    url: `shops/sildes/${shop_id}`,
    method: Method.GET,
    loading: false
  })
}

/**
 * 获取店铺导航
 * @param shop_id
 */
export function getShopNav(shop_id) {
  return request({
    url: `shops/navigations/${shop_id}`,
    method: Method.GET,
    loading: false
  })
}

/**
 * 获取店铺分类【分组】
 * @param shop_id
 */
export function getShopCategorys(shop_id) {
  return request({
    url: `shops/cats/${shop_id}`,
    method: Method.GET,
    loading: false
  })
}
