/**
 * 规格管理API
 */

import request from '@/utils/request'

/**
 * 获取规格列表
 * @param params
 */
export function getSpecs(params) {
  return request({
    url: 'admin/goods/specs',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 添加规格
 * @param params
 */
export function addSpec(params) {
  return request({
    url: 'admin/goods/specs',
    method: 'post',
    data: params
  })
}

/**
 * 编辑规格
 * @param id
 * @param params
 */
export function eidtSpec(id, params) {
  return request({
    url: `admin/goods/specs/${id}`,
    method: 'put',
    data: params
  })
}

/**
 * 删除规格
 * @param ids
 * @returns {*}
 */
export function deleteSpecs(ids) {
  if (Array.isArray(ids)) ids = ids.join(',')
  return request({
    url: `admin/goods/specs/${ids}`,
    method: 'delete'
  })
}

/**
 * 获取规格值
 * @param spec_id 规格ID
 * @returns {*}
 */
export function getSpecValues(spec_id) {
  return request({
    url: `admin/goods/specs/${spec_id}/values`,
    method: 'get'
  })
}

/**
 * 保存规格值
 * @param spec_id
 * @param value_list
 * @returns {Promise<any>}
 */
export function saveSpecValues(spec_id, value_list) {
  return request({
    url: `admin/goods/specs/${spec_id}/values`,
    method: 'post',
    data: { value_list }
  })
}
