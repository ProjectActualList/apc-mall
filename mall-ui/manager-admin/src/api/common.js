/**
 * 公共API
 */

import { api } from '~/ui-domain'
import request from '@/utils/request'
import Storage from '@/utils/storage'

/**
 * 获取图片验证码URL
 * @param scene
 * @param uuid
 * @returns {string}
 */
export function getValidateCodeUrl(scene, uuid) {
  return `${api.base}/captchas/${uuid}/${scene}?rmd=${new Date().getTime()}`
}

/**
 * 刷新token
 */
export function refreshToken() {
  return request({
    url: 'admin/systems/admin-users/token',
    method: 'post',
    data: {
      refresh_token: Storage.getItem('admin_refresh_token')
    }
  })
}

/**
 * 获取首页数据
 */
export function getIndexData() {
  return request({
    url: 'admin/index/page',
    method: 'get',
    loading: false
  })
}
