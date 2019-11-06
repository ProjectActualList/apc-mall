/**
 * 系统设置
 */

import request from '@/utils/request'

/**
 * 获取站点设置
 */
export function getSiteSetting() {
  return request({
    url: 'admin/settings/site',
    method: 'get',
    loading: false
  })
}

/**
 * 修改站点设置
 * @param params
 */
export function editSiteSetting(params) {
  return request({
    url: 'admin/settings/site',
    method: 'put',
    data: params
  })
}

/**
 * 获取积分设置
 */
export function getPointSetting() {
  return request({
    url: 'admin/settings/point',
    method: 'get'
  })
}

/**
 * 修改积分设置
 * @param params
 */
export function editPointSetting(params) {
  return request({
    url: 'admin/settings/point',
    method: 'put',
    data: params
  })
}
