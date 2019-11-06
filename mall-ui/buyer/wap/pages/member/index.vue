<template>
  <div id="member-index" style="background-color: #f0f2f5">
    <van-nav-bar title="我的" @click-right="$router.push('clear')">
      <i class="clear-pl" slot="right"/>
    </van-nav-bar>
    <div class="member-container">
      <div class="head-box" :style="{backgroundImage: 'url('+ user.face +')'}"></div>
      <div class="head-items">
        <div class="head-img">
          <nuxt-link to="/member/my-profile"><en-face :url="user.face"/></nuxt-link>
        </div>
        <div class="head-user-name">{{ user.nickname }}</div>
        <div class="items">
          <nuxt-link to="/member/my-collection" class="item">
            <span>
              <p>{{ nums.goods_collect_count || 0 }}</p>
              <p>收藏的商品</p>
            </span>
          </nuxt-link>
          <nuxt-link to="/member/my-collection?type=shop" class="item">
            <p>{{ nums.shop_collect_count || 0 }}</p>
            <p>收藏的店铺</p>
          </nuxt-link>
          <nuxt-link to="/member/my-order?order_status=WAIT_COMMENT" class="item">
            <p>{{ nums.pending_comment_count || 0}}</p>
            <p>待评论</p>
          </nuxt-link>
        </div>
      </div>
      <div class="member-nav">
        <ul class="member-nav-items">
          <li>
            <nuxt-link to="/member/my-order?order_status=WAIT_PAY">
              <i class="iconfont ea-icon-wait-pay"></i>
              <p>待付款</p>
            </nuxt-link>
          </li>
          <li>
            <nuxt-link to="/member/my-order?order_status=WAIT_SHIP">
              <i class="iconfont ea-icon-wait-ship"></i>
              <p>待发货</p>
            </nuxt-link>
          </li>
          <li>
            <nuxt-link to="/member/my-order?order_status=WAIT_ROG">
              <i class="iconfont ea-icon-wait-rog"></i>
              <p>待收货</p>
            </nuxt-link>
          </li>
          <li>
            <nuxt-link to="/member/after-sale">
              <i class="iconfont ea-icon-after-sale"></i>
              <p>退款/售后</p>
            </nuxt-link>
          </li>
        </ul>
        <van-cell title="全部订单" is-link value="查看全部订单" to="/member/my-order"/>
      </div>
      <van-cell-group style="margin-top: 10px">
        <van-cell title="我的优惠券" is-link value="查看优惠券" to="/member/my-coupons"/>
        <van-cell title="我的积分" is-link value="消费、等级积分" to="/member/my-points"/>
        <van-cell title="站内消息" is-link value="查看站内消息" to="/member/website-message"/>
        <van-cell v-if="show_dis" title="分销管理" is-link value="查看我的分销" to="/member/distribution/distribution-manage"/>
      </van-cell-group>
      <van-cell-group style="margin-top: 10px">
        <van-cell title="我的资料" is-link value="修改资料" to="/member/my-profile"/>
        <van-cell title="收货地址" is-link value="我的收货地址" to="/member/shipping-address"/>
        <van-cell title="账户安全" is-link value="修改密码、手机绑定" to="/member/account-safe"/>
      </van-cell-group>
      <div class="big-btn">
        <van-button type="danger" size="large" @click="handleLogout">退出登录</van-button>
      </div>
    </div>
    <tab-bar :active="3"/>
  </div>
</template>

<script>
  import { mapActions, mapGetters } from 'vuex'
  import * as API_Members from '@/api/members'
  import Storage from '@/utils/storage'
  export default {
    name: 'member-index',
    data() {
      return {
        nums: ''
      }
    },
    mounted() {
      if (Storage.getItem('refresh_token')) {
        API_Members.getStatisticsNum().then(response => {
          this.nums = response
        })
      }
    },
    computed: {
      // 显示分销菜单
      show_dis() {
        return process.env.distribution
      },
      ...mapGetters(['user'])
    },
    methods: {
      /** 退出登录 */
      handleLogout() {
        if (this.MixinIsWeChatBrowser()) {
          this.$confirm('确定要退出登录吗？这将会解绑您的微信！', () => {
            this.logout('WECHAT').then(() => {
              Storage.removeItem('is_wechat_auth')
              location.href = '/'
            })
          })
        } else {
          this.$confirm('确定要退出登录吗？', () => {
            this.logout().then(() => {
              location.href = '/'
            })
          })
        }
      },
      ...mapActions({
        logout: 'user/logoutAction'
      })
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .member-container {
    padding-top: 46px;
    padding-bottom: 50px;
    .head-box {
      width: 100%;
      height: 130px;
      background-size: cover;
      background-repeat: no-repeat;
      background-position: center;
      filter: blur(15px);
    }
    .head-items {
      position: relative;
      height: 55px;
      background-color: #fff;
    }
    .head-img {
      position: absolute;
      top: -35px;
      left: 15px;
      width: 70px;
      height: 70px;
      border: 3px rgba(255, 255, 255, 0.8) solid;
      border-radius: 100%;
      background-color: #fff;
      img {
        position: relative;
        width: 100%;
        height: 100%;
        border-radius: 100%;
      }
    }
    .head-user-name {
      position: absolute;
      top: -40px;
      left: 100px;
      max-width: 168px;
      font-size: 16px;
      color: #fff;
      -webkit-line-clamp: 1;
      line-clamp: 1;
      display: -webkit-box;
      word-break: break-all;
      -webkit-box-orient: vertical;
      box-orient: vertical;
      overflow: hidden;
    }
    .items {
      display: flex;
      justify-content: space-between;
      align-items: center;
      position: relative;
      width: calc(100% - 100px);
      height: 55px;
      float: right;
      .item {
        display: flex;
        flex-direction: column;
        justify-content: center;
        width: calc(100% / 3);
        border-left: 1px solid #e7e7e7;
        &:first-child {
          border-left: none;
        }
        span {
          align-self: flex-start;
        }
        p {
          text-align: center;
          font-size: 12px;
          line-height: 18px;
        }
      }
    }
    .member-nav {
      position: relative;
      margin-top: 15px;
      width: 100%;
      min-height: 110px;
      background-color: #fff;
    }
    .member-nav-items {
      position: relative;
      display: flex;
      justify-content: space-around;
      align-items: center;
      height: 66px;
      border-bottom: 1px solid #e7e7e7;
      margin-top: -1px;
      li {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 25%;
        a {
          display: block;
          width: 100%;
          text-align: center;
        }
      }
      i {
        display: block;
        height: 26px;
        font-size: 26px;
        text-align: center;
      }
    }
    .big-btn {
      padding: 10px 15px;
    }
  }
  /deep/ .clear-pl {
    display: inline-block;
    width: 15px;
    height: 15px;
  }
</style>

