<template>
  <div>
    <van-nav-bar left-arrow title="我的订单" @click-left="MixinRouterBack"/>
    <!--商品信息-->
    <div class="assemble-container">
      <!--商品信息-->
      <div class="assemble-goods">
        <a :href="`/goods/${assemble.goods_id}?sku_id=${assemble.sku_id}&from_nav=assemble`">
          <img :src="assemble.thumbnail" alt="">
        </a>
        <div>
          <a :href="`/goods/${assemble.goods_id}?sku_id=${assemble.sku_id}&from_nav=assemble`">
            <p>{{ assemble.goods_name }}</p>
          </a>
          <p>
            <span>{{ assemble.required_num }}人团 &sdot; 本团已团{{ assemble.offered_num }}件</span>
            <span>
             <em class="assemble-goods-price">¥{{ assemble.origin_price | unitPrice  }}</em>
             <em class="assemble-goods-reduce-price">拼团省¥{{ savePrice | unitPrice }}</em>
           </span>
          </p>
        </div>
      </div>
      <!--邀请好友参团-->
      <div class="assemble-invitations">
        <p>
          <span v-if="assemble.required_num - assemble.offered_num">
            仅剩<em>{{ assemble.required_num - assemble.offered_num }}</em>个名额
          </span>
          <span v-else> 已成团 </span>
        </p>
        <!--参团人数-->
        <ul>
          <li v-for="(user, index) in assemble.participants" :key="index">
            <img :src="user.face" alt="">
            <span v-if="user.is_master === 1">团长</span>
          </li>
        </ul>
        <!--倒计时-->
        <div v-if="assemble.left_time" class="assemble-invitations-countdown">
          剩余
          <span v-if="day">{{ day }}</span><i v-if="day">天</i><span>{{ hours }}</span>时<span>{{ minutes }}</span>分<span>{{ seconds }}</span>秒
          结束
        </div>
        <!--invite friends-->
        <div class="invite-friends">
          <button v-if="assemble.left_time && isShared" @click="showSpecsPopup = true">我也要参团</button>
          <button v-if="assemble.left_time && !isShared" @click="handleInviteFriends">邀请好友</button>
          <button v-if="!assemble.left_time" @click="showSpecsPopup = true">一键发起拼团</button>
        </div>
      </div>
      <!--您可能喜欢 -- 二级功能 -->
      <div v-if="!isShared && maybelikegoods.length" class="maybe-like-goods">
        <p>您可能喜欢</p>
        <ul>
          <li v-for="(goods, index) in maybelikegoods" :key="index">
            <a :href="`/goods/${goods.goods_id}?sku_id=${goods.sku_id}&from_nav=assemble`">
              <img :src="goods.thumbnail" alt="">
            </a>
            <a :href="`/goods/${goods.goods_id}?sku_id=${goods.sku_id}&from_nav=assemble`">
              <p class="goods_name">{{ goods.goods_name }}</p>
            </a>
            <p>¥
              <span>
                {{ goods.origin_price | unitPrice }}
              </span>
            </p>
          </li>
        </ul>
      </div>
    </div>
    <goods-specs
      :goods-id="goods_id"
      :show="showSpecsPopup"
      @sku-changed="(sku) => { selectedSku = sku }"
      @num-changed="(num) => { buyNum = num }"
      @close="showSpecsPopup = false"
      @to-assemble-buy-now="toAssembleBuyNow"
    />
    <!--手动分享弹框-->
    <van-popup v-model="showAssShare" position="bottom" style="height:35%">
      <van-nav-bar title="分享给小伙伴" @click-right="showAssShare = false">
        <i class="iconfont ea-icon-close" slot="right"/>
      </van-nav-bar>
      <ul class="share-ass">
        <li @click="handleShare(share)" v-for="(share, index) in shareList" :key="index">
          <img :src="share.icon" class="share-icon" alt="">
          <span>{{ share.text }}</span>
        </li>
      </ul>
    </van-popup>
    <!--微信遮罩-->
    <div v-show="showWxAssShare" class="weixin-mask">
      <img src="../../../assets/images/background-zhezhao.png" alt="">
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_Order from '@/api/order'
  import * as API_Trade from '@/api/trade'
  import * as API_Share from '@/api/share'
  import Storage from '@/utils/storage'
  import { Foundation } from '@/ui-utils'
  import GoodsSpecs from './goods-specs'
  export default {
    name: 'my-order-assemble',
    data() {
      return {
        // uuid
        uuid: Storage.getItem('uuid'),

        /** 拼团详情 */
        assemble: {},

        /** 拼团人 */
        assemblePeople: [],

        /** 您可能喜欢*/
        maybelikegoods: [],

        /** 倒计时计时器 */
        timer: null,

        /** 天数 */
        day: 0,

        /** 时 */
        hours: 0,

        /** 分 */
        minutes: 0,

        /** 秒 */
        seconds: 0,

	      /** 分享列表 */
	      shareList: [
		      { type: 'qqFriend', text: 'QQ', icon: require('../../../assets/images/icon-qq.jpg') },
		      { type: 'weibo', text: '微博', icon: require('../../../assets/images/icon-weibocai.png') },
		      { type: 'wechatFriend', text: '微信', icon: require('../../../assets/images/icon-weixin.jpg') },
		      { type: 'wechatTimeline', text: '朋友圈', icon: require('../../../assets/images/icon-friend.png') },
		      { type: 'qZone', text: 'QQ空间', icon: require('../../../assets/images/icon-Qzone.png') }
	      ],

        /** 是否显示分享弹框 */
        showAssShare: false,

        /** 是否显示分享遮罩 */
        showWxAssShare: false,

        /** 是否是被分享的 */
        isShared: false,

	      /** 分享参数 */
	      shareParams: {},

        /** 拼团规格弹框**/
        showSpecsPopup: false,

        /** 商品id */
        goods_id: this.$route.query.goods_id,

        /** 购买数量 */
        buyNum: '',

        /** 已选sku */
        selectedSku: ''
      }
    },
    async mounted() {
      // 获取拼团订单详情 // 猜你喜欢
      const responses = await Promise.all([
        API_Order.getAssembleOrderDetail(this.$route.query.order_sn),
        API_Order.getAssembleGuest(this.$route.query.order_sn),
      ])
      this.assemble = responses[0]
      this.assemble.left_time && this.contShareDown(this.assemble.left_time)
      // 猜你喜欢
      this.maybelikegoods = responses[1]
      // 分享参数 分享链接
      const shareLink = `${this.MixinDomain.buyer_wap}/member/my-order/assemble?order_sn=${this.$route.query.order_sn}&goods_id=${this.assemble.goods_id}&sku_id=${this.assemble.sku_id}&from_nav=assemble`
	    this.shareParams = {
	      title: this.assemble.goods_name,
	      desc: this.assemble.goods_name,
	      link: shareLink,
	      imgUrl: this.assemble.thumbnail
      }
	    // 1. 判断是否是微信浏览器 如果是微信浏览器则去右上角进行分享 如果不是则进行手动分享
      if (this.MixinIsWeChatBrowser()) {
        API_Share.wxShare({ url: shareLink, type: 'WAP' }, this.shareParams).then(_ => {
          this.$message.success('分享成功')
          this.showWxAssShare = false
        }).catch(err => {
          this.$message.success('分享失败')
          console.log(err)
        })
      }
      // 2 检测是否是被分享的
      this.isShared = !!(this.$route.query.from_nav && this.$route.query.from_nav === 'assemble')
    },
    computed: {
      savePrice() {
        return parseFloat(this.assemble.origin_price) - parseFloat(this.assemble.sales_price)
      }
    },
    components: {
      [GoodsSpecs.name]: GoodsSpecs
    },
    methods: {
      /** 邀请好友 */
      handleInviteFriends() {
	      this.showAssShare = !this.MixinIsWeChatBrowser()
	      this.showWxAssShare = !!this.MixinIsWeChatBrowser()
      },

      /** 参与分享来的拼团 或者一键发起拼团 */
      toAssembleBuyNow() {
        if (!this.isLogin()) return
        const { buyNum } = this
        const { sku_id } = this.selectedSku
        API_Trade.addToAssembleCart(sku_id, buyNum).then(_ => {
          this.$store.dispatch('cart/getAssembleCartAction')
          const link = this.assemble.left_time ? `/checkout?order_id=${this.$route.query.order_id}&from_nav=assemble` : `/checkout?from_nav=assemble`
          this.$router.push(link)
        })
      },

      /** 是否已登录 */
      isLogin() {
        if (!this.selectedSku) {
          this.$message.error('请选择商品规格！')
          this.showSpecsPopup = true
          return false
        }
        if (!Storage.getItem('refresh_token')) {
          this.$confirm('您还未登录，要现在去登录吗？', () => {
            let _forward = `${this.$route.path}?sku_id=${this.selectedSku.sku_id}`
            if (this.$route.query.from_nav === 'assemble') {
              _forward = `${this.$route.path}?sku_id=${this.selectedSku.sku_id}&from_nav=assemble`
            }
            this.$router.push({ path: '/login', query: { forward:_forward } })
          })
          return false
        } else {
          return true
        }
      },

      /** 开启倒计时 */
      contShareDown(times) {
        let end_ime = times
        this.timer = setInterval(() => {
          if (end_ime <= 0) {
            clearInterval(this.timer)
          } else {
            const time = Foundation.countTimeDown(end_ime)
            this.day = parseInt(time.day)
            this.hours = time.hours
            this.minutes = time.minutes
            this.seconds = time.seconds
            end_ime--
          }
        }, 1000)
      },

	    handleShare(share) {
      	const { type } = share
		    this.$nativeShare.setShareData({ ...this.shareParams, icon: this.assemble.thumbnail })
		    try {
			    this.$nativeShare.call(type)
		    } catch (e) {
			    // 在这里做降级处理
			    this.$message.error('当前浏览器不支持分享功能')
		    }
	    }
    },
    destroyed() {
      clearInterval(this.timer)
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  .assemble-container {
    padding-top: 45px;
    padding-bottom: 10px;
    background-color: #f1f1f1;
	  min-height: 100vh;
    /*拼团商品信息*/
    .assemble-goods {
      padding: 10px;
      display: flex;
      flex-direction: row;
      flex-wrap: nowrap;
      align-items: center;
      background-color: #fff;
      img {
        width: 100px;
        margin-right: 10px;
      }
      & > div {
        height: 100px;
        display: flex;
        flex-direction: column;
        flex-wrap: nowrap;
        justify-content: space-between;
        align-items: flex-start;
        p:first-child {
          color: #333;
          font-size: 15px;
        }
        p:last-child {
          & > span {
            display: block;
          }
          em {
            display: inline-block;
          }
          span {
            em.assemble-goods-price {
              color: #f24;
              font-size: 16px;
              margin-right: 10px;
            }
          }
        }
      }
    }
    /*邀请好友*/
    .assemble-invitations {
      position: relative;
      width: 100%;
      margin: 15px 0;
      min-height: 350px;
      background: url(../../../assets/images/background-pt-share.png);
      background-size: 100% 100%;
      p {
        text-align: center;
        padding-top: 110px;
        span {
          display: inline-block;
          background-color: #f7297c;
          color: #fff;
          padding: 4px 8px;
          border-radius: 20px;
        }
      }
      ul {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        margin-top: 20px;
        li {
          position: relative;
          padding: 0 15px;
          img {
	          width: 65px;
	          height: 65px;
	          border-radius: 50%;
	          object-fit: contain;
          }
          span {
            position: absolute;
            top: 0;
            right: 0;
            z-index: 10;
            display: inline-block;
            padding: 0 6px;
            font-size: 8px;
            border-radius: 17px;
            border: 2px solid #fff;
            background-color: #FCC52D;
          }
        }
      }
      .assemble-invitations-countdown {
        text-align: center;
        margin-top: 15px;
        span {
          text-align: center;
          background-color: #333;
          padding: 2px;
          border-radius: 3px;
          color: #fff;
          font-size: 15px;
        }
      }
      /*邀请好友*/
      .invite-friends {
        position: absolute;
        bottom: 10px;
        width: 90%;
        margin: 0 auto;
        padding:5px 20px;
        button {
          width: 100%;
          line-height:45px;
          border-radius: 5px;
          font-size: 15px;
          color: #fff;
          background-color: #f7297c;
        }
      }
    }
    /** 您可能喜欢 */
    .maybe-like-goods {
      background-color: #fff;
      & > p {
        padding: 10px 15px;
        font-size: 15px;
      }
      & > ul {
        & > li {
          list-style: none;
          width: 30%;
          padding: 0 6px;
          display: inline-block;
          img {
            width: 100%;
            border-radius: 1px;
          }
          & > p.goods_name {
            overflow : hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
          }
          p:last-child > span {
            color: #333;
            font-size: 16px;
          }
        }
      }
    }
  }
  /** 弹框分享 */
  /deep/ ul.share-ass {
    width: 100%;
    height: calc(100% - 46px) ;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    flex-grow: 1;
    justify-content: flex-start;
    align-items: center;
    background-color: #f1f1f1;
    padding-top: 46px;
    li {
      list-style: none;
      display: flex;
      flex-direction: column;
      flex-wrap: nowrap;
      justify-content: flex-start;
      align-items: center;
      width: 25%;
      img.share-icon {
        width: 50px;
        height: 50px;
        background-color: #fff;
        border-radius: 50%;
        border: none;
      }
      span {
        display: inline-block;
        margin-top: 5px;
        text-align: center;
      }
    }
  }

  /** 分享遮罩 */
  .weixin-mask {
    margin: 0;
    padding: 0;
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    right: 0;
    left: 0;
    bottom: 0;
    z-index: 50;
    overflow: hidden;
    background-color: rgba(158, 158, 158,.7);
    background-size: 100% 100%;
    img {
      position: absolute;
      right: 0;
      top: 0;
      z-index: 100;
      width: 100%;
    }
  }
</style>
