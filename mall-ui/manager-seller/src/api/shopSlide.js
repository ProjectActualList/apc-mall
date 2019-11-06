/**
 * 店铺幻灯片相关API
 */

import request from '@/utils/request'

/**
 * 获取店铺幻灯片列表
 * @param params
 * @returns {Promise<any>}
 */
export function getShopSlideList(params) {
  return request({
    url: 'seller/shops/sildes',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 保存店铺幻灯片列表（更新/添加操作）
 * @param params
 * @returns {Promise<any>}
 */
export function saveShopSlide(params) {
  return request({
    url: 'seller/shops/sildes',
    method: 'put',
    headers: { 'Content-Type': 'application/json' },
    data: params
  })
}

/**
 * 删除幻灯片
 * @param ids
 * @param params
 * @returns {Promise<any>}
 */
export function delShopSlide(ids, params) {
  return request({
    url: `seller/shops/sildes/${ids}`,
    method: 'delete',
    params
  })
}
