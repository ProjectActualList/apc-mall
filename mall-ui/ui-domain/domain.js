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
    buyer_pc : 'http://192.168.1.119:3000',
    buyer_wap: 'http://192.168.1.119:3001',
    seller   : 'http://192.168.1.119:3002',
    admin    : 'http://192.168.1.119:3003'
  },
  // 生产环境
  pro: {
    buyer_pc : env.DOMAIN_BUYER_PC || 'http://192.168.1.119:7000',
    buyer_wap: env.DOMAIN_BUYER_WAP || 'http://192.168.1.119:7000',
    seller   : env.DOMAIN_SELLER || 'http://192.168.1.119:7000',
    admin    : env.DOMAIN_ADMIN || 'http://192.168.1.119:7000'
  }
}
