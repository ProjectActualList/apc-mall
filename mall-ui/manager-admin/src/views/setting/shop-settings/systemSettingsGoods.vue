<template>
  <el-form :model="goodsForm" :rules="goodsRules" ref="goodsForm" label-width="130px" style="width: 350px">
    <el-form-item label="上架商品是否审核">
      <el-radio-group v-model="goodsForm.marcket_auth">
        <el-radio :label="1">是</el-radio>
        <el-radio :label="0">否</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="修改商品是否审核">
      <el-radio-group v-model="goodsForm.update_auth">
        <el-radio :label="1">是</el-radio>
        <el-radio :label="0">否</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="缩略图尺寸">
      <el-input placeholder="100" size="small" v-model="goodsForm.thumbnail_width">
        <template slot="prepend">宽</template>
        <template slot="append">px</template>
      </el-input>
      <el-input placeholder="100" size="small" v-model="goodsForm.thumbnail_height">
        <template slot="prepend">高</template>
        <template slot="append">px</template>
      </el-input>
    </el-form-item>
    <el-form-item label="小图尺寸" prop="cancel_order_day">
      <el-input placeholder="400" size="small" v-model="goodsForm.small_width">
        <template slot="prepend">宽</template>
        <template slot="append">px</template>
      </el-input>
      <el-input placeholder="400" size="small" v-model="goodsForm.small_height">
        <template slot="prepend">高</template>
        <template slot="append">px</template>
      </el-input>
    </el-form-item>
    <el-form-item label="大图尺寸" prop="cancel_order_day">
      <el-input placeholder="800" size="small" v-model="goodsForm.big_width">
        <template slot="prepend">宽</template>
        <template slot="append">px</template>
      </el-input>
      <el-input placeholder="800" size="small" v-model="goodsForm.big_height">
        <template slot="prepend">高</template>
        <template slot="append">px</template>
      </el-input>
    </el-form-item>
    <el-form-item label="">
      <el-button type="primary" @click="submitEditGoodsSetting">保存设置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  import * as API_Goods from '@/api/goods'

  export default {
    name: 'systemSettingsGoods',
    data() {
      return {
        // 商品设置
        goodsForm: {
          marcket_auth: 1,
          update_auth: 1,
          thumbnail_width: 100,
          thumbnail_height: 100,
          small_width: 400,
          small_height: 400,
          big_width: 800,
          big_height: 800
        },
        goodsRules: {}
      }
    },
    mounted() {
      API_Goods.getGoodsSettings().then(response => {
        Object.keys(this.goodsForm).forEach(key => {
          this.goodsForm[key] = response[key]
        })
      })
    },
    methods: {
      submitEditGoodsSetting() {
        API_Goods.editGoodsSettings(this.goodsForm).then(() => {
          this.$message.success('修改成功！')
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .image-pane .el-input-group {
    width: 200px;
  }
  .point-pane .el-input-group {
    width: 200px;
  }
</style>
