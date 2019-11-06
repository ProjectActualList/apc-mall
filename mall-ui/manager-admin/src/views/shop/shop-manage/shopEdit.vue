<template>
  <div v-loading="loading">
    <el-form :model="shopForm" :rules="shopRules" ref="shopForm" inline label-width="130px">
      <el-tabs type="border-card" v-model="tableName" ref="tabs">
        <el-tab-pane label="基本信息" name="base">
          <el-form-item label="公司名称" prop="company_name">
            <el-input v-model="shopForm.company_name" :maxlength="50"></el-input>
          </el-form-item>
          <el-form-item label="公司地址" prop="company_address">
            <el-input v-model="shopForm.company_address" :maxlength="50"></el-input>
          </el-form-item>
          <el-form-item label="公司电话" prop="company_phone">
            <el-input v-model="shopForm.company_phone" :maxlength="20"></el-input>
          </el-form-item>
          <el-form-item label="员工总数" prop="employee_num">
            <el-input-number v-model="shopForm.employee_num" :min="1" :max="99999999"/>
          </el-form-item>
          <el-form-item label="注册资金" prop="reg_money">
            <el-input v-model="shopForm.reg_money">
              <template slot="append">万</template>
            </el-input>
          </el-form-item>
          <el-form-item label="联系姓名" prop="link_name">
            <el-input v-model="shopForm.link_name" :maxlength="20"></el-input>
          </el-form-item>
          <el-form-item label="联系电话" prop="link_phone">
            <el-input v-model="shopForm.link_phone" :maxlength="11"></el-input>
          </el-form-item>
          <el-form-item label="电子邮箱" prop="company_email">
            <el-input v-model="shopForm.company_email" :maxlength="50"></el-input>
          </el-form-item>
          <el-form-item label="自营店铺" prop="self_operated">
            <el-radio-group v-model="shopForm.self_operated">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="营业执照信息" name="legal">
          <el-form-item label="法人姓名" prop="legal_name">
            <el-input v-model="shopForm.legal_name" :maxlength="50"></el-input>
          </el-form-item>
          <el-form-item label="法人身份证号" prop="legal_id">
            <el-input v-model="shopForm.legal_id" :maxlength="50"></el-input>
          </el-form-item>
          <el-form-item label="营业执照号" prop="license_num">
            <el-input v-model="shopForm.license_num" :maxlength="50"></el-input>
          </el-form-item>
          <el-form-item label="法定经营范围" prop="scope">
            <el-input v-model="shopForm.scope" :maxlength="50"></el-input>
          </el-form-item>
          <el-form-item label="营业执照所在地" prop="license_region">
            <en-region-picker :api="MixinRegionApi" :default="defaultRegionLicense" @changed="(object) => { shopForm.license_region = object.last_id }"/>
          </el-form-item>
          <el-form-item label="营业执照详细地址 " prop="license_add">
            <el-input v-model="shopForm.license_add" :maxlength="50"></el-input>
          </el-form-item>
          <el-form-item label="成立日期" prop="establish_date">
            <el-date-picker
              class="license-date"
              v-model="shopForm.establish_date"
              type="date"
              align="center"
              :editable="false"
              value-format="timestamp"
              :picker-options="{ disabledDate(time) { return time.getTime() > Date.now() } }">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="营业执照有效期" prop="licence_start">
            <el-date-picker
              class="license-date"
              v-model="shopForm.licence_start"
              type="date"
              align="center"
              :editable="false"
              value-format="timestamp"
              :picker-options="{ disabledDate(time) { return time.getTime() > Date.now() } }">
            </el-date-picker>
            <el-date-picker
              class="license-date"
              v-model="shopForm.licence_end"
              type="date"
              align="center"
              :editable="false"
              value-format="timestamp"
              :picker-options="{ disabledDate(time) { return time.getTime() > Date.now() } }">
            </el-date-picker>
          </el-form-item>
          <br>
          <el-form-item label="法人身份证" prop="legal_img">
            <el-upload
              class="avatar-uploader"
              :action="MixinUploadApi"
              :show-file-list="false"
              :on-success="(res) => { shopForm.legal_img = res.url }">
              <img v-if="shopForm.legal_img" :src="shopForm.legal_img" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <a v-if="shopForm.legal_img" :href="shopForm.legal_img" target="_blank" class="see-original">查看原图</a>
          </el-form-item>
          <br>
          <el-form-item label="营业执照" prop="licence_img">
            <el-upload
              class="avatar-uploader"
              :action="MixinUploadApi"
              :show-file-list="false"
              :on-success="(res) => { shopForm.licence_img = res.url }">
              <img v-if="shopForm.licence_img" :src="shopForm.licence_img" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <a v-if="shopForm.licence_img" :href="shopForm.licence_img" target="_blank" class="see-original">查看原图</a>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="组织机构信息" name="organization">
          <el-form-item label="组织机构代码" prop="organization_code">
            <el-input v-model="shopForm.organization_code" :maxlength="50"></el-input>
          </el-form-item>
          <br>
          <el-form-item label="组织机构代码证件" prop="code_img">
            <el-upload
              class="avatar-uploader"
              :action="MixinUploadApi"
              :show-file-list="false"
              :on-success="(res) => { shopForm.code_img = res.url }">
              <img v-if="shopForm.code_img" :src="shopForm.code_img" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <a v-if="shopForm.code_img" :href="shopForm.code_img" target="_blank" class="see-original">查看原图</a>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="开户行银行许可证" name="bank">
          <el-form-item label="银行开户名" prop="bank_account_name">
            <el-input v-model="shopForm.bank_account_name" :maxlength="50"></el-input>
          </el-form-item>
          <el-form-item label="公司银行账号" prop="bank_number">
            <el-input v-model="shopForm.bank_number" :maxlength="19"></el-input>
          </el-form-item>
          <el-form-item label="开户银行支行名称" prop="bank_name">
            <el-input v-model="shopForm.bank_name" :maxlength="50"></el-input>
          </el-form-item>
          <el-form-item label="开户行所在地" prop="bank_region">
            <en-region-picker :api="MixinRegionApi" :default="defaultRegionBank" @changed="(object) => { shopForm.bank_region = object.last_id }"/>
          </el-form-item>
          <br>
          <el-form-item label="开户行许可证" prop="bank_img">
            <el-upload
              class="avatar-uploader"
              :action="MixinUploadApi"
              :show-file-list="false"
              :on-success="(res) => { shopForm.bank_img = res.url }">
              <img v-if="shopForm.bank_img" :src="shopForm.bank_img" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <a v-if="shopForm.bank_img" :href="shopForm.bank_img" target="_blank" class="see-original">查看原图</a>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="税务登记证" name="taxes">
          <el-form-item label="税务登记证号">
            <el-input v-model="shopForm.taxes_certificate_num" :maxlength="50"></el-input>
          </el-form-item>
          <el-form-item label="纳税人识别号" prop="taxes_distinguish_num">
            <el-input v-model="shopForm.taxes_distinguish_num" :maxlength="50"></el-input>
          </el-form-item>
          <br>
          <el-form-item label="一般纳税人证明" prop="taxes_img">
            <el-upload
              class="avatar-uploader"
              :action="MixinUploadApi"
              :show-file-list="false"
              :on-success="(res) => { shopForm.taxes_img = res.url }">
              <img v-if="shopForm.taxes_img" :src="shopForm.taxes_img" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <a v-if="shopForm.taxes_img" :href="shopForm.taxes_img" target="_blank" class="see-original">查看原图</a>
          </el-form-item>
          <br>
          <el-form-item label="税务登记证" prop="taxes_certificate_img">
            <el-upload
              class="avatar-uploader"
              :action="MixinUploadApi"
              :show-file-list="false"
              :on-success="(res) => { shopForm.taxes_certificate_img = res.url }">
              <img v-if="shopForm.taxes_certificate_img" :src="shopForm.taxes_certificate_img" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <a v-if="shopForm.taxes_certificate_img" :href="shopForm.taxes_certificate_img" target="_blank" class="see-original">查看原图</a>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="店铺信息" name="shop">
          <el-form-item label="店铺名称" prop="shop_name">
            <el-input v-model="shopForm.shop_name" :maxlength="50"></el-input>
          </el-form-item>
          <el-form-item label="店铺所在地" prop="shop_region">
            <en-region-picker :api="MixinRegionApi" :default="defaultRegionShop" @changed="(object) => { shopForm.shop_region = object.last_id }" />
          </el-form-item>
          <el-form-item label="佣金比例" prop="shop_commission">
            <el-input v-model="shopForm.shop_commission" :maxlength="50">
              <template slot="append">%</template>
            </el-input>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="经营类目" name="category">
          <el-form-item label="经营类目" prop="goods_management_category" class="form-item-cat">
            <el-checkbox :indeterminate="isIndeterminateCat" v-model="checkAllCat" @change="handleCheckAllCatChange">全选</el-checkbox>
            <div style="margin: 15px 0;"></div>
            <el-checkbox-group v-model="shopForm.goods_management_category" @change="handleCheckedCatsChange">
              <el-checkbox v-for="cat in categorys" :label="cat.id" :key="cat.id">{{ cat.name }}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-tab-pane>
      </el-tabs>
      <div class="save-btn-box">
        <el-button type="primary" @click="handleSaveEdit">{{ isAudit ? '通过并保存' : '保存修改'}}</el-button>
        <el-button v-if="isAudit" type="danger" @click="handleRefusePass">拒绝通过</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
  import * as API_Shop from '@/api/shop'
  import * as API_Category from '@/api/category'
  import { RegExp } from '~/ui-utils'

  export default {
    name: 'shopEdit',
    data() {
      return {
        tableName: 'base',
        shop_id: this.$route.params.id,
        isAudit: !!this.$route.query.audit,
        loading: false,
        shopForm: {},
        shopRules: {
          company_name: [this.MixinRequired('公司名称不能为空！')],
          company_address: [this.MixinRequired('公司地址不能为空！')],
          company_phone: [
            this.MixinRequired('公司电话不能为空！'),
            { validator: (rule, value, callback) => {
              if (!RegExp.TEL.test(value)) {
                callback(new Error('格式应为：010-8888888'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
            }
          ],
          employee_num: [this.MixinRequired('公司员工数不能为空！')],
          reg_money: [
            this.MixinRequired('注册资金不能为空！'),
            { validator: (rule, value, callback) => {
              if (!RegExp.money.test(value)) {
                callback(new Error('注册资金格式有误！'))
              } else {
                callback()
              }
            } }
          ],
          link_name: [this.MixinRequired('联系姓名不能为空！')],
          link_phone: [this.MixinRequired('联系电话不能为空！')],
          company_email: [this.MixinRequired('公司电子邮箱不能为空！')],
          legal_name: [this.MixinRequired('法人姓名不能为空！')],
          legal_id: [this.MixinRequired('法人身份证不能为空！')],
          license_num: [this.MixinRequired('营业执照不能为空！')],
          scope: [this.MixinRequired('法定经营范围不能为空！')],
          license_region: [this.MixinRequired('请选择营业执照所在地！')],
          license_add: [this.MixinRequired('营业执照详细地址不能为空！')],
          establish_date: [this.MixinRequired('请选择公司成立日期！')],
          licence_start: [this.MixinRequired('请选择营业执照有效期开始时间！')],
          licence_end: [this.MixinRequired('请选择营业执照有效期结束时间！')],
          legal_img: [this.MixinRequired('请上传法人身份证！')],
          licence_img: [this.MixinRequired('请上传营业执照！')],
          bank_account_name: [this.MixinRequired('银行开户名不能为空！')],
          bank_number: [this.MixinRequired('公司银行账号不能为空！')],
          bank_name: [this.MixinRequired('开户银行支行名称不能为空！')],
          bank_region: [this.MixinRequired('请选择开户行所在地！')],
          bank_img: [this.MixinRequired('请上传开户行许可证！')],
          taxes_distinguish_num: [this.MixinRequired('纳税人识别号不能为空！')],
          taxes_certificate_img: [this.MixinRequired('请选择税务登记证！')],
          shop_name: [this.MixinRequired('店铺名称不能为空！')],
          shop_region: [this.MixinRequired('请选择店铺所在地！')],
          shop_commission: [this.MixinRequired('店铺佣金比例不能为空！')],
          goods_management_category: [this.MixinRequired('请选择店铺经营类目！')]
        },
        isIndeterminateCat: false,
        checkAllCat: false,
        categorys: [],
        defaultRegionLicense: null,
        defaultRegionBank: null,
        defaultRegionShop: null
      }
    },
    mounted() {
      this.initMemberInfo()
    },
    beforeRouteUpdate(to, from, next) {
      this.shop_id = to.params.id
      next()
    },
    activated() {
      this.shop_id = this.$route.params.id
    },
    watch: {
      shop_id: function() {
        this.initMemberInfo()
        this.shopForm = {}
      }
    },
    methods: {
      /** 经营类目全选 */
      handleCheckAllCatChange(val) {
        this.shopForm.goods_management_category = val ? this.categorys.map(item => item.id) : []
        this.isIndeterminateCat = false
      },

      /** 经营类目选择 */
      handleCheckedCatsChange(value) {
        let checkedCount = value.length
        this.checkAllCat = checkedCount === this.categorys.length
        this.isIndeterminateCat = checkedCount > 0 && checkedCount < this.categorys.length
      },
      /** 保存修改 */
      handleSaveEdit() {
        this.$refs['shopForm'].validate((valid, error) => {
          if (valid) {
            const params = this.MixinClone(this.shopForm)
            if (this.isAudit) {
              params.pass = 1
            }
            params.licence_start /= 1000
            params.licence_end /= 1000
            params.establish_date /= 1000
            API_Shop.editAuthShop(this.shop_id, params).then(response => {
              if (this.isAudit) {
                this.$message.success('审核通过！')
                this.isAudit = false
              } else {
                this.$message.success('修改成功！')
              }
              const { callback } = this.$route.params
              typeof callback === 'function' && callback()
              this.$store.dispatch('delCurrentViews', {
                view: this.$route,
                $router: this.$router
              })
            })
          } else {
            this.$message.error('表单填写有误，请核对！')
            // 找到出错的第一个tab
            // 再将tab切换过去
            const firstError = Object.keys(error)[0]
            let tabChildren = this.$refs['tabs'].$children
            for (let i = 0; i < tabChildren.length; i++) {
              const item = tabChildren[i]
              let finded = false
              for (let j = 0; j < item.$children.length; j++) {
                if (item.$children[j].prop === firstError) {
                  this.tableName = item.name
                  finded = true
                  break
                }
              }
              if (finded) break
            }
            return false
          }
        })
      },
      /** 拒绝通过 */
      handleRefusePass() {
        this.$confirm('确定该店铺拒绝通过吗？', '提示', { type: 'warning' }).then(() => {
          const params = this.MixinClone(this.shopForm)
          params.pass = 0
          API_Shop.editAuthShop(this.shop_id, params).then(response => {
            this.$message.success('已拒绝该店铺！')
            const { callback } = this.$route.params
            typeof callback === 'function' && callback()
            this.$store.dispatch('delCurrentViews', {
              view: this.$route,
              $router: this.$router
            })
          })
        }).catch(() => {})
      },
      /** 初始化会员信息 */
      initMemberInfo() {
        Promise.all([
          API_Shop.getShopDetail(this.shop_id),
          API_Category.getCategoryChildren()
        ]).then(responses => {
          const shopInfo = responses[0]
          const categorys = responses[1]
          // 设置店铺信息
          this.shopForm = this.MixinClone(shopInfo)
          this.shopForm.licence_start *= 1000
          this.shopForm.licence_end *= 1000
          this.shopForm.establish_date *= 1000
          const {
            license_province_id, license_city_id, license_county_id, license_town_id,
            bank_province_id, bank_city_id, bank_county_id, bank_town_id,
            shop_province_id, shop_city_id, shop_county_id, shop_town_id
          } = shopInfo
          this.defaultRegionLicense = [license_province_id, license_city_id, license_county_id, license_town_id]
          this.defaultRegionBank = [bank_province_id, bank_city_id, bank_county_id, bank_town_id]
          this.defaultRegionShop = [shop_province_id, shop_city_id, shop_county_id, shop_town_id]
          this.shopForm.goods_management_category = []

          if (shopInfo.goods_management_category) {
            const _categorys = shopInfo.goods_management_category.split(',')
            this.shopForm.goods_management_category = _categorys
            if (_categorys.length !== 0) {
              if (_categorys.length < categorys.length) {
                this.isIndeterminateCat = true
              } else if (_categorys.length === categorys.length) {
                this.checkAllCat = true
              }
            }
          }
          // 设置经营类目
          this.categorys = categorys.map(item => ({
            id: String(item.category_id),
            name: item.name
          }))
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /deep/ .el-form-item--small .el-form-item__content {
    width: 180px;
  }

  /deep/ .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 100%;
    height: 100%;
    &:hover {
      border-color: #409eff
    }
  }

  /deep/ .avatar-uploader {
    width: 180px;
    height: 180px;
    img { width: 100%; height: 100% }
  }
  /deep/ .avatar-uploader .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }

  /deep/ .el-tabs__content {
    overflow: inherit;
  }

  .save-btn-box {
    text-align: center;
    background-color: #fff;
    padding: 10px 0;
    border: 1px solid #dcdfe6;
    border-top: 0;
    box-shadow: 0 2px 4px 0 rgba(0,0,0,.12), 0 0 6px 0 rgba(0,0,0,.04);
  }
  /deep/ .form-item-cat .el-form-item__content { width: auto }
  .see-original {
    font-size: 12px;
    color: #006db7;
    &:hover { color: #F00 }
  }
  .license-date {
    width: 180px;
  }
</style>
