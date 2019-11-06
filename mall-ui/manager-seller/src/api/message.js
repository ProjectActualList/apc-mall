/**
 * 消息相关API
 */

import request from '@/utils/request'

/**
 * 获取消息列表
 * @param params
 * @returns {Promise<any>}
 */
export function getMsgsList(params) {
  return request({
    url: 'seller/shops/shop-notice-logs',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 删除消息
 * @param ids
 * @param params
 * @returns {Promise<any>}
 */
export function deleteMsgs(ids) {
  return request({
    url: `seller/shops/shop-notice-logs/${ids}`,
    method: 'delete',
    loading: false
  })
}

/**
 * 标记消息为已读
 * @param ids
 * @param params
 * @returns {Promise<any>}
 */
export function signMsgs(ids) {
  return request({
    url: `seller/shops/shop-notice-logs/${ids}/read`,
    method: 'put',
    loading: false
  })
}
