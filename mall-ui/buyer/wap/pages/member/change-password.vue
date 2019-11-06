<template>
  <div id="change-password">
    <nav-bar title="修改密码"/>
    <div class="change-password-container"></div>
    <div v-show="step === 1" class="valid-mobile">
      <van-cell-group :border="false">
        <van-field
          v-model="bindMobile"
          clearable
          placeholder="请输入手机号"
          maxlength="11"
        >
          <span slot="label">已验证手机</span>
        </van-field>
        <van-field
          v-model="validMobileForm.img_code"
          clearable
          label="图片验证码"
          placeholder="请输入图片验证码"
          maxlength="4"
        >
          <img v-if="valid_img_url" :src="valid_img_url" slot="button" @click="getValidImgUrl" class="captcha-img">
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
        <van-button size="large" :disabled="val_disabled_mobile" @click="submitValMobileForm">提交校验</van-button>
      </div>
    </div>
    <div v-show="step === 2" class="change-password">
      <van-cell-group :border="false">
        <van-field
          v-model="changePasswordForm.password"
          type="password"
          clearable
          placeholder="6-20位英文或数字"
          maxlength="20"
        ><span slot="label">新&emsp;密&emsp;码</span></van-field>
        <van-field
          v-model="changePasswordForm.rep_password"
          type="password"
          clearable
          placeholder="请确认密码"
          maxlength="20"
        ><span slot="label">重&nbsp;复&nbsp;密&nbsp;码</span></van-field>
        <van-field
          v-model="changePasswordForm.img_code"
          clearable
          label="图片验证码"
          placeholder="请输入图片验证码"
          maxlength="4"
        >
          <img v-if="valid_img_url" :src="valid_img_url" slot="button" @click="getValidImgUrl" class="captcha-img">
        </van-field>
      </van-cell-group>
      <div class="big-btn">
        <van-button size="large" :disabled="val_disabled_password" @click="submitChangeForm">修改密码</van-button>
      </div>
    </div>
  </div>
</template>

<script>
  import * as API_Common from '@/api/common'
  import * as API_Safe from '@/api/safe'
  import { Foundation, RegExp } from '@/ui-utils'
  import Storage from '@/utils/storage'
  export default {
    name: 'change-password',
    head() {
      return {
        title: `修改密码-${this.site.site_name}`
      }
    },
    data() {
      return {
        uuid: Storage.getItem('uuid'),
        /** 步骤 */
        step: 1,
        /** 验证手机 表单 */
        validMobileForm: {},
        /** 图片验证码URL */
        valid_img_url: '',
        /** 修改密码 表单 */
        changePasswordForm: {
          password: '',
          rep_password: '',
          img_code: ''
        }
      }
    },
    mounted() {
      this.$nextTick(this.getValidImgUrl)
    },
    computed: {
      bindMobile() {
        return Foundation.secrecyMobile(this.$store.getters.user.mobile)
      },
      /** 校验手机号 按钮是否禁用 */
      val_disabled_mobile() {
        const { img_code, sms_code } = this.validMobileForm
        return !(img_code && sms_code)
      },
      /** 修改密码 按钮是否禁用 */
      val_disabled_password() {
        const { password, rep_password, img_code } = this.changePasswordForm
        return !(password && rep_password, img_code)
      }
    },
    methods: {
      /** 获取图片验证码URL */
      getValidImgUrl() {
        this.valid_img_url = API_Common.getValidateCodeUrl(this.uuid, this.step === 1 ? 'VALIDATE_MOBILE' : 'MODIFY_PASSWORD')
      },
      /** 发送验证手机验证码 */
      sendValidMobileSms() {
        return new Promise((resolve, reject) => {
          const { uuid } = this
          const { img_code } = this.validMobileForm
          if (!img_code) {
            this.$message.error('请填写图片验证码！')
            return false
          }
          API_Safe.sendMobileSms(uuid, img_code).then(() => {
            this.$message.success('验证码发送成功，请注意查收！')
            resolve()
          }).catch(reject)
        })
      },
      /** 手机验证 */
      submitValMobileForm() {
        const { sms_code } = this.validMobileForm
        if (!sms_code) {
          this.$message.error('请填写短信验证码！')
          return false
        }
        API_Safe.validChangePasswordSms(sms_code).then(() => {
          this.step = 2
          this.getValidImgUrl()
        })
      },
      /** 修改密码 */
      submitChangeForm() {
        const { uuid } = this
        const { password, rep_password, img_code } = this.changePasswordForm
        if (!RegExp.password.test(password)) {
          this.$message.error('密码格式不正确！')
          return false
        }
        if (password !== rep_password) {
          this.$message.error('两次密码输入不一致！')
          return false
        }
        if (!img_code) {
          this.$message.error('请填写图片验证码！')
          return false
        }
        API_Safe.changePassword(uuid, img_code, password).then(() => {
          this.$message.success('密码修改成功，请重新登录！')
          setTimeout(() => {
            this.$store.dispatch('user/logoutAction')
            this.$router.replace({ name: 'login' })
          }, 500)
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .change-password-container {
    padding-top: 46px;
  }
</style>
