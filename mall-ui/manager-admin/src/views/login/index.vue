<template>
  <div class="login-container">
    <div class="login-bg" id="login-bg"></div>
    <div class="login-form" @keyup.enter="submitLogin">
      <a :href="MixinBuyerDomain" class="login-logo" target="_blank">
        <img class="login-logo-img" src="../../assets/logo_images/logo-javashop-rectangle-light.png" alt="javashop">
      </a>
      <el-form :model="loginForm" :rules="loginRules" ref="loginForm" class="login-input">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" :placeholder="translateKey('username')" autofocus clearable>
            <svg-icon slot="prefix" class="el-input__icon" icon-class="user"/>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" :placeholder="translateKey('password')" :minlength="6" :maxlength="20" clearable>
            <svg-icon slot="prefix" class="el-input__icon" icon-class="password"/>
          </el-input>
        </el-form-item>
        <el-form-item prop="validcode">
          <el-input v-model="loginForm.validcode" :placeholder="translateKey('validcode')" :maxlength="4" clearable>
            <template slot="append">
              <img class="login-validcode-img" :src="validcodeImg" @click="changeValidcode">
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="submitLogin">{{translateKey('logIn')}}</el-button>
        </el-form-item>
        <lang-select class="set-language"/>
      </el-form>
    </div>
  </div>
</template>

<script>
  import * as API_common from '@/api/common'
  import 'particles.js'
  import particlesjsConfig from '@/assets/particlesjs-config.json'
  import LangSelect from '@/components/LangSelect'
  import Storage from '@/utils/storage'
  import uuidv1 from 'uuid/v1'

  export default {
    components: { LangSelect },
    name: 'login',
    data() {
      return {
        loading: false,
        loginForm: {
          username: '',
          password: '',
          validcode: ''
        },
        loginRules: {
          username: [
            { required: true, message: this.translateKey('val_username'), trigger: 'blur' }
          ],
          password: [
            { required: true, message: this.translateKey('val_password'), trigger: 'blur' }
          ],
          validcode: [
            { required: true, message: this.translateKey('val_validcode'), trigger: 'blur' }
          ]
        },
        validcodeImg: '',
        uuid: Storage.getItem('admin_uuid')
      }
    },
    mounted() {
      const uuid = Storage.getItem('admin_uuid')
      if (uuid) {
        this.uuid = uuid
      } else {
        const _uuid = uuidv1()
        this.uuid = _uuid
        Storage.setItem('admin_uuid', _uuid, { expires: 30 })
      }
      this.changeValidcode()
      this.loadParticles()
    },
    methods: {
      /** 翻译 */
      translateKey(key) {
        return this.$t('login.' + key)
      },
      /** 加载背景插件 */
      loadParticles() {
        window.particlesJS('login-bg', particlesjsConfig)
      },
      /** 更换图片验证码 */
      changeValidcode() {
        this.validcodeImg = API_common.getValidateCodeUrl('LOGIN', this.uuid)
      },
      /** 表单提交 */
      submitLogin() {
        this.$refs.loginForm.validate((valid) => {
          if (valid) {
            const params = this.MixinClone(this.loginForm)
            params.uuid = this.uuid
            params.captcha = params.validcode
            delete params.validcode
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
            this.$message.error(this.translateKey('val_form'))
            return false
          }
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../styles/mixin";
  .login-container {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
  }

  .bg {
    position: absolute;
    z-index: -1;
    width: 100%;
    height: 100%;
  }

  $form_width: 325px;
  .login-form {
    position: absolute;
    z-index: 1;
    left: 50%;
    top: 100px;
    padding: 20px 15px;
    margin-left: -(($form_width+30px)/2);
    width: $form_width;
    background-color: #fff;
    @include box-shadow(0 0 15px 2px #d8dce5)
  }
  .login-logo {
    width: 100%;
    height: 110px;
  }
  .login-logo-img {
    width: 300px;
    height: 100px;
    margin: 0 auto;
    display: block;
  }

  .login-input {
    margin-top: 20px;
    .el-button {
      width: 100%;
    }
    /deep/ .el-input-group__append {
      padding: 0;
      margin: 0;
      border: 0;
    }
    .login-validcode-img {
      width: 90px;
      height: 32px;
      display: block;
      cursor: pointer;
    }
  }
</style>
