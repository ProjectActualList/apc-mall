<template>
  <div>
    <el-menu class="navbar" mode="horizontal">
      <div class="navbar-left">
        <hamburger class="hamburger-container" :toggleClick="toggleSideBar" :isActive="sidebar.opened"></hamburger>
        <breadcrumb class="breadcrumb-container"></breadcrumb>
      </div>

      <div class="right-menu">
        <el-tooltip effect="dark" content="全屏" placement="bottom">
          <screenfull class="screenfull right-menu-item"></screenfull>
        </el-tooltip>

        <lang-select class="international right-menu-item"></lang-select>

        <!--<el-tooltip effect="dark" content="换肤" placement="bottom">-->
        <!--<theme-picker class="theme-switch right-menu-item"></theme-picker>-->
        <!--</el-tooltip>-->

        <el-dropdown class="avatar-container right-menu-item" trigger="click">
          <div class="avatar-wrapper">
            <img class="user-avatar" :src="user.face">
            <i class="el-icon-caret-bottom"></i>
          </div>
          <el-dropdown-menu slot="dropdown">
            <router-link to="/">
              <el-dropdown-item>
                首页
              </el-dropdown-item>
            </router-link>
            <a target='_blank' :href="MixinBuyerDomain">
              <el-dropdown-item>
                浏览网站
              </el-dropdown-item>
            </a>
            <span @click="changePassword">
            <el-dropdown-item>修改密码</el-dropdown-item>
          </span>
            <span @click="logout">
            <el-dropdown-item divided>退出登录</el-dropdown-item>
          </span>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-menu>
    <el-dialog
      title="修改密码"
      :visible.sync="dialogVisible"
      width="500px"
      append-to-body
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form :model="form" :rules="formRules" ref="form" label-width="100px">
        <el-form-item label="头像" prop="face">
          <el-upload
            :action="MixinUploadApi"
            list-type="picture"
            :on-success="(res) => { form.face = res.url }"
            :on-remove="() => { form.face = '' }"
            :file-list="form.face ? [{name: '头像', url: form.face}] : []"
            :multiple="false"
            :limit="1"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <span slot="tip" class="el-upload__tip">&nbsp;建议上传jpg/png文件，且不超过1MB</span>
          </el-upload>
        </el-form-item>
        <el-form-item label="原密码" prop="old_passwprd">
          <el-input v-model="form.old_passwprd" placeholder="不修改则不填"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input type="password" v-model="form.password" placeholder="不修改则不填"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="rep_password">
          <el-input type="password" v-model="form.rep_password" placeholder="不修改则不填"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import Breadcrumb from '@/components/Breadcrumb'
  import Hamburger from '@/components/Hamburger'
  import Screenfull from '@/components/Screenfull'
  import LangSelect from '@/components/LangSelect'
  import ThemePicker from '@/components/ThemePicker'
  import { RegExp } from '~/ui-utils'
  import * as API_Auth from '@/api/auth'

  export default {
    components: {
      Breadcrumb,
      Hamburger,
      Screenfull,
      LangSelect,
      ThemePicker
    },
    data() {
      const user = this.MixinClone(this.$store.getters.user)
      return {
        dialogVisible: false,
        form: {
          face: user.face,
          old_passwprd: '',
          password: '',
          rep_password: ''
        },
        formRules: {
          face: [this.MixinRequired('请上传头像！')],
          old_passwprd: [{ required: false, message: '请输入原密码！', trigger: 'blur' }],
          password: [
            { required: false, message: '请输入新密码！', trigger: 'blur' },
            { validator: (rule, value, callback) => {
              if (value && !RegExp.password.test(value)) {
                callback(new Error('密码应为6-20位英文或数字！'))
              } else {
                callback()
              }
            } }
          ],
          rep_password: [
            { required: false, message: '请确认您的新密码！', trigger: 'blur' },
            { validator: (rule, value, callback) => {
              if (value && this.form.password !== value) {
                callback(new Error('两次输入不一致！'))
              } else {
                callback()
              }
            } }
          ]
        }
      }
    },
    computed: {
      ...mapGetters(['sidebar', 'user'])
    },
    watch: {
      form: {
        handler: function(newVal) {
          const { old_passwprd, password, rep_password } = newVal
          const required = !!(old_passwprd || password || rep_password)
          this.formRules.old_passwprd[0].required = required
          this.formRules.password[0].required = required
          this.formRules.rep_password[0].required = required
        },
        deep: true
      }
    },
    methods: {
      /** 提交修改密码表单 */
      submitForm() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            const params = this.MixinClone(this.form)
            if (!params.old_passwprd) {
              delete params.old_passwprd
              delete params.password
              delete params.rep_password
            }
            API_Auth.editUserInfo(params).then(() => {
              this.dialogVisible = false
              this.$store.dispatch('fedLogoutAction')
              location.reload()
            })
          } else {
            this.$message.error('表单填写有误，请核对！')
          }
        })
      },
      toggleSideBar() {
        this.$store.dispatch('toggleSideBar')
      },
      changePassword() {
        this.dialogVisible = true
      },
      logout() {
        this.$confirm('确定退出吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.dispatch('logoutAction').then(() => {
            location.replace('/login')// 为了重新实例化vue-router对象 避免bug
          })
        }).catch(() => {})
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../../styles/variables";
  .navbar {
    height: 80px;
    border-radius: 0 !important;
    background-color: $nav_bg !important;
    border-color: $nav_bg !important;
    .navbar-left {
      float: left;
      display: flex;
      height: 80px;
      align-items: center;
    }
    .hamburger-container {
      padding: 0 10px;
    }
    .breadcrumb-container {
      float: left;
      /deep/ {
        .el-breadcrumb__separator,
        .el-breadcrumb__item .el-breadcrumb__inner a {
          color: $nav_text !important;
        }
        .el-breadcrumb__item .el-breadcrumb__inner .no-redirect {
          color: $nav_text_active !important;
        }
      }
    }
    .right-menu {
      display: flex;
      align-items: center;
      float: right;
      height: 80px;
      /deep/ svg {
        fill: $nav_text_active !important;
      }
      &:focus{
        outline: none;
      }
      .right-menu-item {
        display: inline-block;
        margin: 0 15px;
      }
      .avatar-container {
        height: 50px;
        margin-right: 30px;
        .avatar-wrapper {
          cursor: pointer;
          margin-top: 5px;
          position: relative;
          .user-avatar {
            width: 40px;
            height: 40px;
            border-radius: 100%;
          }
          .el-icon-caret-bottom {
            position: absolute;
            right: -20px;
            top: 25px;
            font-size: 12px;
          }
          /deep/ .el-icon-caret-bottom {
            color: $nav_text_active !important;
          }
        }
      }
    }
  }
</style>
