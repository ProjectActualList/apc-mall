/**
 * 订单相关API
 */

import request from '@/utils/request'

/**
 * 获取订单列表
 * @param params
 * @returns {Promise<any>}
 */
export function getOrderList(params) {
  return request({
    url: 'seller/trade/orders',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 根据订单sn获取订单详情
 * @param sn
 * @returns {Promise<any>}
 */
export function getOrderDetail(sn) {
  return request({
    url: `seller/trade/orders/${sn}`,
    method: 'get',
    loading: false
  })
}

/**
 * 调整价格
 * @param sn
 * @returns {Promise<any>}
 */
export function updateOrderPrice(sn, params) {
  return request({
    url: `seller/trade/orders/${sn}/price`,
    method: 'put',
    loading: false,
    data: params
  })
}

/**
 * 修改收货人信息
 * @param sn
 * @param params
 * @returns {Promise<any>}
 */
export function updateConsigneeInfo(sn, params) {
  return request({
    url: `seller/trade/orders/${sn}/address`,
    method: 'put',
    loading: false,
    data: params
  })
}

/**
 * 确认收款
 * @param sn
 * @param params
 * @returns {Promise<any>}
 */
export function confirmGetAmount(sn, params) {
  return request({
    url: `seller/trade/orders/${sn}/pay`,
    method: 'post',
    loading: false,
    data: params
  })
}

/**
 * 发货
 * @param ids
 * @param params
 * @returns {Promise<any>}
 */
export function deliveryGoods(sn, params) {
  return request({
    url: `seller/trade/orders/${sn}/delivery`,
    method: 'post',
    loading: false,
    data: params
  })
}

/**
 * 查询快递物流信息
 * @param params
 * @returns {Promise<any>}
 */
export function getLogisticsInfo(params) {
  return request({
    url: `seller/express`,
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 生成电子面单
 * @param params
 * @returns {Promise<any>}
 */
export function generateElectronicSurface(params) {
  return request({
    url: `seller/waybill`,
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取订单流程图数据
 * @param ids
 * @param params
 * @returns {Promise<any>}
 */
export function getStepList(ids) {
  return request({
    url: `seller/trade/orders/${ids}/flow`,
    method: 'get',
    loading: false
  })
}

