import request from '@/utils/request'
import Storage from '@/utils/storage'
import md5 from 'js-md5'

/**
 * 登录
 * @param params
 * @returns {*}
 */
export function login(params) {
  params.password = md5(params.password)
  return request({
    url: 'admin/systems/admin-users/login',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 退出账户
 * @returns {*}
 */
export function logout() {
  return request({
    url: `admin/systems/admin-users/logout?uid=${Storage.getItem('admin_uid')}`,
    method: 'post'
  })
}

/**
 * 获取用户角色权限
 * @param role_id
 * @returns {*}
 */
export function getUserRolesPermissions(role_id) {
  return request({
    url: `admin/systems/roles/${role_id}/checked`,
    method: 'get'
  })
}
