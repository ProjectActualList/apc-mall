<template>
  <div id="find-password">
    <nav-bar title="找回密码"></nav-bar>
    <!--验证账号-->
    <div v-show="step === 1" class="find-item">
      <van-cell-group :border="false">
        <van-field
          v-model="validAccountForm.account"
          clearable
          label="账户名"
          placeholder="请输入账户名"
        >
          <span slot="label">账&emsp;户&emsp;名</span>
        </van-field>
        <van-field
          v-model="validAccountForm.img_code"
          clearable
          label="图片验证码"
          placeholder="请输入图片验证码"
          maxlength="4"
        >
          <img v-if="valid_img_url" :src="valid_img_url" slot="button" @click="getValidImgUrl" class="captcha-img">
        </van-field>
      </van-cell-group>
      <div class="big-btn">
        <van-button size="large" :disabled="val_disabled_account" @click="handleValidAccount">验证账户</van-button>
      </div>
    </div>
    <!--验证手机号-->
    <div v-show="step === 2" class="find-item">
      <van-cell-group :border="false">
        <van-field
          v-model="validMobileForm.mobile"
          clearable
          readonly
        >
          <span slot="label">手&emsp;机&emsp;号</span>
        </van-field>
        <van-field
          v-model="validMobileForm.img_code"
          clearable
          label="图片验证码"
          placeholder="请输入图片验证码"
          maxlength="4"
        >
          <img v-show="valid_img_url" :src="valid_img_url" slot="button" @click="getValidImgUrl" class="captcha-img">
        </van-field>
        <van-field
          v-model="validMobileForm.sms_code"
          center
          clearable
          label="短信验证码"
          placeholder="请输入短信验证码"
          maxlength="6"
        >
          <en-count-down-btn slot="button" :start="sendValidMobileSms">发送验证码</en-count-down-btn>
        </van-field>
      </van-cell-group>
      <div class="big-btn">
        <van-button size="large" :disabled="val_disabled_mobile" @click="handleNextStep">下一步</van-button>
      </div>
    </div>
    <!--更新密码-->
    <div v-show="step === 3">
      <van-notice-bar>
        请务必牢记您的新密码！为了您的账户安全，请定期更换密码！
      </van-notice-bar>
      <div class="find-item">
        <van-cell-group :border="false">
          <van-field
            v-model="changePasswordForm.password"
            type="password"
            clearable
            placeholder="请输入密码"
            maxlength="20"
            :error-message="passwordError"
          >
            <span slot="label">新&emsp;密&emsp;码</span>
          </van-field>
          <van-field
            v-model="changePasswordForm.rep_password"
            type="password"
            clearable
            label="确认新密码"
            placeholder="请确认密码"
            maxlength="20"
            :error-message="repPasswordError"
          />
        </van-cell-group>
        <div class="big-btn">
          <van-button size="large" :disabled="val_disabled_password" @click="submitChangeForm">确认修改</van-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_Passport from '@/api/passport'
  import * as API_Common from '@/api/common'
  import { RegExp } from '@/ui-utils'
  import Storage from '@/utils/storage'

  export default {
    name: 'find-password',
    layout: 'full',
    head() {
      return {
        title: `找回密码-${this.site.site_name}`
      }
    },
    data() {
      return {
        // uuid
        uuid: Storage.getItem('uuid'),
        // 步骤
        step: 1,
        // 校验账户信息 表单
        validAccountForm: {},
        // 校验手机 表单
        validMobileForm: {},
        // 图片验证码URL
        valid_img_url: '',
        // 修改密码 表单
        changePasswordForm: {
          password: '',
          rep_password: ''
        }
      }
    },
    computed: {
      /** 验证账户按钮 按钮是否禁用 */
      val_disabled_account() {
        const { account, img_code } = this.validAccountForm
        return !(account && img_code)
      },
      /** 验证手机号 按钮是否禁用 */
      val_disabled_mobile() {
        const { img_code, sms_code } = this.validMobileForm
        return !(img_code && sms_code)
      },
      /** 确认修改密码 按钮是否禁用 */
      val_disabled_password() {
        const { password, rep_password } = this.changePasswordForm
        return (!password || !rep_password) || (password !== rep_password)
      },
      // 密码 错误信息
      passwordError() {
        const { password } = this.changePasswordForm
        if (!password) return ''
        if (RegExp.password.test(password)) return ''
        return '密码应为6-20位英文或数字！'
      },
      // 确认密码 错误信息
      repPasswordError() {
        const { password, rep_password } = this.changePasswordForm
        if (!password || !rep_password) return ''
        if (password !== rep_password) return '两次输入不一致！'
        return ''
      },
    },
    mounted() {
      this.$nextTick(this.getValidImgUrl)
    },
    methods: {
      /** 获取图片验证码URL */
      getValidImgUrl() {
        const uuid = this.step === 1 ? this.uuid : this.validMobileForm.uuid
        this.valid_img_url = API_Common.getValidateCodeUrl(uuid, 'FIND_PASSWORD')
      },
      /** 验证账户 */
      handleValidAccount() {
        const { uuid } = this
        const { account, img_code } = this.validAccountForm
        API_Passport.validAccount(uuid, img_code, account).then((response) => {
          this.validMobileForm.mobile = response.mobile
          this.validMobileForm.uname = response.uname
          this.validMobileForm.uuid = response.uuid
          this.step = 2
          this.getValidImgUrl()
        })
      },
      /** 发送手机验证码异步方法 */
      sendValidMobileSms() {
        return new Promise((resolve, reject) => {
          const { uuid, img_code } = this.validMobileForm
          if (!img_code) {
            this.$message.error('请输入图片验证码！')
            reject()
          } else {
            API_Passport.sendFindPasswordSms(uuid, img_code).then(() => {
              this.$message.success('发送成功，请注意查收！')
              resolve()
            }).catch(reject)
          }
        })
      },
      /** 下一步 */
      handleNextStep() {
        const { uuid, sms_code } = this.validMobileForm
        API_Passport.validFindPasswordSms(uuid, sms_code).then(() => {
          this.step = 3
          this.getValidImgUrl()
        })
      },
      /** 找回密码 */
      submitChangeForm() {
        const { uuid } = this.validMobileForm
        const { password } = this.changePasswordForm
        API_Passport.changePassword(uuid, password).then(() => {
          this.$message.success('密码找回成功，请牢记您的新密码！')
          setTimeout(() => {
            this.$router.push(`/login${this.MixinForward}`)
          }, 200)
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../assets/styles/color";
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
  .captcha-img {
    width: 70px;
    height: 24px;
  }
</style>
