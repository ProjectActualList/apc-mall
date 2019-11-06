/**
 * 促销相关API
 */

import request from '@/utils/request'

const MockBaseUrl = 'https://www.easy-mock.com/mock/5c3d888e4ac38336de5939b9/'

/**
 * 获取积分分类下级
 * @param parent_id
 */
export function getExchangeCatsChildren(parent_id = 0) {
  return request({
    url: `admin/promotion/exchange-cats/${parent_id}/children`,
    method: 'get'
  })
}

/**
 * 添加积分兑换分类
 * @param params
 */
export function addExchangeCat(params) {
  return request({
    url: 'admin/promotion/exchange-cats',
    method: 'post',
    data: params
  })
}

/**
 * 获取积分兑换分类详情
 * @param id
 */
export function getExchangeCatsDetail(id) {
  return request({
    url: `admin/promotion/exchange-cats/${id}`,
    method: 'get'
  })
}

/**
 * 编辑积分兑换分类
 * @param id
 * @param params
 */
export function editExhcangeCat(id, params) {
  return request({
    url: `admin/promotion/exchange-cats/${id}`,
    method: 'put',
    data: params
  })
}

/**
 * 删除积分兑换分类
 * @param id
 */
export function deleteExchangeCat(id) {
  return request({
    url: `admin/promotion/exchange-cats/${id}`,
    method: 'delete'
  })
}

/**
 * 获取积分商品列表
 * @param params
 */
export function getExchangeGoods(params) {
  return request({
    url: 'admin/promotion/exchange-goods',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取团购活动列表
 * @param params
 */
export function getGroupBuyActives(params) {
  return request({
    url: 'admin/promotion/group-buy-actives',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 添加团购活动
 * @param params
 */
export function addGrouBuyActivity(params) {
  return request({
    url: 'admin/promotion/group-buy-actives',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: params
  })
}

/**
 * 获取团购活动商品列表
 * @param params
 */
export function getGroupBuyGoods(params) {
  return request({
    url: `admin/promotion/group-buy-goods`,
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取团购商品详情
 * @param gb_id
 */
export function getGroupBuyGoodsDetail(gb_id) {
  return request({
    url: `admin/promotion/group-buy-goods/${gb_id}`,
    method: 'get'
  })
}

/**
 * 编辑团购活动
 * @param id
 * @param params
 */
export function editGroupBuyActivity(id, params) {
  return request({
    url: `admin/promotion/group-buy-actives/${id}`,
    method: 'put',
    headers: { 'Content-Type': 'application/json' },
    data: params
  })
}

/**
 * 删除团购活动
 * @param id
 */
export function deleteGroupBuyActivity(id) {
  return request({
    url: `admin/promotion/group-buy-actives/${id}`,
    method: 'delete'
  })
}

/**
 * 审核团购商品
 * @param act_id
 * @param params
 */
export function reviewGroupBuyGoods(act_id, params) {
  return request({
    url: `admin/promotion/group-buy-actives/review/${act_id}`,
    method: 'post',
    data: params
  })
}

/**
 * 获取团购商品列表
 * @param params
 */
export function getGroupBuyGoodsList(params) {
  return request({
    url: `admin/promotion/group-buy-goods`,
    method: 'get'
  })
}

/**
 * 获取团购分类列表
 * @param params
 */
export function getGroupBuyCategory(params) {
  return request({
    url: 'admin/promotion/group-buy-cats',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 添加团购分类
 * @param params
 */
export function addGroupBuyCategory(params) {
  return request({
    url: 'admin/promotion/group-buy-cats',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: params
  })
}

/**
 * 获取团购详情
 * @param id
 */
export function getGroupBuyCategoryDetail(id) {
  return request({
    url: `admin/promotion/group-buy-cats/${id}`,
    method: 'get'
  })
}

/**
 * 编辑团购分类
 * @param id
 * @param params
 */
export function editGroupBuyCategory(id, params) {
  return request({
    url: `admin/promotion/group-buy-cats/${id}`,
    method: 'put',
    headers: { 'Content-Type': 'application/json' },
    data: params
  })
}

/**
 * 删除团购分类
 * @param id
 */
export function deleteGroupBuyCategory(id) {
  return request({
    url: `admin/promotion/group-buy-cats/${id}`,
    method: 'delete'
  })
}

/**
 * 获取限时抢购列表
 * @param params
 */
export function getSeckillList(params) {
  return request({
    url: 'admin/promotion/seckills',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 增加限时抢购
 * @param params
 */
export function addSeckill(params) {
  return request({
    url: 'admin/promotion/seckills',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: params
  })
}

/**
 * 获取限时抢购详情
 * @param id
 */
export function getSeckillDetail(id) {
  return request({
    url: `admin/promotion/seckills/${id}`,
    method: 'get'
  })
}

/**
 * 修改限时抢购
 * @param id
 * @param params
 */
export function editSeckill(id, params) {
  return request({
    url: `admin/promotion/seckills/${id}`,
    method: 'put',
    headers: { 'Content-Type': 'application/json' },
    data: params
  })
}

/**
 * 删除限时抢购
 * @param id
 */
export function deleteSeckill(id) {
  return request({
    url: `admin/promotion/seckills/${id}`,
    method: 'delete'
  })
}

/**
 * 发布限时抢购
 * @param seckill_id
 * @param params
 */
export function releaseSeckill(seckill_id, params) {
  return request({
    url: `admin/promotion/seckills/${seckill_id}/release`,
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: params
  })
}

/**
 * 查看限时抢购商品
 * @param params
 */
export function getSeckillGoods(params) {
  return request({
    url: `admin/promotion/seckill-applys`,
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 审核限时抢购商品
 * @param apply_id
 * @param status
 * @param fail_reason
 */
export function reviewSckillGoods(apply_id, status = 'yes', fail_reason) {
  return request({
    url: `admin/promotion/seckills/review/${apply_id}`,
    method: 'post',
    data: {
      status,
      fail_reason
    }
  })
}

/**
 * 获取拼团列表
 */
export function getAssembleList(params) {
  return request({
    url: 'admin/promotion/pintuan',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 关闭某个拼团活动
 * @param id
 */
export function deleteAssemble(id) {
  return request({
    url: `admin/promotion/pintuan/${id}/close`,
    method: 'put',
    loading: false
  })
}

/**
 * 开启某个拼团活动
 * @param id
 */
export function openAssemble(id) {
  return request({
    url: `admin/promotion/pintuan/${id}/open`,
    method: 'put',
    loading: false
  })
}

/**
 * 获取某个拼团活动信息的详情
 * @param id
 */
export function getAssembleDetail(id) {
  return request({
    url: `admin/promotion/pintuan/${id}`,
    method: 'get',
    loading: false
  })
}

/**
 * 获取拼团商品列表
 * @param id
 */
export function getAssembleGoodsList(id) {
  return request({
    url: `admin/promotion/pintuan/goods/${id}`,
    method: 'get',
    loading: false
  })
}

