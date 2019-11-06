import Vue from 'vue'
import { Loading } from 'element-ui'
import axios from 'axios'
import store from '@/store'
import Storage from '@/utils/storage'
import { Foundation } from '~/ui-utils'
import router from '@/router'
import md5 from 'js-md5'
import checkToken from '@/utils/checkToken'
import { api, api_model } from '~/ui-domain'

const qs = require('qs')

// 创建axios实例
const service = axios.create({
  baseURL: api.admin,
  timeout: 5000,
  paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' })
})

// request拦截器
service.interceptors.request.use(config => {
  // 如果是put/post请求，用qs.stringify序列化参数
  const is_put_post = config.method === 'put' || config.method === 'post'
  const is_json = config.headers['Content-Type'] === 'application/json'
  if (is_put_post && is_json) {
    config.data = JSON.stringify(config.data)
  }
  if (is_put_post && !is_json) {
    config.data = qs.stringify(config.data, { arrayFormat: 'repeat' })
  }
  // Do something before request is sent
  /** 配置全屏加载 */
  if (config.loading !== false) {
    config.loading = Loading.service({
      lock: true,
      background: 'rgba(0, 0, 0, 0.7)',
      spinner: 'el-icon-loading',
      text: '请稍候...'
    })
  }

  // uuid
  const uuid = Storage.getItem('admin_uuid')
  config.headers['uuid'] = uuid

  /** 设置令牌 */
  let accessToken = Storage.getItem('admin_access_token')
  if (accessToken) {
    if (api_model === 'pro') {
      const uid = Storage.getItem('admin_uid')
      const nonce = Foundation.randomString(6)
      const timestamp = parseInt(new Date().getTime() / 1000)
      const sign = md5(uid + nonce + timestamp + accessToken)
      const _params = { uid, nonce, timestamp, sign }
      let params = config.params || {}
      params = { ...params, ..._params }
      config.params = params
    } else {
      config.headers['Authorization'] = accessToken
    }
  }
  return config
}, error => {
  Promise.reject(error)
})

// respone拦截器
service.interceptors.response.use(
  async response => {
    await closeLoading(response)
    return response.data
  },
  async error => {
    await closeLoading(error)
    const error_response = error.response || {}
    const error_data = error_response.data || {}
    if (error_response.status === 403) {
      if (!Storage.getItem('admin_refresh_token')) return
      store.dispatch('fedLogoutAction')
      window.location.replace(`/login?forward=${location.pathname}`)
      return
    }
    if (error.config.message !== false) {
      let _message = error.code === 'ECONNABORTED' ? '连接超时，请稍候再试！' : '网络错误，请稍后再试！'
      Vue.prototype.$message.error(error_data.message || _message)
    }
    return Promise.reject(error)
  }
)

/**
 * 关闭全局加载
 * 延迟200毫秒关闭，以免晃眼睛
 * @param target
 */
const closeLoading = (target) => {
  if (!target.config.loading) return true
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      target.config.loading.close()
      resolve()
    }, 200)
  })
}

export default function request(options) {
  // 如果是刷新token或者登录，不需要检查token直接请求。
  if (options.url.indexOf('systems/admin-users/login') + options.url.indexOf('systems/admin-users/token') > -2) {
    // console.log(options.url + ' | 请求的刷新token或是登录，不需要检查token直接请求。')
    return service(options)
  }
  return new Promise((resolve, reject) => {
    checkToken(options).then(() => {
      service(options).then(resolve).catch(reject)
    })
  })
}
