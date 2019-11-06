<template>
  <div id="shop-info" class="item-container">
    <div class="content">
      <el-form
        :model="shopInfoForm"
        :rules="shopInfoRules"
        ref="shopInfoForm"
        label-width="180px"
        size="small"
      >
        <h5 class="item-title">店铺信息</h5>
        <el-form-item label="店铺名称：" prop="shop_name">
          <el-input v-model.trim="shopInfoForm.shop_name" :maxlength="15" clearable></el-input>
        </el-form-item>
        <el-form-item label="店铺地址：" prop="shop_region">
          <en-region-picker :api="MixinRegionApi" :default="defaultRegions" @changed="(object) => { shopInfoForm.shop_region = object.last_id }"/>
        </el-form-item>
        <el-form-item label="经营类目：" prop="goods_management_category">
          <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChanged">全选</el-checkbox>
          <el-checkbox-group v-model="shopInfoForm.goods_management_category" @change="handleCheckedCategorysChange">
            <el-checkbox v-for="cate in categorys" :label="cate.id" :key="cate.id">{{cate.name}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
    </div>
    <div class="next-btns">
      <el-button size="small" @click="$router.back()">上一步</el-button>
      <el-button @click="handleNextStep" size="small">提交申请</el-button>
    </div>
  </div>
</template>

<script>
  import * as API_Shop from '@/api/shop'
  import * as API_Goods from '@/api/goods'
  import EnRegionPicker from "@/components/RegionPicker"
  import mixin from './checkStatusMixin'
  export default {
    name: "shop-info",
    middleware: 'auth-seller',
    mixins: [mixin],
    components: { EnRegionPicker },
    data() {
      const req_rule = (message, trigger) => ({ required: true, message, trigger: trigger || 'blur' })
      const len_rule = (min, max) => ({ min, max, message: `'长度在 ${min} 到 ${max} 个字符`, trigger: 'change' })
      return {
        defaultRegions: null,
        /** 分类 */
        categorys: [],
        isIndeterminate: false,
        checkAll: false,
        /** 基础信息 表单 */
        shopInfoForm: {
          shop_name: '',
          goods_management_category: [],
          shop_region: ''
        },
        /** 基础信息 表单规则 */
        shopInfoRules: {
          shop_name: [ req_rule('请输入店铺名称！'), len_rule(1, 10) ],
          shop_region: [ req_rule('请选择店铺地址！') ],
          goods_management_category: [
            { type: 'array', required: true, message: '请至少选择一个经营类目', trigger: 'change' }
          ]
        }
      }
    },
    mounted() {
      Promise.all([
        API_Shop.getApplyShopInfo(),
        API_Goods.getCategory()
      ]).then(responses => {
        const shopInfo = responses[0]
        const categorys = responses[1]
        if (!shopInfo || shopInfo.bank_account_name === null) {
          this.$router.replace({ name: 'shop-apply' })
          return false
        }
        // 设置店铺信息
        Object.keys(this.shopInfoForm).forEach(key => this.shopInfoForm[key] = shopInfo[key])
        const { shop_province_id, shop_city_id, shop_county_id, shop_town_id } = shopInfo
        this.defaultRegions = [shop_province_id, shop_city_id, shop_county_id, shop_town_id]
        this.shopInfoForm.goods_management_category = []
        if (shopInfo.goods_management_category) {
          const _categorys = shopInfo.goods_management_category.split(',')
          this.shopInfoForm.goods_management_category = _categorys
          if (_categorys.length !== 0) {
            if (_categorys.length < categorys.length) {
              this.isIndeterminate = true
            } else if (_categorys.length === categorys.length) {
              this.checkAll = true
            }
          }
        }
        // 设置分类信息
        this.categorys = categorys.map(item => ({
          id: String(item.category_id),
          name: item.name
        }))
      })
    },
    methods: {
      /** 下一步 */
      handleNextStep() {
        this.$refs['shopInfoForm'].validate((valid) => {
          if (valid) {
            const params = JSON.parse(JSON.stringify(this.shopInfoForm))
            params.goods_management_category = params.goods_management_category.join(',')
            API_Shop.applyShopStep(4, params).then(response => {
              this.$router.push({ name: 'shop-apply-success' })
            })
          } else {
            this.$message.error('表单填写有误，请核对！')
            return false
          }
        })
      },
      /** 经营类目全选框发生改变 */
      handleCheckAllChanged(checked) {
        this.shopInfoForm.goods_management_category = checked ? this.categorys.map(item => item.id) : []
        this.isIndeterminate = false
      },
      /** 选中的经营类目发生改变 */
      handleCheckedCategorysChange(vaule) {
        const checkedCount = vaule.length
        const categorysCount = this.categorys.length
        this.checkAll = checkedCount === categorysCount
        this.isIndeterminate = checkedCount > 0 && checkedCount < categorysCount
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
    /deep/ .el-checkbox-group .el-checkbox:nth-child(2n + 1) {
      margin-left: 0;
    }
  }
</style>
