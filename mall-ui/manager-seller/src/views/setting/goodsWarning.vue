<template>
  <div class="bg-settings">
    <el-form :model="stockWarningForm" ref="stockWarningForm" label-width="100px" class="demo-ruleForm">
      <el-form-item
        label="库存预警数"
        prop="goods_warning_count"
        :rules="[
          { required: true, message: '数量不能为空', trigger: 'blur' },
          { type: 'number', message: '库存预警数必须为数字值', trigger: 'blur' }
        ]">
        <el-input-number v-model="stockWarningForm.goods_warning_count" controls-position="right" :min="0" :max="99999"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSaveStockWarning('stockWarningForm')">保存修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import * as API_shop from '@/api/shop'
  export default {
    name: 'goodsWarning',
    data() {
      return {
        /** 库存预警数*/
        stockWarningForm: {
          goods_warning_count: 0
        }
      }
    },
    mounted() {
      this.GET_stockWarningNum()
    },
    methods: {
      /** 获取当前库存预警数*/
      GET_stockWarningNum() {
        API_shop.getShopData({}).then(response => {
          this.stockWarningForm.goods_warning_count = response.goods_warning_count
        })
      },

      /** 保存修改 */
      handleSaveStockWarning(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const _params = {
              warning_count: this.stockWarningForm.goods_warning_count
            }
            API_shop.saveStockWarningNum(_params).then(() => {
              this.$message.success('修改成功')
              this.GET_stockWarningNum()
            })
          }
        })
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
</style>
