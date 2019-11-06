/**
 * 店铺导航相关API
 */

import request from '@/utils/request'

/**
 * 获取店铺导航列表
 * @param params
 * @returns {Promise<any>}
 */
export function getShopNavList(params) {
  return request({
    url: 'seller/shops/navigations',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 添加店铺导航
 * @param params
 * @returns {Promise<any>}
 */
export function addShopNav(params) {
  return request({
    url: 'seller/shops/navigations',
    method: 'post',
    data: params
  })
}

/**
 * 修改店铺导航
 * @param id
 * @param params
 * @returns {Promise<any>}
 */
export function editShopNav(id, params) {
  return request({
    url: `seller/shops/navigations/${id}`,
    method: 'put',
    data: params
  })
}

/**
 * 删除店铺导航
 * @param id
 * @param params
 * @returns {Promise<any>}
 */
export function delShopNav(id, params) {
  return request({
    url: `seller/shops/navigations/${id}`,
    method: 'delete',
    params
  })
}
