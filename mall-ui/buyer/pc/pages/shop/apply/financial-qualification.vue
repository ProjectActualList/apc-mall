<template>
  <div id="financial-qualification" class="item-container">
    <div class="content">
      <el-form
        :model="finlQuafForm"
        :rules="finlQuafRules"
        ref="finlQuafForm"
        label-width="180px"
        size="small"
      >
        <h5 class="item-title">开户行银行许可证</h5>
        <el-form-item label="银行开户名：" prop="bank_account_name">
          <el-input v-model.trim="finlQuafForm.bank_account_name" :maxlength="30" clearable></el-input>
        </el-form-item>
        <el-form-item label="公司银行账号：" prop="bank_number">
          <el-input v-model.trim="finlQuafForm.bank_number" :maxlength="19" clearable></el-input>
        </el-form-item>
        <el-form-item label="开户银行支行名称：" prop="bank_name">
          <el-input v-model.trim="finlQuafForm.bank_name" :maxlength="18" clearable></el-input>
        </el-form-item>
        <el-form-item label="开户银行所在地：" prop="bank_region">
          <en-region-picker :api="MixinRegionApi" :default="defaultRegions" @changed="(object) => { finlQuafForm.bank_region = object.last_id }"/>
        </el-form-item>
        <el-form-item label="开户银行许可证电子版：" prop="bank_img">
          <el-upload
            :action="MixinUploadApi"
            :on-success="(res) => { finlQuafForm.bank_img = res.url }"
            :on-remove="() => { finlQuafForm.bank_img = '' }"
            :limit="1"
            :file-list="finlQuafForm.bank_img ? [{name: '开户银行许可证', url: finlQuafForm.bank_img}] : []"
            list-type="picture">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件</div>
          </el-upload>
        </el-form-item>
        <h5 class="item-title">税务登记证</h5>
        <el-form-item label="税务登记证号：" prop="taxes_certificate_num">
          <el-input v-model.trim="finlQuafForm.taxes_certificate_num" :maxlength="30" clearable></el-input>
        </el-form-item>
        <el-form-item label="纳税人识别号：" prop="taxes_distinguish_num">
          <el-input v-model.trim="finlQuafForm.taxes_distinguish_num" :maxlength="20" clearable></el-input>
        </el-form-item>
        <el-form-item label="税务登记证电子版：" prop="taxes_certificate_img">
          <el-upload
            :action="MixinUploadApi"
            :on-success="(res) => { finlQuafForm.taxes_certificate_img = res.url }"
            :on-remove="() => { finlQuafForm.taxes_certificate_img = '' }"
            :limit="1"
            :file-list="finlQuafForm.taxes_certificate_img ? [{name: '税务登记证', url: finlQuafForm.taxes_certificate_img}] : []"
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
    name: "financial-qualification",
    middleware: 'auth-seller',
    mixins: [mixin],
    components: { EnRegionPicker },
    data() {
      const req_rule = (message, trigger) => ({ required: true, message, trigger: trigger || 'blur' })
      return {
        defaultRegions: null,
        /** 基础信息 表单 */
        finlQuafForm: {
          bank_account_name: '',
          bank_number: '',
          bank_name: '',
          bank_img: '',
          taxes_certificate_num: '',
          taxes_distinguish_num: '',
          taxes_certificate_img: '',
          bank_region: ''
        },
        /** 基础信息 表单规则 */
        finlQuafRules: {
          bank_account_name: [ req_rule('请输入银行开户名！') ],
          bank_number: [ req_rule('请输入公司银行账号！') ],
          bank_name: [ req_rule('请输入开户银行支行名称！')],
          bank_region: [ req_rule('请选择开户银行所在地！') ],
          bank_img: [ req_rule('请上传开户银行许可证电子版！') ],
          taxes_distinguish_num: [
            req_rule('请输入纳税人识别号！'),
            {
              validator: (rule, value, callback) => {
                if (!RegExp.TINumber.test(value)) {
                  callback(new Error('纳税人识别号格式有误！'))
                } else {
                  callback()
                }
              },
              trigger: 'blur'
            }
          ],
          taxes_certificate_img: [ req_rule('请上税务登记证电子版！') ]
        }
      }
    },
    mounted() {
      API_Shop.getApplyShopInfo().then(response => {
        if (!response || response.legal_name === null) {
          this.$router.replace({ name: 'shop-apply' })
          return false
        }
        Object.keys(this.finlQuafForm).forEach(key => this.finlQuafForm[key] = response[key])
        const { bank_province_id, bank_city_id, bank_county_id, bank_town_id } = response
        this.defaultRegions = [bank_province_id, bank_city_id, bank_county_id, bank_town_id]
        console.log(this.finlQuafForm)
      })
    },
    methods: {
      /** 下一步 */
      handleNextStep() {
        this.$refs['finlQuafForm'].validate((valid) => {
          if (valid) {
            API_Shop.applyShopStep(3, this.finlQuafForm).then(response => {
              this.$router.push({ name: 'shop-apply-shop-info' })
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
