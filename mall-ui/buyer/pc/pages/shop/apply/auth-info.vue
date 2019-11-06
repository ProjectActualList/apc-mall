<template>
  <div id="auth-info" class="item-container">
    <div class="content">
      <el-form
        :model="authInfoForm"
        :rules="authInfoRules"
        ref="authInfoForm"
        label-width="180px"
        size="small"
      >
        <h5 class="item-title">营业执照信息</h5>
        <el-form-item label="法定代表人姓名：" prop="legal_name">
          <el-input v-model.trim="authInfoForm.legal_name" :maxlength="20" clearable></el-input>
        </el-form-item>
        <el-form-item label="法定代表人身份证：" prop="legal_id">
          <el-input v-model.trim="authInfoForm.legal_id" :maxlength="18" clearable></el-input>
        </el-form-item>
        <el-form-item label="法人身份证电子版：" prop="legal_img">
          <el-upload
            :action="MixinUploadApi"
            :on-success="(res) => { authInfoForm.legal_img = res.url }"
            :on-remove="() => { authInfoForm.legal_img = '' }"
            :limit="1"
            :file-list="authInfoForm.legal_img ? [{name: '法人身份证', url: authInfoForm.legal_img}] : []"
            list-type="picture">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="营业执照编号：" prop="license_num">
          <el-input v-model.trim="authInfoForm.license_num" :maxlength="30" clearable></el-input>
        </el-form-item>
        <el-form-item label="营业执照所在地：" prop="license_region">
          <en-region-picker :api="MixinRegionApi" :default="defaultRegions" @changed="(object) => { authInfoForm.license_region = object.last_id }"/>
        </el-form-item>
        <el-form-item label="营业执照详细地址：" prop="license_add">
          <el-input v-model.trim="authInfoForm.license_add" :maxlength="100" clearable></el-input>
        </el-form-item>
        <el-form-item label="成立日期：" prop="establish_date">
          <el-date-picker
            v-model="authInfoForm.establish_date"
            type="date"
            :editable="false"
            value-format="timestamp"
            placeholder="选择成立日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="营业执照有效期：" prop="licence_start" class="licence-range">
          <el-date-picker
            v-model="authInfoForm.licence_start"
            type="date"
            :editable="false"
            value-format="timestamp"
            placeholder="开始日期">
          </el-date-picker>
        </el-form-item>
        <div style="width: 400px;margin: 0 auto;">
          <span style="margin-left: 180px;">至</span>
        </div>
        <el-form-item label="" prop="licence_end" class="licence-range">
          <el-date-picker
            v-model="authInfoForm.licence_end"
            type="date"
            :editable="false"
            value-format="timestamp"
            placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="法定经营范围：" prop="scope">
          <el-input v-model.trim="authInfoForm.scope" :maxlength="100" clearable></el-input>
        </el-form-item>
        <el-form-item label="营业执照电子版：" prop="licence_img">
          <el-upload
            :action="MixinUploadApi"
            :on-success="(res) => { authInfoForm.licence_img = res.url }"
            :on-remove="() => { authInfoForm.licence_img = '' }"
            :limit="1"
            :file-list="authInfoForm.licence_img ? [{name: '营业执照', url: authInfoForm.licence_img}] : []"
            list-type="picture">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件</div>
          </el-upload>
        </el-form-item>
        <h5 class="item-title">组织机构代码证</h5>
        <el-form-item label="组织机构代码：" prop="organization_code">
          <el-input v-model.trim="authInfoForm.organization_code" :maxlength="50" clearable></el-input>
        </el-form-item>
        <el-form-item label="组织机构代码证电子版：" prop="code_img">
          <el-upload
            :action="MixinUploadApi"
            :on-success="(res) => { authInfoForm.code_img = res.url }"
            :on-remove="() => { authInfoForm.code_img = '' }"
            :limit="1"
            :file-list="authInfoForm.code_img ? [{name: '组织机构代码证', url: authInfoForm.code_img}] : []"
            list-type="picture">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件</div>
          </el-upload>
        </el-form-item>
        <h5 class="item-title">一般纳税人证明</h5>
        <el-form-item label="一般纳税人证明：" prop="taxes_img">
          <el-upload
            :action="MixinUploadApi"
            :on-success="(res) => { authInfoForm.taxes_img = res.url }"
            :on-remove="() => { authInfoForm.taxes_img = '' }"
            :limit="1"
            :file-list="authInfoForm.taxes_img ? [{name: '一般纳税人证明', url: authInfoForm.taxes_img}] : []"
            list-type="picture">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件</div>
          </el-upload>
        </el-form-item>
      </el-form>
    </div>
    <div class="next-btns">
      <el-button size="small" @click="$router.back()">上一步</el-button>
      <el-button size="small" @click="handleNextStep">下一步</el-button>
    </div>
  </div>
</template>

<script>
  import { RegExp } from '@/ui-utils'
  import * as API_Shop from '@/api/shop'
  import EnRegionPicker from "@/components/RegionPicker"
  import mixin from './checkStatusMixin'
  export default {
    name: "auth-info",
    middleware: 'auth-seller',
    mixins: [mixin],
    components: { EnRegionPicker },
    data() {
      const req_rule = (message, trigger) => ({ required: true, message, trigger: trigger || 'blur' })
      const len_rule = (min, max) => ({ min, max, message: `'长度在 ${min} 到 ${max} 个字符` })
      return {
        defaultRegions: null,
        /** 基础信息 表单 */
        authInfoForm: {
          legal_name: '',
          legal_id: '',
          legal_img: '',
          license_num: '',
          establish_date: '',
          licence_start: '',
          licence_end: '',
          scope: '',
          licence_img: '',
          organization_code: '',
          code_img: '',
          taxes_img: '',
          license_region: '',
          license_add: ''
        },
        /** 基础信息 表单规则 */
        authInfoRules: {
          legal_name: [ req_rule('请输入法定代表人姓名！'), len_rule(1, 20) ],
          legal_id: [
            req_rule('请输入法定代表人身份证号！'),
            {
              validator: (rule, value, callback) => {
                if (!RegExp.IDCard.test(value)) {
                  callback(new Error('身份证号格式不正确'))
                } else {
                  callback()
                }
              },
              trigger: 'blur'
            }
          ],
          legal_img: [ req_rule('请上传法人身份证电子版！')],
          license_num: [ req_rule('请输入营业执照编号！') ],
          license_region: [ req_rule('请选择营业执照所在地！') ],
          license_add: [ req_rule('请填写营业执照详细地址！') ],
          establish_date: [ req_rule('请选择成立日期！') ],
          licence_start: [
            req_rule('请选择开始日期！'),
            {
              validator: (rule, value, callback) => {
                if (this.authInfoForm.licence_end && value >= this.authInfoForm.licence_end) {
                  callback(new Error('开始时间不能晚于结束时间！'))
                } else {
                  callback()
                }
              },
              trigger: 'blur'
            }
          ],
          licence_end: [
            req_rule('请选择结束日期！'),
            {
              validator: (rule, value, callback) => {
                if (this.authInfoForm.licence_start && value <= this.authInfoForm.licence_start) {
                  callback(new Error('结束时间不能早于开始时间！'))
                } else {
                  callback()
                }
              },
              trigger: 'blur'
            }
          ],
          scope: [ req_rule('请输入法定经营范围！') ],
          licence_img: [ req_rule('请上传营业执照电子版！') ]
        }
      }
    },
    mounted() {
      API_Shop.getApplyShopInfo().then(response => {
        if (!response || response.company_name === null) {
          this.$router.replace({ name: 'shop-apply' })
          return false
        }
        const { establish_date, licence_start, licence_end } = response
        establish_date && (response.establish_date *= 1000)
        licence_start && (response.licence_start *= 1000)
        licence_end && (response.licence_end *= 1000)
        Object.keys(this.authInfoForm).forEach(key => this.authInfoForm[key] = response[key])
        const { license_province_id, license_city_id, license_county_id, license_town_id } = response
        this.defaultRegions = [license_province_id, license_city_id, license_county_id, license_town_id]
      })
    },
    methods: {
      /** 下一步 */
      handleNextStep() {
        this.$refs['authInfoForm'].validate((valid) => {
          if (valid) {
            const params = JSON.parse(JSON.stringify(this.authInfoForm))
            params.establish_date /= 1000
            params.licence_start /= 1000
            params.licence_end /= 1000
            API_Shop.applyShopStep(2, params).then(response => {
              this.$router.push({ name: 'shop-apply-financial-qualification' })
            })
          } else {
            this.$message.error('表单填写有误，请核对！')
            return false
          }
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .item-container {
    /deep/ .el-form { width: 100% }
    /deep/ .el-form-item {
      width: 410px;
      margin-left: auto;
      margin-right: auto;
    }
    .item-title {
      font-size: 22px;
      padding-left: 20px;
      padding-bottom: 20px;
    }
    /deep/ .el-date-editor {
      width: 230px;
    }
  }
</style>
