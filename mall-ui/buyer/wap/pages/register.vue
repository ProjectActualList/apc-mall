<template>
  <div id="register">
    <nav-bar title="会员注册"></nav-bar>
    <no-ssr>
      <van-popup
        v-model="showAgreement"
        :close-on-click-overlay="false"
        position="bottom"
        style="height: 75%"
      >
        <van-nav-bar
          title="注册协议"
          class="agreement-title"
        />
        <div class="agreement-content" v-html="agreement.content"></div>
        <div class="agreement-btns">
          <van-button @click="handleAgreement(false)">不同意</van-button>
          <van-button type="danger" @click="handleAgreement(true)">同意</van-button>
        </div>
      </van-popup>
    </no-ssr>
    <div class="register-content">
      <div v-show="step === 1">
        <no-ssr>
          <van-cell-group :border="false">
            <van-field
              v-model="registerForm.mobile"
              type="tel"
              clearable
              placeholder="请输入手机号"
              maxlength="11"
            >
              <span slot="label">手&emsp;机&emsp;号</span>
            </van-field>
            <van-field
              v-model="registerForm.captcha"
              center
              clearable
              label="图片验证码"
              placeholder="请输入图片验证码"
              maxlength="4"
            >
              <img v-if="valid_code_url" slot="button" :src="valid_code_url" @click="changeValidCodeUrl" class="captcha-img"/>
            </van-field>
            <van-field
              v-model="registerForm.sms_code"
              center
              clearable
              label="短信验证码"
              placeholder="请输入短信验证码"
              maxlength="6"
            >
              <en-count-down-btn slot="button" :start="sendValidMobileSms">发送验证码</en-count-down-btn>
            </van-field>
          </van-cell-group>
        </no-ssr>
        <div class="big-btn">
          <van-button size="large" :disabled="login_disabled_valsms" @click="handleValSmsCode">下一步</van-button>
        </div>
      </div>
      <div v-show="step === 2" >
        <no-ssr>
          <van-cell-group :border="false">
            <van-field
              v-model="registerForm.password"
              type="password"
              clearable
              label="设置密码"
              placeholder="6-20位英文或数字"
              maxlength="20"
            />
            <van-field
              v-model="registerForm.rep_password"
              type="password"
              clearable
              label="重复密码"
              placeholder="请牢记您的密码"
              maxlength="20"
            />
          </van-cell-group>
        </no-ssr>
        <div class="big-btn">
          <van-button size="large" :disabled="login_disabled_register" @click="handleConfirmRegister">立即注册</van-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import { mapActions } from 'vuex'
  import * as API_Common from '@/api/common'
  import * as API_Passport from '@/api/passport'
  import * as API_Article from '@/api/article'
  import * as API_Connect from '@/api/connect'
  import { RegExp } from '@/ui-utils'
  import { domain } from '@/ui-domain'
  import Storage from '@/utils/storage'
  export default {
    name: 'register',
    layout: 'full',
    async asyncData() {
      const agreement = await API_Article.getArticleByPosition('REGISTRATION_AGREEMENT')
      return { agreement }
    },
    head() {
      return {
        title: `会员注册-${this.site.site_name}`
      }
    },
    data() {
      return {
        // uuid
        uuid: Storage.getItem('uuid'),
        // 步骤
        step: 1,
        // 会员注册 表单
        registerForm: {
          password: '',
          rep_password: '',
          mobile: '',
          captcha: '',
          sms_code: ''
        },
        // 是否显示图片验证码
        showValidCode: false,
        // 图片验证码URL
        valid_code_url: '',
        // 显示注册协议
        showAgreement: true,
        // 注册协议
        agreement: '',
        // 同意注册协议
        agreed: false,
        // 是否为信任登录
        isConnect: false
      }
    },
    computed: {
      /** 下一步 按钮是否禁用 */
      login_disabled_valsms() {
        const { captcha, mobile, sms_code } = this.registerForm
        return !(captcha && mobile && sms_code)
      },
      /** 立即注册 按钮是否禁用 */
      login_disabled_register() {
        const { password, rep_password } = this.registerForm
        if (!password || !rep_password || (password !== rep_password)) return true
        return false
      }
    },
    mounted() {
      this.changeValidCodeUrl()
      const uuid_connect = Storage.getItem('uuid_connect')
      const isConnect = (this.$route.query.form === 'connect' && !!uuid_connect) || this.MixinIsWeChatBrowser()
      this.isConnect = isConnect
    },
    methods: {
      /** 是否同意注册协议 */
      handleAgreement(agreed) {
        if (!agreed) {
          location.href = '/'
        } else {
          this.agreed = agreed
          this.showAgreement = false
        }
      },
      /** 获取图片验证码 */
      changeValidCodeUrl() {
        this.valid_code_url = API_Common.getValidateCodeUrl(this.uuid, 'REGISTER')
      },
      /** 发送手机验证码异步方法 */
      sendValidMobileSms() {
        return new Promise((resolve, reject) => {
          const { mobile, captcha } = this.registerForm
          if (!captcha) {
            this.$message.error('请输入图片验证码！')
            reject()
          } else if (!RegExp.mobile.test(mobile)) {
            this.$message.error('手机号格式有误！')
            reject()
          } else {
            API_Passport.sendRegisterSms(mobile, captcha).then(() => {
              this.$message.success('短信发送成功，请注意查收！')
              resolve()
            }).catch(reject)
          }
        })
      },
      /** 下一步 验证短信验证码 */
      handleValSmsCode() {
        const { mobile, sms_code } = this.registerForm
        API_Passport.validMobileSms(mobile, 'REGISTER', sms_code).then(() => {
          this.step = 2
        })
      },
      /** 立即注册 */
      handleConfirmRegister() {
        if (!this.agreed) {
          this.$message.error('请先同意注册协议！')
          return false
        }
        const { mobile, password } = this.registerForm
        if (!RegExp.password.test(password)) {
          this.$message.error('密码应为6-20位英文或数字！')
          return false
        }
        this.registerByMobile({ mobile, password }).then(() => {
          if (this.isConnect) {
            API_Connect.registerBindConnect(Storage.getItem('uuid_connect')).then(() => {
              this.getUserData().then(() => {
                this.$router.push({ path: '/member' })
              })
            })
          } else {
            this.getUserData().then(() => {
              this.$router.push({ path: '/member' })
            })
          }
        })
      },
      ...mapActions({
        registerByMobile: 'user/registerByMobileAction',
        getUserData: 'user/getUserDataAction'
      })
    }
  }
</script>

<style type="text/scss" lang="scss">
  @import "../assets/styles/color";
  .van-popup {
    overflow: hidden;
  }
  .agreement-title {
    position: relative;
  }
  .agreement-content {
    padding: 20px;
    height: calc(100% - 46px - 44px);
    overflow: scroll;
    box-sizing: border-box;
  }
  .agreement-btns {
    .van-button {
      width: 50%;
      border-radius: 0;
      border: none;
    }
  }
  .register-content {
    .captcha-img {
      width: 70px;
      height: 24px;
    }
    .big-btn {
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
  }
</style>
