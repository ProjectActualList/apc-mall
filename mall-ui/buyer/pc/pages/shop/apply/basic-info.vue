<template>
  <div id="basic-info" class="item-container">
    <h2 class="title">基本信息</h2>
    <div class="content">
      <el-form
        :model="basicInfoForm"
        :rules="basicInfoRules"
        ref="basicInfoForm"
        label-width="120px"
        style="width: 350px"
        size="small"
        class="basic-info-form"
      >
        <el-form-item label="公司名称：" prop="company_name">
          <el-input v-model="basicInfoForm.company_name" clearable :maxlength="20"></el-input>
        </el-form-item>
        <el-form-item label="公司地址：" prop="company_address">
          <el-input v-model="basicInfoForm.company_address" clearable :maxlength="100"></el-input>
        </el-form-item>
        <el-form-item label="公司电话：" prop="company_phone">
          <el-input v-model="basicInfoForm.company_phone" clearable :maxlength="13"></el-input>
        </el-form-item>
        <el-form-item label="公司邮箱：" prop="company_email">
          <el-input v-model="basicInfoForm.company_email" clearable :maxlength="80"></el-input>
        </el-form-item>
        <el-form-item label="员工总数：" prop="employee_num">
          <el-input v-model="basicInfoForm.employee_num" :maxlength="10" clearable>
            <el-button slot="append">人</el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="注册资金：" prop="reg_money">
          <el-input v-model="basicInfoForm.reg_money" :maxlength="10" clearable>
            <el-button slot="append">万</el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="联系人姓名：" prop="link_name">
          <el-input v-model="basicInfoForm.link_name" :maxlength="20" clearable></el-input>
        </el-form-item>
        <el-form-item label="联系人电话：" prop="link_phone">
          <el-input v-model="basicInfoForm.link_phone" :maxlength="11" clearable></el-input>
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
  import mixin from './checkStatusMixin'
  export default {
    name: "basic-info",
    middleware: 'auth-seller',
    mixins: [mixin],
    data() {
      const req_rule = (message) => ({ required: true, message, trigger: 'blur' })
      return {
        /** 基础信息 表单 */
        basicInfoForm: {
          company_name: '',
          company_address: '',
          company_phone: '',
          company_email: '',
          employee_num: '',
          reg_money: '',
          link_name: '',
          link_phone: ''
        },
        /** 基础信息 表单规则 */
        basicInfoRules: {
          company_name: [req_rule('请输入公司名称')],
          company_address: [req_rule('请输入公司地址')],
          company_phone: [
            req_rule('请输入公司电话'),
            { validator: (rule, value, callback) => {
                if (!RegExp.TEL.test(value)) {
                  callback(new Error('请输入固定电话，例如：010-8888888'))
                } else {
                  callback()
                }
              },
              trigger: 'blur'
            }
          ],
          company_email: [
            req_rule('请输入公司邮箱'),
            { validator: (rule, value, callback) => {
              if (!RegExp.email.test(value)) {
                callback(new Error('邮箱格式不正确！'))
              } else {
                callback()
              }
              },
              trigger: 'blur'
            }
          ],
          employee_num: [
            req_rule('请输入员工人数'),
            { validator: (rule, value, callback) => {
              if (!RegExp.integer.test(value)) {
                callback(new Error('员工人数应为正整数，且不为零！'))
              } else {
                callback()
              }
              },
              trigger: 'blur'
            }
          ],
          reg_money: [
            req_rule('请输入注册金额'),
            { validator: (rule, value, callback) => {
                if (!RegExp.integer.test(value)) {
                  callback(new Error('注册资金应为正整数，且不为零！'))
                } else {
                  callback()
                }
              },
              trigger: 'blur'
            }
          ],
          link_name: [req_rule('请输入联系人姓名')],
          link_phone: [
            req_rule('请输入联系人电话'),
            { validator: (rule, value, callback) => {
                if (!RegExp.mobile.test(value)) {
                  callback(new Error('联系人电话格式不正确！'))
                } else {
                  callback()
                }
              },
              trigger: 'blur'
            }
          ]
        }
      }
    },
    mounted() {
      API_Shop.getApplyShopInfo().then(response => {
        Object.keys(this.basicInfoForm).forEach(key => this.basicInfoForm[key] = response[key])
      })
    },
    methods: {
      /** 下一步 */
      handleNextStep() {
        this.$refs['basicInfoForm'].validate((valid) => {
          if (valid) {
            this.basicInfoForm.reg_monety = this.basicInfoForm.reg_money
            API_Shop.applyShopStep(1, this.basicInfoForm).then(response => {
              this.$router.push({ name: 'shop-apply-auth-info' })
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
  .basic-info-form {
    padding-bottom: 35px;
  }
</style>
