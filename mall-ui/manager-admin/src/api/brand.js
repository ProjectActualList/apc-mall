/**
 * 品牌管理API
 */

import request from '@/utils/request'

/**
 * 获取品牌列表
 * @param params
 * @returns {Promise<any>}
 */
export function getBrandList(params) {
  return request({
    url: 'admin/goods/brands',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 添加品牌
 * @param params
 */
export function addBrand(params) {
  return request({
    url: 'admin/goods/brands',
    method: 'post',
    data: params
  })
}

/**
 * 获取品牌详情
 * @param id
 */
export function getBrandDetail(id) {
  return request({
    url: `admin/goods/brands/${id}`,
    method: 'get'
  })
}

/**
 * 编辑品牌
 * @param id
 * @param params
 */
export function editBrand(id, params) {
  return request({
    url: `admin/goods/brands/${id}`,
    method: 'put',
    data: params
  })
}

/**
 * 删除品牌
 * @param ids
 * @returns {Promise<any>}
 */
export function deleteBrand(ids) {
  if (Array.isArray(ids)) ids = ids.join(',')
  return request({
    url: `admin/goods/brands/${ids}`,
    method: 'delete'
  })
}

