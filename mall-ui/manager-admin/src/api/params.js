/**
 * 参数相关API
 */

import request from '@/utils/request'

/**
 * 添加参数组
 * @param params
 * @returns {*}
 */
export function addParamsGroup(params) {
  return request({
    url: 'admin/goods/parameter-groups',
    method: 'post',
    data: params
  })
}

/**
 * 修改参数组
 * @param group_id
 * @param params
 * @returns {*}
 */
export function editParamsGroup(group_id, params) {
  return request({
    url: `admin/goods/parameter-groups/${group_id}`,
    method: 'put',
    data: params
  })
}

/**
 * 删除参数组
 * @param group_id
 * @returns {*}
 */
export function deleteParamsGroup(group_id) {
  return request({
    url: `admin/goods/parameter-groups/${group_id}`,
    method: 'delete'
  })
}

/**
 * 参数组排序
 * @param group_id
 * @param sort_type
 * @returns {*}
 */
export function sortParamsGroup(group_id, sort_type) {
  return request({
    url: `admin/goods/parameter-groups/${group_id}/sort`,
    method: 'put',
    data: { sort_type }
  })
}

/**
 * 获取参数组详情
 * @param id
 * @returns {*}
 */
export function getParamsGroupDetail(id) {
  return request({
    url: `admin/goods/parameter-groups/${id}`,
    method: 'get'
  })
}

/**
 * 添加参数
 * @param params
 * @returns {*}
 */
export function addParams(params) {
  return request({
    url: 'admin/goods/parameters',
    method: 'post',
    data: params
  })
}

/**
 * 编辑参数
 * @param id
 * @param params
 * @returns {*}
 */
export function editParams(id, params) {
  return request({
    url: `admin/goods/parameters/${id}`,
    method: 'put',
    data: params
  })
}

/**
 * 删除参数
 * @param id
 * @returns {*}
 */
export function deleteParams(id) {
  return request({
    url: `admin/goods/parameters/${id}`,
    method: 'delete'
  })
}

/**
 * 参数排序
 * @param param_id
 * @param sort_type
 * @returns {*}
 */
export function sortParams(param_id, sort_type) {
  return request({
    url: `admin/goods/parameters/${param_id}/sort`,
    method: 'put',
    data: { sort_type }
  })
}

/**
 * 获取参数详情
 * @param id
 * @returns {*}
 */
export function getParamDetail(id) {
  return request({
    url: `admin/goods/parameters/${id}`,
    method: 'get'
  })
}
