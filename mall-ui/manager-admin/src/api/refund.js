/**
 * 退款单API
 */

import request from '@/utils/request'

/**
 * 获取退款单列表
 * @param params
 */
export function getRefundList(params) {
  return request({
    url: 'admin/after-sales/refund',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取售后详情
 * @param sn
 */
export function getRefundDetail(sn) {
  return request({
    url: `admin/after-sales/refund/${sn}`,
    method: 'get',
    loading: false
  })
}

/**
 * 平台退款
 * @param sn
 * @param params
 */
export function refundMoney(sn, params) {
  return request({
    url: `admin/after-sales/refunds/${sn}`,
    method: 'post',
    data: params
  })
}

/**
 * 导出Excel
 * @param params
 */
export function exportRefundExcel(params) {
  return request({
    url: 'admin/after-sales/exports/excel',
    method: 'get',
    params
  })
}
