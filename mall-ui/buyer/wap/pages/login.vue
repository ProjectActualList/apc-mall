<template>
  <div id="login">
    <van-nav-bar
      title="登录"
      left-arrow
      @click-left="MixinRouterBack"
      @click-right="$router.push('clear')">
      <i class="clear-pl" slot="right"/>
    </van-nav-bar>
    <!--登录方式切换-->
    <div class="login-tab">
      <div
        class="tab-item account"
        :class="[login_type === 'account' && 'active']"
        @click="login_type = 'account'"
      >
        <span>账号密码登录</span>
      </div>
      <div
        class="tab-item quick"
        :class="[login_type === 'quick' && 'active']"
        @click="login_type = 'quick'"
      >
        <span>短信验证码登录</span>
      </div>
    </div>
    <!--账号密码登录、短信验证码登录-->
    <div class="login-content">
      <div v-show="login_type === 'account'" class="content-item account">
        <no-ssr>
          <van-cell-group :border="false">
            <van-field
              v-model="accountForm.username"
              clearable
              placeholder="邮箱/用户名/已验证手机"
            >
              <span slot="label">用&emsp;户&emsp;名</span>
            </van-field>
            <van-field
              v-model="accountForm.password"
              type="password"
              clearable
              placeholder="请输入密码"
            >
              <span slot="label">密&emsp;&emsp;&emsp;码</span>
            </van-field>
            <van-field
              v-model="accountForm.captcha"
              center
              clearable
              label="图片验证码"
              placeholder="请输入图片验证码"
              maxlength="4"
            >
              <img v-if="captcha_url" slot="button" :src="captcha_url" @click="handleChangeCaptchalUrl" class="captcha-img"/>
            </van-field>
          </van-cell-group>
        </no-ssr>
        <div class="login-btn">
          <van-button size="large" :disabled="login_disabled_account" @click="handleLogin">登&nbsp;&nbsp;&nbsp;录</van-button>
        </div>
      </div>
      <div v-show="login_type === 'quick'" class="content-item quick">
        <no-ssr>
          <van-cell-group :border="false">
            <van-field
              v-model="quickForm.mobile"
              type="tel"
              clearable
              placeholder="请输入手机号"
              maxlength="11"
            >
              <span slot="label">手&emsp;机&emsp;号</span>
            </van-field>
            <van-field
              v-model="quickForm.captcha"
              center
              clearable
              label="图片验证码"
              placeholder="请输入图片验证码"
              maxlength="4"
            >
              <img v-if="captcha_url" slot="button" :src="captcha_url" @click="handleChangeCaptchalUrl" class="captcha-img"/>
            </van-field>
            <van-field
              v-model="quickForm.sms_code"
              center
              clearable
              label="短信验证码"
              placeholder="请输入短信验证码"
            >
              <en-count-down-btn slot="button" :start="sendValidMobileSms">发送验证码</en-count-down-btn>
            </van-field>
          </van-cell-group>
        </no-ssr>
        <div class="login-btn">
          <van-button size="large" :disabled="login_disabled_quick" @click="handleLogin">登&nbsp;&nbsp;&nbsp;录</van-button>
        </div>
      </div>
    </div>
    <!--忘记密码、手机注册-->
    <div class="login-nav">
      <nuxt-link :to="'/find-password' + MixinForward">
        <i class="iconfont ea-icon-password"></i>忘记密码
      </nuxt-link>
      <nuxt-link :to="'/register' + MixinForward">
        <i class="iconfont ea-icon-mobile"></i>手机注册
      </nuxt-link>
    </div>
    <!--第三方登录-->
    <div v-if="!isWXBrowser" class="login-other">
      <h4>其他登录方式</h4>
      <div class="icons-login-other">
        <a :href="getConnectUrl('wap', 'QQ')"><i class="iconfont ea-icon-qq"></i></a>
        <!--<a v-if="isWXBrowser" :href="getConnectUrl('wap', 'WECHAT')"><i class="iconfont ea-icon-wechat"></i></a>-->
        <a :href="getConnectUrl('wap', 'WEIBO')"><i class="iconfont ea-icon-weibo"></i></a>
        <a v-if="isAliPayBrowser" :href="getConnectUrl('wap', 'ALIPAY')"><i class="iconfont ea-icon-alipay"></i></a>
      </div>
      <div class="agreement-tips">
        <p>登录即代表您已同意<a href="javascript:;">{{ site.site_name }}隐私政策</a></p>
      </div>
    </div>
  </div>
</template>

<script>
  import { mapActions, mapGetters } from 'vuex'
  import { RegExp } from '@/ui-utils'
  import jwt_decode from 'jwt-decode'
  import * as API_Common from '@/api/common'
  import * as API_Passport from '@/api/passport'
  import * as API_Connect from '@/api/connect'
  import EnCountDownBtn from "@/components/CountDownBtn"
  import Storage from '@/utils/storage'
  export default {
    name: 'login',
    components: {EnCountDownBtn},
    layout: 'full',
    head() {
      return {
        title: `会员登录-${this.site.site_name}`
      }
    },
    data() {
      return {
        // uuid
        uuid: Storage.getItem('uuid'),
        // 登录类型
        login_type: 'account', // 'account',
        /** 图片验证码 */
        captcha_url: '',
        /** 快捷登录 表单 */
        quickForm: {
          mobile: '',
          captcha: '',
          sms_code: ''
        },
        /** 普通登录 表单 */
        accountForm: {
          username: '',
          password: '',
          captcha: ''
        },
        isConnect: false,
        // 是否为微信内置浏览器
        isWXBrowser: process.client ? /micromessenger/i.test(navigator.userAgent) : false,
        // 是否为支付宝内置浏览器
        isAliPayBrowser: process.client ? (navigator.userAgent.match(/Alipay/i) === 'alipay') : false
      }
    },
    computed: {
      // 普通登录按钮 是否禁用
      login_disabled_account() {
        const { username, password, captcha } = this.accountForm
        return !(username && password && captcha)
      },
      // 短信登录按钮 是否禁用
      login_disabled_quick() {
        const { captcha, mobile, sms_code } = this.quickForm
        return !(captcha && mobile && sms_code)
      },
      ...mapGetters(['site'])
    },
    mounted() {
      this.handleChangeCaptchalUrl()
      const uuid_connect = Storage.getItem('uuid_connect')
      const isConnect = (this.$route.query.form === 'connect' && !!uuid_connect) || this.MixinIsWeChatBrowser()
      this.isConnect = isConnect
      if (isConnect) {
        this.login_type = 'account'
      }
    },
    methods: {
      /** 发送短信验证码异步回调 */
      sendValidMobileSms() {
        const { mobile, captcha } = this.quickForm
        return new Promise((resolve, reject) => {
          if (!captcha) {
            this.$message.error('请输入图片验证码！')
            reject()
          } else if (!mobile) {
            this.$message.error('请输入手机号码！')
            reject()
          } else if (!RegExp.mobile.test(mobile)) {
            this.$message.error('手机号码格式有误！')
            reject()
          } else {
            if (this.isConnect) {
              API_Connect.sendMobileLoginSms(mobile, captcha, this.uuid).then(() => {
                this.$message.success('短信发送成功，请注意查收！')
                resolve()
              })
            } else {
              API_Passport.sendLoginSms(mobile, captcha).then(() => {
                this.$message.success('短信发送成功，请注意查收！')
                resolve()
              })
            }
          }
        })
      },
      /** 改变图片验证码URL */
      handleChangeCaptchalUrl() {
        this.captcha_url = API_Common.getValidateCodeUrl(this.uuid, 'LOGIN')
      },
      /** 登录事件 */
      handleLogin() {
        const _forwardMatch = this.MixinForward.match(/\?forward=(.+)/) || []
        const forward = _forwardMatch[1]
        const login_type = this.login_type
        const form = login_type === 'quick' ? this.quickForm : this.accountForm
        if (login_type === 'quick') {
          if (!form.mobile || !RegExp.mobile.test(form.mobile) || !form.sms_code) {
            this.$message.error('表单填写有误，请检查！')
            return false
          }
        } else {
          if (!form.username || !form.password || !form.captcha) {
            this.$message.error('表单填写有误，请检查！')
            return false
          }
        }
        if (this.isConnect) {
          let uuid = Storage.getItem('uuid_connect')
          if (!uuid) {
            this.$message.error('参数异常，请刷新页面！')
            return false
          }
          const params = JSON.parse(JSON.stringify(form))
          params.uuid = this.uuid
          if (login_type === 'quick') {
            API_Connect.loginByMobileConnect(uuid, params).then(loginCallback).catch(this.handleChangeCaptchalUrl)
          } else {
            API_Connect.loginByConnect(uuid, params).then(loginCallback).catch(this.handleChangeCaptchalUrl)
          }
          const _this = this
          // 登录回调
          async function loginCallback(response) {
            if (response.result === 'bind_success') {
              const { uid, access_token, refresh_token } = response
              _this.$store.dispatch('user/setAccessTokenAction', access_token)
              _this.$store.dispatch('user/setRefreshTokenAction', refresh_token)
              const expires = new Date(jwt_decode(refresh_token).exp * 1000)
              Storage.setItem('uid', uid, { expires })
              await _this.getUserData()
              if (_this.MixinIsWeChatBrowser()) {
                window.location.href = '/'
                return
              }
              if (forward && /^http/.test(forward)) {
                window.location.href = forward
              } else {
                window.location.href = forward || '/'
              }
            } else {
              _this.$confirm('当前用户已绑定其它账号，请先解绑！', () => {
                _this.removeAccessToken()
                _this.removeRefreshToken()
                _this.$router.push('/')
              })
            }
          }
        } else {
          this.login({ login_type, form }).then(() => {
            if (forward && /^http/.test(forward)) {
              window.location.href = forward
            } else {
              this.$router.replace({ path: forward || '/' })
            }
          }).catch(this.handleChangeCaptchalUrl)
        }
      },
      /** 获取第三方登录链接 */
      getConnectUrl: API_Connect.getConnectUrl,
      ...mapActions({
        login: 'user/loginAction',
        removeAccessToken: 'user/removeAccessTokenAction',
        removeRefreshToken: 'user/removeRefreshTokenAction',
        getUserData: 'user/getUserDataAction'
      })
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../assets/styles/color";
  .login-tab {
    height: 44px;
    line-height: 45px;
    border-bottom: 1px solid #ebeef5;
    color: #333;
    font-size: 14px;
    .tab-item {
      width: 50%;
      float: left;
      text-align: center;
      &.active span {
        border-bottom: 2px solid #f23030;
        padding: 13px 10px;
      }
      &.disabled {
        color: #999;
        pointer-events: none;
      }
    }
  }
  .login-content {
    padding-top: 10px;
    padding-bottom: 20px;
    .login-btn {
      padding: 10px 15px;
      .van-button {
        color: #fff;
        background-color: $color-main;
        &.van-button--disabled {
          color: #999;
          background-color: #e8e8e8;
          border: 1px solid #e5e5e5;
        }
      }
    }
    .captcha-img {
      width: 70px;
      height: 24px;
    }
  }
  .login-nav {
    padding: 0 15px;
    a + a { float: right }
  }
  .login-other {
    margin-top: 80px;
    height: 85px;
    position: relative;
    padding: 24px 10px 0 10px;
    border-top: 1px solid #d7d7d7;
    h4 {
      position: absolute;
      top: -19px;
      left: 50%;
      background-color: white;
      padding: 5px 15px;
      color: #bfbfbf;
      transform: translateX(-50%);
    }
    .icons-login-other {
      width: 100%;
      text-align: center;
      i {
        font-size: 28px;
        margin-right: 15px;
      }
      .ea-icon-qq { color: #68B2FC }
      .ea-icon-wechat { color: #56C13D }
      .ea-icon-weibo { color: #E31C34 }
      .ea-icon-alipay { color: #1AA1E6 }
    }
    .agreement-tips {
      margin-top: 20px;
      width: 100%;
      text-align: center;
      font-size: 14px;
      line-height: 30px;
      color: #333
    }
  }
  /deep/ .clear-pl {
    display: inline-block;
    width: 15px;
    height: 15px;
  }
</style>
