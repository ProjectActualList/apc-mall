/**
 * 店铺相关API
 */

import request from '@/utils/request'

/**
 * 获取店铺列表
 * @param params
 * @returns {Promise<any>}
 */
export function getShopList(params) {
  return request({
    url: 'admin/shops',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取店铺详情
 * @param shop_id
 */
export function getShopDetail(shop_id) {
  return request({
    url: `admin/shops/${shop_id}`,
    method: 'get'
  })
}

/**
 * 关闭店铺
 * @param shop_id
 */
export function closeShop(shop_id) {
  return request({
    url: `admin/shops/disable/${shop_id}`,
    method: 'put'
  })
}

/**
 * 恢复店铺
 * @param shop_id
 */
export function recoverShop(shop_id) {
  return request({
    url: `admin/shops/enable/${shop_id}`,
    method: 'put'
  })
}

/**
 * 修改审核店铺
 * @param shop_id
 * @param params
 */
export function editAuthShop(shop_id, params) {
  return request({
    url: `admin/shops/${shop_id}`,
    method: 'put',
    data: params
  })
}

/**
 * 获取店铺模板列表
 * @param params
 * @returns {Promise<any>}
 */
export function getShopThemeList(params) {
  return request({
    url: 'admin/shops/themes',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 添加店铺模板
 * @param params
 * @returns {*}
 */
export function addShopTheme(params) {
  return request({
    url: 'admin/shops/themes',
    method: 'post',
    data: params
  })
}

/**
 * 获取店铺模板详情
 * @param id
 * @returns {Promise<any>}
 */
export function getShopThemeDetail(id) {
  return request({
    url: `admin/shops/themes/${id}`,
    method: 'get'
  })
}

/**
 * 修改店铺模板
 * @param id
 * @param params
 * @returns {*}
 */
export function editShopTheme(id, params) {
  return request({
    url: `admin/shops/themes/${id}`,
    method: 'put',
    data: params
  })
}

/**
 * 删除店铺模板
 * @param id
 * @returns {*}
 */
export function deleteShopTheme(id) {
  return request({
    url: `admin/shops/themes/${id}`,
    method: 'delete'
  })
}
