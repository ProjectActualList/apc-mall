import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css' // Progress 进度条样式
import { Message } from 'element-ui'
import Storage from '@/utils/storage'

const whiteList = ['/login']

router.beforeEach((to, from, next) => {
  NProgress.start()
  const refreshToken = Storage.getItem('admin_refresh_token')
  if (refreshToken) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      if (store.getters.addRouters.length === 0) {
        store.dispatch('GenerateRoutes').then(() => {
          router.addRoutes(store.getters.addRouters)
          next({ ...to, replace: true })
        }).catch(() => {
          store.dispatch('fedLogoutAction').then(() => {
            Message.error('验证失败,请重新登录')
            window.location.replace(`/login?forward=${location.pathname}`)
          })
        })
      } else {
        next()
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?forward=${location.pathname}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
