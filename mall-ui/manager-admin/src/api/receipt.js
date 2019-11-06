/**
 * 发票相关API
 */

import request from '@/utils/request'

/**
 * 获取发票历史
 * @param params
 */
export function getHistoryReceiptList(params) {
  return request({
    url: 'admin/members/receipts',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取历史发票详情
 * @param history_id
 */
export function getHistoryReceiptDetail(history_id) {
  return request({
    url: `admin/members/receipts/${history_id}/detail`,
    method: 'get'
  })
}
