import * as API_Members from '@/api/members'
import * as API_Passport from '@/api/passport'
import * as types from './mutation-types'
import Storage from '@/utils/storage'
import jwt_decode from 'jwt-decode'
import { Foundation } from '@/ui-utils'
import * as API_Address from "@/api/address"

export const state = () => {
  const user = Storage.getItem('user')
  return {
    user: user ? JSON.parse(user) : ''
  }
}

/** mutations */
export const mutations = {
  /**
   * 保存用户信息
   * @param state
   * @param data
   */
  [types.SET_USER_INFO](state, data) {
    state.user = data
    if (process.client) {
      const refreshToken = Storage.getItem('refresh_token')
      const expires = new Date(jwt_decode(refreshToken).exp * 1000)
      Storage.setItem('user', JSON.stringify(data), { expires })
    }
  },
  /**
   * 移除用户信息
   * @param state
   * @param data
   */
  [types.REMOVE_USER_INFO](state, data) {
    state.user = ''
    Storage.removeItem('user')
    Storage.removeItem('uid')
  },
  /**
   * 设置访问令牌
   * @param state
   * @param token
   */
  [types.SET_ACCESS_TOKEN](state, token) {
    if (process.client) {
      const expires = new Date(jwt_decode(token).exp * 1000)
      Storage.setItem('access_token', token, { expires })
    }
  },
  /**
   * 移除访问令牌
   * @param state
   */
  [types.REMOVE_ACCESS_TOKEN](state) {
    Storage.removeItem('access_token')
  },
  /**
   * 设置刷新令牌
   * @param state
   * @param token
   */
  [types.SET_REFRESH_TOKEN](state, token) {
    if (process.client) {
      const expires = new Date(jwt_decode(token).exp * 1000)
      Storage.setItem('refresh_token', token, { expires })
      Storage.setItem('uid', Storage.getItem('uid'), { expires })
      Storage.setItem('user', Storage.getItem('user'), { expires })
    }
  },
  /**
   * 移除刷新令牌
   * @param state
   */
  [types.REMOVE_REFRESH_TOKEN](state) {
    Storage.removeItem('refresh_token')
  }
}

/** actions */
export const actions = {
  /**
   * 获取用户数据
   * @param commit
   * @param params
   */
  getUserDataAction: ({ commit }) => {
    return new Promise((resolve, reject) => {
      API_Members.getUserInfo().then(response => {
        commit(types.SET_USER_INFO, response)
        resolve(response)
      }).catch(error => reject(error))
    })
  },
  /**
   * 登录
   * @param commit
   * @param params
   * @returns {Promise<any>}
   */
  loginAction: ({ commit, dispatch }, params) => {
    return new Promise((resolve, reject) => {
      if (params.login_type === 'quick') {
        const { mobile, sms_code } = params.form
        API_Passport.loginByMobile(mobile, sms_code).then(loginSccess).catch(reject)
      } else {
        params.form.uuid = Storage.getItem('uuid')
        API_Passport.login(params.form).then(loginSccess).catch(reject)
      }
      function loginSccess(res) {
        const { access_token, refresh_token, uid } = res
        commit(types.SET_ACCESS_TOKEN, access_token)
        commit(types.SET_REFRESH_TOKEN, refresh_token)
        const expires = new Date(jwt_decode(refresh_token).exp * 1000)
        Storage.setItem('uid', uid, { expires })
        API_Members.getUserInfo().then(response => {
          commit(types.SET_USER_INFO, response)
          resolve(response)
        }).catch(reject)
      }
    })
  },
  /**
   * 登出
   * @param commit
   * @param dispatch
   * @param type
   * @returns {Promise<any>}
   */
  logoutAction: ({ commit, dispatch }, type) => {
    return new Promise((resolve, reject) => {
      API_Members.logout(type).then(() => {
        commit(types.REMOVE_USER_INFO)
        commit(types.REMOVE_ACCESS_TOKEN)
        commit(types.REMOVE_REFRESH_TOKEN)
        resolve()
      }).catch(error => reject(error))
    })
  },
  /**
   * 保存用户信息【修改资料】
   * @param commit
   * @param params
   * @returns {Promise<any>}
   */
  saveUserInfoAction: ({ commit }, params) => {
    return new Promise((resolve, reject) => {
      API_Members.saveUserInfo(params).then(response => {
        commit(types.SET_USER_INFO, response)
        resolve(response)
      }).catch(error => reject(error))
    })
  },
  /**
   * 移除用户信息
   * @param commit
   */
  removeUserAction: ({ commit }) => {
    commit(types.REMOVE_USER_INFO)
  },
  /**
   * 注册【通过手机号】
   * @param commit
   * @param params
   * @returns {Promise<any>}
   */
  registerByMobileAction: ({ commit, dispatch }, params) => {
    return new Promise((resolve, reject) => {
      API_Passport.registerByMobile(params).then(res=> {
        const { access_token, refresh_token, uid } = res
        commit(types.SET_ACCESS_TOKEN, access_token)
        commit(types.SET_REFRESH_TOKEN, refresh_token)
        const expires = new Date(jwt_decode(refresh_token).exp * 1000)
        Storage.setItem('uid', uid, { expires })
        resolve(res)
      })
    })
  },
  /**
   * 设置accessToken
   * @param commit
   * @param accessToken
   */
  setAccessTokenAction: ({ commit }, accessToken) => {
    commit(types.SET_ACCESS_TOKEN, accessToken)
  },
  /**
   * 移除accessToken
   * @param commit
   */
  removeAccessTokenAction: ({ commit }) => {
    commit(types.REMOVE_ACCESS_TOKEN)
  },
  /**
   * 设置refreshToken
   * @param commit
   * @param refreshToken
   */
  setRefreshTokenAction: ({ commit }, refreshToken) => {
    commit(types.SET_REFRESH_TOKEN, refreshToken)
  },
  /**
   * 移除refreshToken
   * @param commit
   */
  removeRefreshTokenAction: ({ commit }) => {
    commit(types.REMOVE_REFRESH_TOKEN)
  }
}
