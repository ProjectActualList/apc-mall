/**
 * 物流公司相关API
 */

import request from '@/utils/request'

/**
 * 获取物流公司列表
 * @param params
 * @returns {Promise<any>}
 */
export function getExpressCompanyList(params) {
  return request({
    url: 'seller/shops/logi-companies',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 开启 物流公司
 * @param ids
 * @param params
 * @returns {Promise<any>}
 */
export function openExpressPower(ids, params) {
  return request({
    url: `seller/shops/logi-companies/${ids}`,
    method: 'post',
    loading: false,
    data: params
  })
}

/**
 * 关闭 物流公司
 * @param ids
 * @param params
 * @returns {Promise<any>}
 */
export function closeExpressPower(ids, params) {
  return request({
    url: `seller/shops/logi-companies/${ids}`,
    method: 'delete',
    loading: false,
    params
  })
}
