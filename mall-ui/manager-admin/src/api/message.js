/**
 * 消息设置相关API
 * 消息模板
 * 站内消息
 */

import request from '@/utils/request'

/**
 * 获取消息模板列表
 * @param params
 */
export function getMessageTemplate(params) {
  return request({
    url: 'admin/systems/message-templates',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 编辑消息模板
 * @param id
 * @param params
 */
export function editMessageTemplate(id, params) {
  return request({
    url: `admin/systems/message-templates/${id}`,
    method: 'put',
    data: params
  })
}

/**
 * 获取站内消息列表
 * @param params
 */
export function getMessageList(params) {
  return request({
    url: 'admin/systems/messages',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 发布新的站内消息
 * @param params
 */
export function releaseMessage(params) {
  return request({
    url: 'admin/systems/messages',
    method: 'post',
    data: params
  })
}
