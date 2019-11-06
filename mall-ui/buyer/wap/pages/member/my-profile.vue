<template>
  <div id="my-profile">
    <nav-bar title="我的资料"></nav-bar>
    <div class="profile-container">
      <van-cell-group :border="false">
        <van-cell title="头像" is-link class="face-cell">
          <en-face :url="profileForm.face" class="img-face"/>
          <input id="update-face" class="update-face" type="file" accept="image/jpg,image/jpeg,image/png" @change="handleUpdateFace">
        </van-cell>
        <van-cell title="用户名" :value="profileForm.uname"/>
        <van-cell title="昵称" :value="profileForm.nickname" is-link @click="showEditNickname = true"/>
        <van-cell title="性别" :value="profileForm.sex === 1 ? '男' : '女'" is-link @click="showSexActionsheet = true"/>
        <van-cell title="生日" :value="formatterBirthdayDate()" is-link @click="showBirthdayActionsheet = true"/>
      </van-cell-group>
    </div>
    <!--修改昵称弹框-->
    <van-dialog
      v-model="showEditNickname"
      title="请输入昵称"
      show-cancel-button
      :before-close="beforeNicknameClose"
      @confirm="profileForm.nickname = nickname"
    >
      <van-field v-model="nickname" clearable placeholder="2-20位汉字、字母或数字"/>
    </van-dialog>
    <!--修改性别弹出菜单-->
    <van-actionsheet v-model="showSexActionsheet" :actions="sexActions" cancel-text="取消"/>
    <van-actionsheet v-model="showBirthdayActionsheet">
      <van-datetime-picker
        v-model="birthday"
        type="date"
        :min-date="new Date(1900,1,1)"
        :max-date="new Date()"
        @cancel="showBirthdayActionsheet = false"
        @confirm="handleConfirmBirthday"
      />
    </van-actionsheet>
    <!--登录-->
    <div class="big-btn">
      <van-button size="large" @click="submitProfile">保存修改</van-button>
    </div>
    <div v-show="show_cropper" class="cropper-box">
      <no-ssr>
        <vueCropper
          ref="cropper"
          outputType="png"
          :img="cropper_img"
          :autoCropWidth="200"
          :autoCropHeight="200"
          :info="false"
          fixed
          centerBox
          autoCrop
        ></vueCropper>
      </no-ssr>
      <div class="confirm-btns">
        <van-button type="default" @click="show_cropper = false">取消裁剪</van-button>
        <van-button type="primary" @click="handleCropper">确认裁剪</van-button>
      </div>
    </div>
  </div>
</template>

<script>
  import '@/static/lrz/lrz.all.bundle'
  import Vue from 'vue'
  import { mapGetters, mapActions } from 'vuex'
  import { Foundation, RegExp } from '@/ui-utils'
  import request, { Method } from '@/utils/request'
  export default {
    name: 'my-profile',
    head() {
      return {
        title: `我的资料-${this.site.site_name}`
      }
    },
    data() {
      const user = this.$store.state.user.user
      return {
        /** 个人资料 表单 */
        profileForm: user ? JSON.parse(JSON.stringify(user)) : {},
        // 显示修改昵称 dialog
        showEditNickname: false,
        // 昵称
        nickname: user.nickname || '',
        // 显示性别选择菜单
        showSexActionsheet: false,
        // 性别选择菜单选项
        sexActions: [
          { name: '男', callback: (action) => {
            this.profileForm.sex = 1
            this.showSexActionsheet = false
          }},
          { name: '女', callback: () => {
            this.profileForm.sex = 0
            this.showSexActionsheet = false
          }}
        ],
        // 显示日期选择器
        showBirthdayActionsheet: false,
        // 生日
        birthday: user.birthday ? new Date(user.birthday * 1000) : new Date(),
        // 图片裁剪源
        cropper_img: '',
        // 显示图片裁剪
        show_cropper: false
      }
    },
    watch: {
      user(newVal, oldVal) {
        this.profileForm = newVal ? JSON.parse(JSON.stringify(newVal)) : {}
        this.birthday = newVal.birthday ? new Date(newVal.birthday * 1000) : new Date()
        this.nickname = newVal.nickname
      }
    },
    computed: {
      ...mapGetters({
        user: 'user'
      })
    },
    methods: {
      /** 头像发生改变 */
      handleUpdateFace(event) {
        const {files} = event.target
        if (files.length === 0) return
        const file = files[0]
        this.$confirm('该图片需要裁剪吗？选择取消将以原图上传。', () => {
          lrz(file).then(response => {
            this.cropper_img = response.base64
            this.show_cropper = true
            event.target.value = ''
          }).catch((e) => {
            this.$message.error('当前设备不支持上传图片，请到APP或PC端操作！')
          })
        }, () => {
          this.handleUpload(file, file.name)
          event.target.value = ''
        })
      },
      // 图片裁剪
      handleCropper() {
        this.$refs.cropper.getCropData(async data => {
          this.handleUpload(data)
        })
        this.show_cropper = false
      },
      // 上传文件
      handleUpload(file, filename = 'file') {
        const formData = new FormData()
        if (typeof file === 'string') {
          file = this.MixinBase64toBlob(file)
        }
        formData.append('file', file, filename)
        request({
          url: this.MixinUploadApi,
          method: 'POST',
          data: formData,
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }).then(response => {
          this.profileForm.face = response.url
        })
      },
      /** 确认生日选择 */
      handleConfirmBirthday() {
        this.profileForm.birthday = parseInt(new Date(this.birthday).getTime() / 1000)
        this.showBirthdayActionsheet = false
      },
      /** 昵称dialog关闭前 */
      beforeNicknameClose(action, done) {
        if (action === 'cancel') return done()
        const { nickname } = this
        if (!nickname) {
          this.$message.error('请输入昵称！')
          done(false)
        } else if (nickname.length < 2 || nickname.length > 20) {
          this.$message.error('长度应在2-20之间！')
          done(false)
        } else if (!RegExp.userName.test(nickname)) {
          this.$message.error('格式不正确！')
          done(false)
        } else {
          done()
        }
      },
      /** 保存资料提交表单 */
      submitProfile() {
        const { birthday } = this.profileForm
        if (isNaN(birthday) ) {
          this.$message.error('生日格式不正确！')
          return
        }
        this.saveUserInfo(this.profileForm).then(() => {
          this.$store.dispatch('user/getUserDataAction')
          this.$message.success('修改成功！')
        })
      },
      /** 格式化生日日期 */
      formatterBirthdayDate() {
        const { birthday } = this.profileForm
        return Foundation.unixToDate(birthday, 'yyyy-MM-dd')
      },
      ...mapActions({
        saveUserInfo: 'user/saveUserInfoAction'
      })
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /deep/ {
    .van-cell {
      align-items: center;
    }
  }
  .profile-container {
    padding-top: 46px;
  }
  .face-cell {
    padding-top: 5px;
    padding-bottom: 5px;
    .img-face {
      width: 50px;
      height: 50px;
    }
    .update-face {
      position: absolute;
      top: 0;
      right: 0;
      height: 100%;
      width: 100%;
      opacity: 0;
    }
  }
  .birthday-input {
    background-color: #fff;
    width: 100%;
    text-align: right;
    &::-webkit-calendar-picker-indicator {
      display: none;
    }
    &::-webkit-clear-button {
      display: none;
    }
  }
  .cropper-box {
    position: fixed;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    background-color: #836FFF;
  }
  /deep/ .confirm-btns {
    position: absolute;
    left: 50%;
    margin-left: -(186px / 2);
    bottom: 20px;
    .van-button:first-child {
      margin-right: 10px;
    }
  }
</style>
