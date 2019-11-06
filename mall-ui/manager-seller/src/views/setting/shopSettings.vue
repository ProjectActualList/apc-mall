<template>
  <div class="bg-settings">
    <el-form
      :model="shopDataForm"
      ref="shopDataForm"
      :rules="rules"
      label-width="200px"
      class="demo-ruleForm"
      status-icon>
      <!--店铺名称-->
      <el-form-item label="店铺名称：" prop="shop_name">
        <el-input v-model="shopDataForm.shop_name" @change="() => { shopDataForm.shop_name = shopDataForm.shop_name.trim() }" style="width: 200px;" auto-complete="off"></el-input>
      </el-form-item>
      <!--身份证号-->
      <el-form-item label="身份证号：" prop="legal_id">
        <span>{{ shopDataForm.legal_id }}</span>
      </el-form-item>
      <!--店铺地址-->
      <el-form-item label="店铺地址：" prop="shop_region">
        <en-region-picker :api="MixinRegionApi" :default="areas" @changed="(object) => { shopDataForm.shop_region = object.last_id }"></en-region-picker>
      </el-form-item>
      <!--详细地址-->
      <el-form-item label="详细地址：" prop="shop_add">
        <el-input v-model="shopDataForm.shop_add" @change="() => { shopDataForm.shop_add = shopDataForm.shop_add.trim() }"  style="width: 200px;" auto-complete="off"></el-input>
      </el-form-item>
      <!--联系电话-->
      <el-form-item label="联系电话：" prop="link_phone">
        <el-input v-model="shopDataForm.link_phone" style="width: 200px;" maxLength="11"></el-input>
      </el-form-item>
      <!--QQ-->
      <el-form-item label="QQ：" prop="shop_qq">
        <el-input v-model="shopDataForm.shop_qq" style="width: 200px;" placeholder="请设置店铺的客服QQ"></el-input>
        <span class="QQ-tip">（设置店铺的客服QQ）</span>
      </el-form-item>
      <!--店铺简介-->
      <el-form-item label="店铺简介：" prop="shop_desc">
        <UE ref="UE" :defaultMsg="shopDataForm.shop_desc" style="width: 80%;"></UE>
      </el-form-item>
      <!--店铺logo-->
      <el-form-item label="店铺logo：" prop="shop_logo">
        <el-upload
          class="upload-demo"
          key="shop_logo"
          :action="`${MixinUploadApi}?scene=shop`"
          :on-success="uploadSuccessLogo"
          :before-upload="handleImagesUrlBefore"
          :file-list="fileList_logo"
          ref="fileList_logo"
          list-type="picture">
          <el-button size="small" type="primary">
            上传<i class="el-icon-upload el-icon--right"></i>
          </el-button>
          <div slot="tip" class="el-upload__tip">
            此处为您的店铺logo，将显示在店铺Logo栏里。 （限制上传宽（200～300）x高（50～100）规格的图片！）
          </div>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSaveShopData('shopDataForm')">保存修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import * as API_Shop from '@/api/shop'
  import { UE } from '@/components'
  import { RegExp } from '~/ui-utils'
  export default {
    name: 'shopSetting',
    components: {
      [UE.name]: UE
    },
    data() {
      const validPhone = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入联系人电话'))
        } else if (!RegExp.mobile.test(value)) {
          callback(new Error('请输入正确的11位手机号码'))
        } else {
          callback()
        }
      }
      const validQQ = (rule, value, callback) => {
        if (value && !/^[0-9]\d*$/g.test(value)) {
          callback(new Error('请输入0-9之间的整数'))
        } else {
          callback()
        }
      }
      return {
        /** 店铺信息*/
        shopDataForm: {
          /** 店铺ID */
          shop_id: 0,

          /** 店铺名称 */
          shop_name: '',

          /** 身份证号*/
          legal_id: '',

          /** 店铺地址id */
          shop_region: '',

          /** 详细地址*/
          shop_add: '',

          /** 联系电话*/
          link_phone: '',

          /** QQ*/
          shop_qq: '',

          /** 店铺简介*/
          shop_desc: '',

          /** 店铺logo*/
          shop_logo: ''
        },

        /** 地区信息*/
        areas: [],

        /** 店铺logo图片存*/
        fileList_logo: [],

        /** 店铺banner图片存*/
        fileList_banner: [],

        /** 校验规则 校验必填 */
        rules: {
          /** 店铺名称 */
          shop_name: [
            { required: true, message: '请填写店铺名称', trigger: 'blur' },
            { whitespace: true, message: '店铺名称不可为纯空格', trigger: 'blur' },
            { max: 15, message: '店铺名称长度最多15个字符', trigger: 'blur' }
          ],

          /** 店铺地址 */
          shop_region: [
            { required: true, message: '请填写店铺名称', trigger: 'blur' }
          ],

          /** 详细地址 */
          shop_add: [
            { whitespace: true, message: '详细地址不可为纯空格', trigger: 'blur' },
            { max: 50, message: '详细地址长度最多50个字符', trigger: 'blur' }
          ],
          /** 联系人电话 */
          link_phone: [
            { required: true, message: '请填写联系人电话', trigger: 'blur' },
            { validator: validPhone, trigger: 'blur' }
          ],
          /** 店铺qq */
          shop_qq: [
            { required: false, trigger: 'change', validator: validQQ },
            { max: 20, message: '店铺QQ长度最多20个字符', trigger: 'change' }
          ]
        }
      }
    },
    mounted() {
      this.GET_ShopGradeData()
    },
    methods: {
      /** 获取店铺信息 */
      GET_ShopGradeData() {
        API_Shop.getShopData().then(response => {
          this.shopDataForm = { ...response }
          this.fileList_logo = [{ url: this.shopDataForm.shop_logo }]
          this.areas = [this.shopDataForm.shop_province_id, this.shopDataForm.shop_city_id,
            this.shopDataForm.shop_county_id || -1, this.shopDataForm.shop_town_id || -1]
        })
      },

      /** 保存店铺设置*/
      handleSaveShopData(formName) {
        this.shopDataForm.shop_desc = this.$refs['UE'].getUEContent()
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const _params = {
              shop_add: this.shopDataForm.shop_add,

              shop_name: this.shopDataForm.shop_name,

              link_phone: this.shopDataForm.link_phone,

              shop_logo: this.shopDataForm.shop_logo,

              shop_desc: this.shopDataForm.shop_desc || '',

              shop_qq: this.shopDataForm.shop_qq,

              shop_region: this.shopDataForm.shop_region
            }
            API_Shop.saveShopSettings(_params).then(() => {
              this.$message.success('保存店铺设置成功')
              this.GET_ShopGradeData()
            })
          }
        })
      },

      /** 图片上传之前的校验 */
      handleImagesUrlBefore(file) {
        return new Promise((resolve, reject) => {
          let reader = new FileReader()
          reader.onload = (event) => {
            let image = new Image()
            image.onload = () => {
              let width = image.width
              let height = image.height
              if (width > 300 || width < 200) {
                this.$message.error('图片宽度必须在200~300之间！')
                reject()
              }
              if (height > 100 || height < 50) {
                this.$message.error('图片高度必须在50~100之间！')
                reject()
              }
              resolve()
            }
            image.src = event.target.result
          }
          reader.readAsDataURL(file)
        })
      },

      /** 上传logo成功以后*/
      uploadSuccessLogo(response) {
        this.fileList_logo.shift()
        this.fileList_logo.push(response)
        this.shopDataForm.shop_logo = response.url
      }
    }
  }
</script>

<style lang="scss" type="text/scss" scoped>
  .bg-settings {
    background: #fff;
    border: 1px solid #FAFAFA;
    margin: 15px;
    padding: 10px;
  }
  /deep/ .el-form-item--medium .el-form-item__content, .el-form-item--medium .el-form-item__label {
    line-height: 24px;
  }
  /deep/ .upload-demo {
    width: 80%;
  }
  .QQ-tip {
    font-size: 13px;
    color: #796a6a;
  }
</style>
