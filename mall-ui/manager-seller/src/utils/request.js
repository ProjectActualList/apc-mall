import Vue from 'vue'
import axios from 'axios'
import { MessageBox, Loading } from 'element-ui'
import Storage from '@/utils/storage'
import { Foundation } from '~/ui-utils'
import { domain } from '~/ui-domain'
import store from '@/store'
import router from '@/router'
import md5 from 'js-md5'
import checkToken from '@/utils/checkToken'
import { api, api_model } from '~/ui-domain'
const qs = require('qs')

// 创建axios实例
const service = axios.create({
  baseURL: api.seller, // 基础api
  timeout: 5000, // 请求超时时间
  paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' })
})

// request拦截器
service.interceptors.request.use(config => {
  // Do something before request is sent
  /** 配置全屏加载 */
  if (config.loading !== false) {
    const { loading } = config
    const is_num = typeof (config.loading) === 'number'
    if (is_num) config.loading_num = true
    config.loading = Loading.service({
      lock: true,
      background: `rgba(0, 0, 0, ${is_num ? loading : '0.8'})`,
      spinner: 'el-icon-loading'
    })
  }

  // uuid
  const uuid = Storage.getItem('seller_uuid')
  config.headers['uuid'] = uuid

  // 获取访问Token
  let accessToken = Storage.getItem('seller_access_token')
  if (accessToken) {
    if (api_model === 'pro') {
      const uid = Storage.getItem('seller_uid')
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
  /** 进行参数序列化 */
  if ((config.method === 'put' || config.method === 'post') && config.headers['Content-Type'] !== 'application/json') {
    config.data = qs.stringify(config.data, { arrayFormat: 'repeat' })
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
      if (!Storage.getItem('seller_refresh_token')) return
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
  const { loading, loading_num } = target.config
  if (!loading) return true
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      target.config.loading.close()
      resolve()
    }, loading_num ? 0 : 200)
  })
}

export default function request(options) {
  // 如果是请求【刷新token、登录、退出】不需要检查token，直接请求。
  if (/seller\/check\/token|seller\/login|seller\/members\/logout/.test(options.url)) {
    return service(options)
  }
  return new Promise((resolve, reject) => {
    checkToken(options).then(() => {
      service(options).then(resolve).catch(reject)
    })
  })
}
