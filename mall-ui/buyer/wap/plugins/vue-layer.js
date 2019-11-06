import Vue from 'vue'
import { Dialog, Toast } from 'vant'

/**
 * 普通alert弹框
 * @param message
 * @param fn
 */
const alert = function (message, fn) {
  Dialog.alert({
    title: '提示',
    message
  }).then(fn)
}
/**
 * 操作确认
 * @param message
 * @param fn
 * @param caFn
 * @returns {*|boolean}
 */
const confirm = function (message, fn, caFn) {
  caFn = caFn || function() {}
  Dialog.confirm({
    title: '提示',
    message
  }).then(fn).catch(caFn)
}

/**
 * 消息提示
 * @type {{success : _message.success, error : _message.error}}
 * @private
 */
const _message = {
  /**
   * 成功提示
   * @param message
   * @param fn
   */
  success: function (message, fn) {
    Toast.success(message)
  },
  /**
   * 错误提示
   * @param message
   * @param fn
   */
  error: function (message, fn) {
    Toast.fail(message)
  }
}

// 挂载 到Vue原型链上
Vue.prototype.$toast = Toast
Vue.prototype.$alert = alert
Vue.prototype.$confirm = confirm
Vue.prototype.$message = _message
