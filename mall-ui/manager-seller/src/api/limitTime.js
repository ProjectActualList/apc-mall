/**
 * 限时活动相关API
 */

import request from '@/utils/request'

/**
 * 获取限时活动列表
 * @param params
 * @returns {Promise<any>}
 */
export function getLimitTimeActivityList(params) {
  return request({
    url: 'seller/promotion/seckill-applys/seckill',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取限时活动商品列表
 * @param params
 * @returns {Promise<any>}
 */
export function getLimitTimeGoodsList(params) {
  return request({
    url: 'seller/promotion/seckill-applys',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取限时活动详情
 * @param ids
 * @param params
 * @returns {Promise<any>}
 */
export function getLimitTimeActivityDetails(ids, params) {
  return request({
    url: `seller/promotion/seckill-applys/${ids}/seckill`,
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 限时活动报名
 * @param ids
 * @param params
 * @returns {Promise<any>}
 * @constructor
 */
export function signUpLimitTimeActivity(params) {
  return request({
    url: 'seller/promotion/seckill-applys',
    method: 'post',
    loading: false,
    headers: { 'Content-Type': 'application/json' },
    data: params
  })
}

