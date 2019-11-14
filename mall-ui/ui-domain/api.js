/**
 * Created by Andste on 2018/7/2.
 * base    : 基础业务API
 * buyer   : 买家API
 * seller  : 商家中心API
 * admin   ：后台管理API
 */

const env = process.server
  ? process.env
  : (global.window ? window.__NUXT__.state.env : {})

module.exports = {
  // 开发环境
  dev: {
    base  : 'http://192.168.1.119:6001/base-api',
    buyer : 'http://192.168.1.119:6002/buyer-api',
    seller: 'http://192.168.1.119:6003/seller-api',
    admin : 'http://192.168.1.119:6004/manager-api'
  },
  // 生产环境
  pro: {
    base  : env.API_BASE || 'http://192.168.1.119:6000/base-api',
    buyer : env.API_BUYER || 'http://192.168.1.119:6000/buyer-api',
    seller: env.API_SELLER || 'http://192.168.1.119:6000/seller-api',
    admin : env.API_ADMIN || 'http://192.168.1.119:6000/manager-api'
  }
}
