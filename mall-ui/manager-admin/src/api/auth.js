/**
 * 权限管理相关API
 */

import request from '@/utils/request'
import md5 from 'js-md5'

/**
 * 获取管理员列表
 * @param params
 */
export function getAdministratorList(params) {
  return request({
    url: 'admin/systems/manager/admin-users',
    method: 'get',
    loaidng: false,
    params
  })
}

/**
 * 添加管理员
 * @param params
 */
export function addAdministrator(params) {
  return request({
    url: 'admin/systems/manager/admin-users',
    method: 'post',
    data: params
  })
}

/**
 * 获取管理员详情
 * @param id
 */
export function getAdministratorDetail(id) {
  return request({
    url: `admin/systems/manager/admin-users/${id}`,
    method: 'get'
  })
}

/**
 * 修改管理员
 * @param id
 * @param parmas
 */
export function editAdministrator(id, parmas) {
  return request({
    url: `admin/systems/manager/admin-users/${id}`,
    method: 'put',
    data: parmas
  })
}

/**
 * 删除管理员
 * @param id
 */
export function deleteAdministrator(id) {
  return request({
    url: `admin/systems/manager/admin-users/${id}`,
    method: 'delete'
  })
}

/**
 * 获取角色列表
 * @param params
 */
export function getRoleList(params) {
  return request({
    url: 'admin/systems/roles',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 删除角色
 * @param id
 */
export function deleteRole(id) {
  return request({
    url: `admin/systems/roles/${id}`,
    method: 'delete'
  })
}

/**
 * 获取角色具体权限
 * @param id
 */
export function getRolePermission(id) {
  return request({
    url: `admin/systems/roles/${id}`,
    method: 'get'
  })
}

/**
 * 添加角色
 * @param params
 */
export function addRole(params) {
  return request({
    url: 'admin/systems/roles',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: params
  })
}

/**
 * 修改角色
 * @param id
 * @param params
 */
export function editRole(id, params) {
  return request({
    url: `admin/systems/roles/${id}`,
    method: 'put',
    headers: { 'Content-Type': 'application/json' },
    data: params
  })
}

/**
 * 修改当前管理员的头像以及密码
 * @param params
 */
export function editUserInfo(params) {
  if (params.old_passwprd && params.password) {
    params.old_passwprd = md5(params.old_passwprd)
    params.password = md5(params.password)
  }
  return request({
    url: 'admin/systems/admin-users',
    method: 'put',
    data: params
  })
}
