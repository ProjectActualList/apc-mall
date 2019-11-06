/**
 * 团购商品相关API
 */

import request from '@/utils/request'

/**
 * 获取团购商品列表
 * @param params
 * @returns {Promise<any>}
 */
export function getGroupBuyGoodsList(params) {
  return request({
    url: 'seller/promotion/group-buy-goods',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取团购分类列表
 * @param params
 * @returns {Promise<any>}
 */
export function getGroupCateGoriesList() {
  return request({
    url: 'seller/promotion/group-buy-cats',
    method: 'get',
    loading: false
  })
}

/**
 * 删除团购商品
 * @param ids
 * @param params
 * @returns {Promise<any>}
 */
export function deleteGroupBuyGoods(ids, params) {
  return request({
    url: `seller/promotion/group-buy-goods/${ids}`,
    method: 'delete',
    loading: false,
    params
  })
}

/**
 * 保存团购商品详情 更新操作
 * @param ids
 * @param params
 * @returns {Promise<any>}
 */
export function saveGroupBuyGoods(ids, params) {
  return request({
    url: `seller/promotion/group-buy-goods/${ids}`,
    method: 'put',
    loading: false,
    headers: { 'Content-Type': 'application/json' },
    data: params
  })
}

/**
 * 新增团购商品
 * @param params
 * @returns {Promise<any>}
 */
export function addGroupBuyGoods(params) {
  return request({
    url: 'seller/promotion/group-buy-goods',
    method: 'post',
    loading: false,
    headers: { 'Content-Type': 'application/json' },
    data: params
  })
}

/**
 * 获取一个团购商品详情
 * @param ids
 * @param params
 * @returns {Promise<any>}
 */
export function getGroupBuyGoodsDetails(ids, params) {
  return request({
    url: `seller/promotion/group-buy-goods/${ids}`,
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取团购活动列表 （待审核状态可显示）
 * @param params
 * @returns {Promise<any>}
 */
export function getGroupBuyActivityList(params) {
  return request({
    url: 'seller/promotion/group-buy-goods/active',
    method: 'get',
    loading: false,
    params
  })
}

