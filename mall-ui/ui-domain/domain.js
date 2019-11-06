/**
 * Created by Andste on 2018/7/2.
 * buyer_pc : 买家PC端
 * buyer_wap: 买家WAP端
 * seller   : 商家中心
 * admin    : 后台管理
 */

const env = process.server
  ? process.env
  : (global.window ? window.__NUXT__.state.env : {})

module.exports = {
  // 开发环境
  dev: {
    buyer_pc : 'http://127.0.0.1:3000',
    buyer_wap: 'http://127.0.0.1:3001',
    seller   : 'http://127.0.0.1:3002',
    admin    : 'http://127.0.0.1:3003'
  },
  // 生产环境
  pro: {
    buyer_pc : env.DOMAIN_BUYER_PC || 'http://www.javashop7.s1.natapp.cc',
    buyer_wap: env.DOMAIN_BUYER_WAP || 'http://m.javashop7.s1.natapp.cc',
    seller   : env.DOMAIN_SELLER || 'http://seller.javashop7.s1.natapp.cc',
    admin    : env.DOMAIN_ADMIN || 'http://admin.javashop7.s1.natapp.cc'
  }
}
