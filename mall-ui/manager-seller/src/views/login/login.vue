<template>
  <div class="login-container">
    <div class="login-header">
      <div class="inner-header">
        <div class="logo-header">
          <a :href="MixinBuyerDomain" target="_blank">
            <img src="../../assets/logo_images/logo-javashop-white.png">
          </a>
        </div>
      </div>
    </div>
    <div class="login-banner">
      <swiper :options="swiperOption">
        <swiper-slide class="banner-item item-1">
          <div class="banner-img"></div>
        </swiper-slide>
        <swiper-slide class="banner-item item-2">
          <div class="banner-img"></div>
        </swiper-slide>
        <div class="swiper-button-prev swiper-button-white" slot="button-prev"></div>
        <div class="swiper-button-next swiper-button-white" slot="button-next"></div>
      </swiper>
    </div>
    <div class="login-main">
      <h2 class="lm-title">申请开店流程</h2>
      <h5 class="lm-title-sb">商家按照下面的流程步骤，即可在本商城开设您的店铺。</h5>
      <div class="lm-join-step">
        <dl class="step step-1">
          <dt></dt>
          <dd>注册商家账号</dd>
          <dd>登录后申请开店</dd>
        </dl>
        <span class="arrow"></span>
        <dl class="step step-2">
          <dt></dt>
          <dd>在线申请开店</dd>
          <dd>提交相关信息</dd>
        </dl>
        <span class="arrow"></span>
        <dl class="step step-3">
          <dt></dt>
          <dd>申请提交平台</dd>
          <dd>审核商家资料</dd>
        </dl>
        <span class="arrow"></span>
        <dl class="step step-4">
          <dt></dt>
          <dd>缴纳相关费用</dd>
          <dd>提交付款凭证</dd>
        </dl>
        <span class="arrow"></span>
        <dl class="step step-5">
          <dt></dt>
          <dd>平台审核开通</dd>
          <dd>进入店铺管理</dd>
        </dl>
      </div>
    </div>
    <div class="login-form">
      <div class="lf-content">
        <h3 class="lf-title">商户卖家登录</h3>
        <h5 class="lf-title-sb">请使用卖家账户可登录后进入店铺或申请开店</h5>
        <div class="lf-form" @keyup.enter="submitLoginForm">
          <el-form :model="loginForm" :rules="loginRules" ref="loginForm" size="large" label-width="80px">
            <el-form-item label="商家账号" prop="username">
              <el-input v-model="loginForm.username" clearable placeholder="用户名/邮箱/手机号"></el-input>
            </el-form-item>
            <el-form-item label="登录密码" prop="password">
              <el-input v-model="loginForm.password" type="password" clearable placeholder="请输入密码" maxlength="20"></el-input>
            </el-form-item>
            <el-form-item prop="captcha" class="img-code">
              <span slot="label">验&ensp;证&ensp;码</span>
              <el-input v-model="loginForm.captcha" clearable placeholder="验证码" maxlength="4">
                <template slot="append">
                  <img :src="validcodeImg" @click="changeValidcode" class="verification-code">
                </template>
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="loading" @click="submitLoginForm" class="login-btn">确认登录</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="lf-register">
        还没有成为我们的合作伙伴？<a :href="MixinBuyerDomain + '/register?to=shop-apply'">快速注册</a>
      </div>
    </div>
  </div>
</template>

<script>
  import uuidv1 from 'uuid/v1'
  import Storage from '@/utils/storage'
  import * as API_Common from '@/api/common'

  export default {
    name: 'login',
    data() {
      return {
        // 登录loading状态
        loading: false,
        // 登录表单 表单
        loginForm: {
          username: '',
          password: '',
          captcha: ''
        },
        // 登陆表单 规则
        loginRules: {
          username: [this.MixinRequired('请输入用户名/邮箱/手机号')],
          password: [this.MixinRequired('请输入密码！')],
          captcha: [this.MixinRequired('请输入图片验证码！')]
        },
        // 轮播配置
        swiperOption: {
          loop: true,
          effect: 'fade',
          navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev'
          },
          autoplay: {
            delay: 3000,
            stopOnLastSlide: false,
            disableOnInteraction: false
          },
          simulateTouch: false
        },
        // 图片验证码
        validcodeImg: ''
      }
    },
    mounted() {
      const uuid = Storage.getItem('seller_uuid')
      if (uuid) {
        this.uuid = uuid
      } else {
        const _uuid = uuidv1()
        this.uuid = _uuid
        Storage.setItem('seller_uuid', _uuid, { expires: 30 })
      }
      this.changeValidcode()
    },
    methods: {
      /** 更换图片验证码 */
      changeValidcode() {
        this.validcodeImg = API_Common.getValidateCodeUrl('LOGIN', this.uuid)
      },
      /** 提交登录表单 */
      submitLoginForm() {
        Storage.removeItem('seller_shop')
        this.$refs['loginForm'].validate((valid) => {
          if (valid) {
            const params = this.MixinClone(this.loginForm)
            params.uuid = this.uuid
            this.loading = true
            this.$store.dispatch('loginAction', params).then(() => {
              this.loading = false
              const forward = this.$route.query.forward
              this.$router.push({ path: forward || '/' })
            }).catch(() => {
              this.loading = false
              this.changeValidcode()
            })
          } else {
            this.$message.error('表单填写有误，请核对！')
          }
        })
      }
    }
  }
</script>

<style lang="scss" type="text/scss" scoped >
  .login-container {
    background-color: #fff;
  }
  .login-header {
    width: 100%;
    height: 80px;
    position: absolute;
    z-index: 2;
    top: 0;
    .inner-header {
      width: 1100px;
      margin: 0 auto;
      overflow: hidden;
    }
    .logo-header {
      width: 220px;
      height: 50px;
      text-align: left;
      float: left;
      margin: 15px auto auto 0;
      img {
        height: 100%;
      }
    }
  }
  $banner-height: 600px;
  .login-banner {
    position: relative;
    z-index: 0;
    height: $banner-height;
    .banner-item {
      height: $banner-height;
      .banner-img {
        background: no-repeat center top;
        height: 100%;
        background-size: contain;
      }
      &.item-1 {
        background-image: url("../../assets/background-banner-01-repeat.png");
        .banner-img {
          background-image: url("../../assets/background-banner-01.png");
        }
      }
      &.item-2 {
        background-image: url("../../assets/background-banner-02-repeat.png");
        .banner-img {
          background-image: url("../../assets/background-banner-02.png");
        }
      }
    }
    .swiper-button-prev, .swiper-button-next {
      width: 35px;
      height: 35px;
      border-radius: 100%;
      border: 2px solid rgba(255,255,255,.5);
      background-size: 30%;
      &:hover {
        border-color: rgba(255,255,255,.8)
      }
    }
    .swiper-button-prev {
      left: 50%;
      margin-left: -600px;
    }
    .swiper-button-next {
      right: 50%;
      margin-right: -600px;
    }
  }
  .login-main {
    width: 1200px;
    padding: 30px 0;
    margin: 0 auto;
    overflow: hidden;
    background-color: #fff;
    .lm-title, .lm-title-sb {
      text-align: center;
    }
    .lm-title {
      font: lighter 28px/36px "microsoft yahei";
      color: #333;
    }
    .lm-title-sb {
      font: lighter 14px/20px "microsoft yahei";
      color: #999;
    }
    .lm-join-step {
      font-size: 0;
      width: 1100px;
      margin: 40px auto 60px auto;
      overflow: hidden;
      dl, span {
        font-size: 12px;
        vertical-align: middle;
        letter-spacing: normal;
        word-spacing: normal;
        display: inline-block;
      }
      dl {
        line-height: 20px;
        text-align: center;
        width: 100px;
      }
      span.arrow {
        background: url("../../assets/icon-login-join.png") no-repeat 0 -100px;
        width: 22px;
        height: 22px;
        margin: -30px 63px auto 63px;
      }
      dt {
        background: url("../../assets/icon-login-join.png") no-repeat;
        display: block;
        width: 100px;
        height: 100px;
        margin-bottom: 10px;
      }
      dd {
        margin: 0;
        padding: 0;
        line-height: 20px;
        color: #666
      }
      .step-1 dt { background-position: 0 0 }
      .step-2 dt { background-position: -100px 0 }
      .step-3 dt { background-position: -200px 0 }
      .step-4 dt { background-position: -300px 0 }
      .step-5 dt { background-position: -400px 0 }
    }
  }
  .login-form {
    position: absolute;
    z-index: 2;
    top: 100px;
    left: 50%;
    width: 396px;
    margin-left: 150px;
    .lf-content {
      filter: progid:DXImageTransform.Microsoft.gradient(enabled='true',startColorstr='#E5FFFFFF', endColorstr='#E5FFFFFF');
      background: rgba(255,255,255,0.9);
      overflow: hidden;
      border-radius: 3px 3px 0 0;
    }
    .lf-title {
      display: block;
      height: 30px;
      margin: 20px 0 0;
      text-align: center;
      font-size: 24px;
      font-weight: normal;
      line-height: 30px;
      color: #333;
    }
    .lf-title-sb {
      display: block;
      height: 30px;
      text-align: center;
      font-size: 13px;
      font-weight: normal;
      line-height: 30px;
      color: #999;
    }
    .lf-form {
      padding: 0 30px;
      overflow: hidden;
      /deep/ .img-code {
        .el-input-group__append {
          padding: 0;
          margin: 0;
          img {
            display: block;
            height: 38px;
            cursor: pointer;
          }
        }
      }
      .login-btn {
        width: calc(100% - 76px);
      }
    }
    .lf-register {
      color: #CCC;
      filter: progid:DXImageTransform.Microsoft.gradient(enabled='true', startColorstr='#7F000000', endColorstr='#7F000000');
      background: rgba(0,0,0,0.5);
      text-align: center;
      width: 100%;
      padding: 10px 0;
      border-radius: 0 0 5px 5px;
      line-height: 30px;
      font-size: 14px;
      a { color: #ffd518 }
    }
  }
</style>
