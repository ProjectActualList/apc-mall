/**
 * 微信分享相关API
 */

import request, { Method } from '@/utils/request'
import Storage from '@/utils/storage'

let wx
if (process.browser) wx = require('weixin-js-sdk')
/**
 * 微信内分享
 * @param params
 * @returns {Promise<void>}
 */
export function wxShare(params, shareParams) {
  // 调用后端API接受json形式的签名等配置数据
  let accessToken = Storage.getItem('access_token')
  return new Promise(async (resolve, reject) => {
    let config
    try {
      config = await request({ url: '/wechat', method: Method.GET, headers: { Authorization: accessToken } ,params })
    } catch (e) {
      console.log('服务器端出错', e)
    }
    wx.config({
      debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
      appId: config.appid, // 必填，公众号的唯一标识
      timestamp: config.timestamp, // 必填，生成签名的时间戳
      nonceStr: config.noncestr, // 必填，生成签名的随机串
      signature: config.signature, // 必填，签名，见附录1
      jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage', 'onMenuShareQQ','onMenuShareWeibo'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    })
    wx.ready(() => {
      // 获取“分享到朋友圈”按钮点击状态及自定义分享内容接口
      wx.onMenuShareTimeline({
        title: shareParams.title, // 分享标题
        link: shareParams.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        imgUrl: shareParams.imgUrl, // 分享图标
        success: (e) => {
          resolve()
        },
        cancel: () => { reject() }
      })
      // 获取“分享给朋友”按钮点击状态及自定义分享内容接口
      wx.onMenuShareAppMessage({
        title: shareParams.title, // 分享标题
        desc: shareParams.desc, // 分享描述
        link: shareParams.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        imgUrl: shareParams.imgUrl, // 分享图标
        type: '', // 分享类型,music、video或link，不填默认为link
        dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
        success: () => { resolve() },
        cancel: () => { reject() }
      })
      // 获取“分享至QQ”按钮点击状态及自定义分享内容接口
      wx.onMenuShareQQ({
        title: shareParams.title, // 分享标题
        desc: shareParams.desc, // 分享描述
        link: shareParams.link, // 分享链接
        success: () => { resolve() },
        cancel: () => { reject() }
      })
      // 获取“分享至微博”按钮点击状态及自定义分享内容接口
      wx.onMenuShareWeibo({
        title: shareParams.title, // 分享标题
        desc: shareParams.desc, // 分享描述
        link: shareParams.link, // 分享链接
        imgUrl: shareParams.imgUrl, // 分享图标
        success: () => { resolve() },
        cancel: () =>{ reject() }
      })
    })
  })

}

