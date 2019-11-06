/**
 * 快递公司相关API
 */

import request from '@/utils/request'

/**
 * 获取物流公司列表
 * @param params
 */
export function getExpressList(params) {
  return request({
    url: 'admin/systems/logi-companies',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 添加物流公司
 * @param params
 */
export function addExpress(params) {
  return request({
    url: 'admin/systems/logi-companies',
    method: 'post',
    data: params
  })
}

/**
 * 删除物流公司
 * @param id
 */
export function deleteExpress(id) {
  return request({
    url: 'admin/systems/logi-companies',
    method: 'delete'
  })
}

/**
 * 获取物流公司详情
 * @param id
 */
export function getExpressDetail(id) {
  return request({
    url: `admin/systems/logi-companies/${id}`,
    method: 'get'
  })
}

/**
 * 修改物流公司
 * @param id
 * @param params
 */
export function editExpress(id, params) {
  return request({
    url: `admin/systems/logi-companies/${id}`,
    method: 'put',
    data: params
  })
}
