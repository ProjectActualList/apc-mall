/**
 * 分类管理API
 */
import request from '@/utils/request'

/**
 * 添加分类
 * @param params
 */
export function addCategory(params) {
  const _params = {
    name: params.category_name,
    parent_id: params.parent_id,
    category_order: params.category_order,
    image: params.category_image
  }
  return request({
    url: 'admin/goods/categories',
    method: 'post',
    data: _params
  })
}

/**
 * 编辑分类
 * @param id
 * @param params
 */
export function editCategory(id, params) {
  const _params = {
    name: params.category_name,
    parent_id: params.parent_id,
    category_order: params.category_order,
    image: params.category_image
  }
  return request({
    url: `admin/goods/categories/${id}`,
    method: 'put',
    data: _params
  })
}

/**
 * 根据分类ID获取分类关联品牌数据
 * @param category_id
 * @returns {Promise<any>}
 */
export function getBrandByCategoryId(category_id) {
  return request({
    url: `admin/goods/categories/${category_id}/brands`,
    method: 'get'
  })
}

/**
 * 编辑分类关联品牌
 * @param category_id 分类ID
 * @param choose_brands 品牌数组
 */
export function editCategoryBrand(category_id, choose_brands) {
  return request({
    url: `admin/goods/categories/${category_id}/brands`,
    method: 'put',
    data: { choose_brands }
  })
}

/**
 * 根据分类ID获取分类关联规格数据
 * @param category_id
 * @returns {Promise<any>}
 */
export function getSpecsByCategoryId(category_id) {
  return request({
    url: `admin/goods/categories/${category_id}/specs`,
    method: 'get'
  })
}

/**
 * 编辑分类关联规格
 * @param category_id 分类ID
 * @param choose_specs 规格数组
 * @returns {*}
 */
export function editCategorySpecs(category_id, choose_specs) {
  return request({
    url: `admin/goods/categories/${category_id}/specs`,
    method: 'put',
    data: { choose_specs }
  })
}

/**
 * 删除分类
 * @param ids
 * @returns {*}
 */
export function deleteCategory(ids) {
  if (Array.isArray(ids)) ids = ids.join(',')
  return request({
    url: `admin/goods/categories/${ids}`,
    method: 'delete'
  })
}

/**
 * 获取分类关联的参数
 * @param category_id 分类ID
 * @returns {*}
 */
export function getCategoryParams(category_id) {
  return request({
    url: `admin/goods/categories/${category_id}/param`,
    method: 'get'
  })
}

/**
 * 获取分类子集
 * @param parent_id
 */
export function getCategoryChildren(parent_id = 0) {
  return request({
    url: `admin/goods/categories/${parent_id}/children`,
    method: 'get',
    loading: false
  })
}
