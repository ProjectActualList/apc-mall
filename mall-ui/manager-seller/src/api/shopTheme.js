/**
 * 店铺模板相关API
 */

import request from '@/utils/request'

/**
 * 获取店铺模板列表
 * @param params
 * @returns {Promise<any>}
 */
export function getShopThemeList(params) {
  return request({
    url: 'seller/shops/themes',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 保存店铺模板
 * @param id
 * @param params
 * @returns {Promise<any>}
 */
export function saveShopTheme(id, params) {
  return request({
    url: `seller/shops/themes/${id}`,
    method: 'put',
    params
  })
}

