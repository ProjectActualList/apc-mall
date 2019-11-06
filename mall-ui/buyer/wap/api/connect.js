/**
 * Created by Andste on 2018/7/2.
 * 信任登录相关API
 */

import request, { Method } from '@/utils/request'
import { api } from '@/ui-domain'
import md5 from 'js-md5'

/**
 * 获取第三方登录url
 * @param client_type
 * @param login_type
 * @returns {string}
 */
export function getConnectUrl(client_type, login_type) {
  return `${process.env.API_BUYER || api.buyer}/passport/connect/${client_type}/${login_type}`
}

/**
 * 获取个人中心绑定url
 * @param login_type
 * @returns {string}
 */
export function getLogindConnectUrl(login_type) {
  return request({
    url: `account-binder/pc/${login_type}`,
    method: Method.GET,
    needToken: true
  })
}

/**
 * 第三方登录绑定
 * @param uuid
 * @param params
 */
export function loginByConnect(uuid, params) {
  params.password = md5(params.password)
  return request({
    url: `passport/login-binder/wap/${uuid}`,
    method: Method.POST,
    data: params
  })
}

/**
 * 登录绑定openid
 * @param uuid
 */
export function loginBindConnect(uuid) {
  return request({
    url: `account-binder/login/${uuid}`,
    method: Method.POST,
    needToken: true
  })
}

/**
 * 注册绑定openid
 * @param uuid
 */
export function registerBindConnect(uuid) {
  return request({
    url: `account-binder/register/${uuid}`,
    method: Method.POST,
    needToken: true
  })
}

/**
 * 获取绑定列表
 */
export function getConnectList() {
  return request({
    url: 'account-binder/list',
    method: Method.GET,
    needToken: true
  })
}

/**
 * 解绑
 * @param type
 */
export function unbindConnect(type) {
  return request({
    url: `account-binder/unbind/${type}`,
    method: Method.POST,
    needToken: true
  })
}

/**
 * 微信自动登录
 * @param uuid
 */
export function weChatAutoLogin(uuid) {
  return request({
    url: 'passport/connect/wechat/login',
    method: Method.GET,
    params: { uuid }
  })
}

/**
 * 微信授权URL
 */
export const wechatAuthUrl = `${process.env.API_BUYER || api.buyer}/passport/connect/wechat/auth`

/**
 * 发送第三方登录手机验证码
 * @param mobile
 * @param captcha
 * @param uuid
 */
export function sendMobileLoginSms(mobile, captcha, uuid) {
  return request({
    url: `passport/mobile-binder/sms-code/${mobile}`,
    method: Method.POST,
    params: {
      captcha,
      uuid
    }
  })
}

/**
 * 第三方登录绑定【通过手机验证码方式登录】
 * @param uuid
 * @param params
 */
export function loginByMobileConnect(uuid, params) {
  return request({
    url: `passport/mobile-binder/${uuid}`,
    method: Method.POST,
    params
  })
}
