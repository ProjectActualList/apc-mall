<template>
  <div class="container">
    <el-tabs type="border-card">
      <el-tab-pane label="Android推送设置">
        <el-form :model="androidForm" :rules="androidRules" ref="androidForm" label-width="145px" style="width: 500px">
          <el-form-item prop="android_push_key" label="Android Key：">
            <el-input v-model="androidForm.android_push_key"></el-input>
          </el-form-item>
          <el-form-item prop="android_push_secret" label="Android Secret：">
            <el-input v-model="androidForm.android_push_secret" type="textarea" :autosize="{ minRows: 2, maxRows: 4}"></el-input>
          </el-form-item>
          <el-form-item prop="android_goods_activity" label="Android Activity：">
            <el-input v-model="androidForm.android_goods_activity"></el-input>
          </el-form-item>
          <el-form-item label="">
            <el-button type="primary" @click="handleSaveEdit('androidForm')">保存设置</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="IOS推送设置">
        <el-form :model="iosForm" :rules="iosRules" ref="iosForm" label-width="145px" style="width: 500px">
          <el-form-item prop="ios_push_key" label="IOS Key：">
            <el-input v-model="iosForm.ios_push_key"></el-input>
          </el-form-item>
          <el-form-item prop="ios_push_secret" label="IOS Secret：">
            <el-input v-model="iosForm.ios_push_secret" type="textarea" :autosize="{ minRows: 2, maxRows: 4}"></el-input>
          </el-form-item>
          <el-form-item label="">
            <el-button type="primary" @click="handleSaveEdit('iosForm')">保存设置</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <!--<el-tab-pane label="RN推送设置">RN推送设置</el-tab-pane>-->
    </el-tabs>
  </div>
</template>

<script>
  import * as API_Push from '@/api/push'

  export default {
    name: 'appPush',
    data() {
      return {
        pushSettings: {},
        androidForm: {
          android_push_key: '',
          android_push_secret: '',
          android_goods_activity: ''
        },
        androidRules: {
          android_push_key: [this.MixinRequired('Android Key不能为空！')],
          android_push_secret: [this.MixinRequired('Android Secret不能为空！')],
          android_goods_activity: [this.MixinRequired('Android Activity不能为空！')]
        },
        iosForm: {
          ios_push_key: '',
          ios_push_secret: ''
        },
        iosRules: {
          ios_push_key: [this.MixinRequired('IOS Key不能为空！')],
          ios_push_secret: [this.MixinRequired('IOS Secret不能为空！')]
        }
      }
    },
    mounted() {
      this.GET_PushSettings()
    },
    methods: {
      handleSaveEdit(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const params = this.MixinClone(this.pushSettings)
            const form = this[formName]
            Object.keys(form).forEach(key => { params[key] = form[key] })
            API_Push.editPushSettings(params).then(response => {
              this.$message.success('修改成功！')
              this.GET_PushSettings()
            })
          } else {
            this.$message.error('表单填写有误，请核对！')
            return false
          }
        })
      },
      /** 获取推送设置 */
      GET_PushSettings() {
        API_Push.getPushSettings().then(response => {
          this.pushSettings = response
          Object.keys(this.androidForm).forEach(key => { this.androidForm[key] = response[key] })
          Object.keys(this.iosForm).forEach(key => { this.iosForm[key] = response[key] })
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .container {
    padding: 10px;
    background-color: #fff;
  }
</style>
