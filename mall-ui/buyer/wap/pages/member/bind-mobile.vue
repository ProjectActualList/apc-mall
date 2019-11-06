<template>
  <div id="bind-mobile">
    <nav-bar title="绑定手机"/>
    <div class="bind-mobile-container">
      <van-cell-group :border="false">
        <van-field
          v-model="bindMobileForm.mobile"
          clearable
          placeholder="请输入手机号"
          maxlength="11"
        >
          <span slot="label">手&emsp;机&emsp;号</span>
        </van-field>
        <van-field
          v-model="bindMobileForm.img_code"
          clearable
          label="图片验证码"
          placeholder="请输入图片验证码"
          maxlength="4"
        >
          <img v-show="valid_img_url" :src="valid_img_url" slot="button" @click="getValidImgUrl" class="captcha-img">
        </van-field>
        <van-field
          v-model="bindMobileForm.sms_code"
          center
          clearable
          label="短信验证码"
          placeholder="请输入短信验证码"
          maxlength="6"
        >
          <en-count-down-btn slot="button" :start="sendBindMobileSms">发送验证码</en-count-down-btn>
        </van-field>
      </van-cell-group>
      <div class="big-btn">
        <van-button size="large" :disabled="val_disabled" @click="submitBindForm">绑&emsp;定</van-button>
      </div>
    </div>
  </div>
</template>

<script>
  import * as API_Safe from '@/api/safe'
  import * as API_Common from '@/api/common'
  import { RegExp } from '@/ui-utils'
  import Storage from '@/utils/storage'
  export default {
    name: 'bind-mobile',
    head() {
      return {
        title: `绑定手机号-${this.site.site_name}`
      }
    },
    data() {
      return {
        uuid: Storage.getItem('uuid'),
        bindMobileForm: {
          mobile: '',
          img_code: '',
          sms_code: ''
        },
        // 图片验证码url
        valid_img_url: '',
        // 绑定成功的手机号
        bindMobile: this.$store.getters.user.mobile
      }
    },
    computed: {
      val_disabled() {
        const { mobile, img_code, sms_code } = this.bindMobileForm
        return !(mobile && img_code && sms_code)
      }
    },
    mounted() {
      this.$nextTick(this.getValidImgUrl)
    },
    methods: {
      /** 获取图片验证码url */
      getValidImgUrl() {
        this.valid_img_url = API_Common.getValidateCodeUrl(this.uuid, 'BIND_MOBILE')
      },
      /** 发送绑定手机验证码 */
      sendBindMobileSms () {
        return new Promise((resolve, reject) => {
          const { mobile, img_code } = this.bindMobileForm
          if (!mobile) {
            this.$message.error('请填写手机号！')
            reject()
          } else if (!RegExp.mobile.test(mobile)) {
            this.$message.error('手机号码格式有误！')
            reject()
          } else if (!img_code) {
            this.$message.error('请输入图片验证码！')
            reject()
          } else {
            const { uuid } = this
            const { mobile, img_code } = this.bindMobileForm
            API_Safe.sendBindMobileSms(mobile, img_code, uuid).then(() => {
              this.$message.success('发送成功，请注意查收！')
              resolve()
            }).catch(reject)
          }
        })
      },
      /** 绑定手机号 表单提交 */
      submitBindForm() {
        const { mobile, sms_code } = this.bindMobileForm
        if (!RegExp.mobile.test(mobile)) {
          this.$message.error('手机号码格式有误！')
          return false
        }
        API_Safe.bindMobile(mobile, sms_code).then(() => {
          this.$message.success('绑定成功！')
          this.$store.dispatch('user/getUserDataAction')
          this.$router.back()
          this.bindMobile = mobile
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .bind-mobile-container {
    padding-top: 46px;
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
  .captcha-img {
    width: 70px;
    height: 24px;
  }
</style>
