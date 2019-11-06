<template>
  <div class="container">
    <el-form :model="pushForm" :rules="pushRules" ref="pushForm" label-width="100px" style="width: 500px">
      <el-form-item label="推送标题" prop="title">
        <el-input v-model="pushForm.title"></el-input>
      </el-form-item>
      <el-form-item label="推送的商品" prop="goods_id">
        <div tabindex="0" class="el-upload el-upload--text" @click="handleSelectGoods">
          <img v-if="selectedGoods && selectedGoods.thumbnail" :src="selectedGoods.thumbnail" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </div>
      </el-form-item>
      <el-form-item label="">
        <el-button type="primary" @click="handlePushGoods">确认推送</el-button>
      </el-form-item>
    </el-form>
    <en-goods-picker
      :limit="1"
      :show="dialogGoodsPickerVisible"
      @close="dialogGoodsPickerVisible = false"
      @confirm="handleGoodsPickerConfirm"
    />
  </div>
</template>

<script>
  import * as API_Push from '@/api/push'

  export default {
    name: 'goodsPush',
    data() {
      return {
        pushForm: {
          title: '',
          goods_id: ''
        },
        pushRules: {
          title: [this.MixinRequired('请输入推送标题！')],
          goods_id: [this.MixinRequired('请选择一个要推送的商品！')]
        },
        // 商品选择器显示
        dialogGoodsPickerVisible: false,
        // 已选商品
        selectedGoods: ''
      }
    },
    methods: {
      /** 选择商品 */
      handleSelectGoods() {
        this.dialogGoodsPickerVisible = true
      },
      /** 商品选择器确认 */
      handleGoodsPickerConfirm(goodsList) {
        const goods = goodsList[0] || {}
        this.selectedGoods = goods
        this.pushForm.goods_id = goods.goods_id
      },
      /** 推送商品 */
      handlePushGoods() {
        this.$refs['pushForm'].validate((valid) => {
          if (valid) {
            this.$confirm('确定要推送这个商品吗？', '提示', { type: 'warning' }).then(() => {
              const { title, goods_id } = this.pushForm
              API_Push.pushGoods(title, goods_id).then(() => {
                this.$message.success('推送成功！')
              })
            }).catch(() => {})
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
  .container {
    padding: 10px;
    background-color: #fff;
  }
  .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    &:hover {
      border-color: #409EFF;
    }
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 150px;
    height: 150px;
    line-height: 150px;
    text-align: center;
  }
  .avatar {
    width: 150px;
    height: 150px;
    display: block;
  }
</style>
