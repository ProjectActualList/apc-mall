<template>
  <div id="withdrawals-apply">
    <nav-bar title="提现申请"/>
    <div class="withdrawals-container">
      <van-cell-group>
        <van-field
          label-align="right"
          v-model="applyWithdrawalsForm.can_rebate"
          class="readonly-input"
          :border="false"
          label="可提现金额:"
          readonly/>
        <van-field
          label-align="right"
          v-model="applyWithdrawalsForm.apply_money"
          :border="false"
          label="提现金额:" />
        <van-field
          v-model="applyWithdrawalsForm.remark"
          label="备注信息:"
          :border="false"
          label-align="right"
          type="textarea"
          rows="3"
          autosize/>
        <div style="padding:15px 0 15px 114px;">
          <van-button type="danger" size="small" @click="handleApplyWithdrawals">申请提现</van-button>
        </div>
      </van-cell-group>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import { RegExp } from '@/ui-utils'
  import * as API_distribution from '@/api/distribution'
  export default {
    name: 'withdrawals-apply',
    data() {
      return {
        /** 申请表单 */
        applyWithdrawalsForm: {
          /** 可提现金额 */
          can_rebate: 0,

          /** 提现金额 */
          apply_money: 0,

          /** 备注 */
          remark: ''
        }
      }
    },
    mounted() {
      this.GET_canRebate()
    },
    methods: {
      /** 获取可提现金额 */
      GET_canRebate() {
        API_distribution.getWithdrawalsCanRebate().then(response => {
          this.applyWithdrawalsForm.can_rebate = response.message
        })
      },

      /** 申请提现 */
      handleApplyWithdrawals() {
        // 申请校验
        if (!this.applyWithdrawalsForm.apply_money) {
          this.$message.error('提现金额不能为空或0')
          return
        } else if (!RegExp.money.test(this.applyWithdrawalsForm.apply_money)) {
          this.$message.error('请输入正整数或者两位小数')
          return
        } else if (parseFloat(this.applyWithdrawalsForm.apply_money) > parseFloat(this.applyWithdrawalsForm.can_rebate)) {
          this.$message.error('已超可提现金额')
          return
        }
        const _params = {
          apply_money: this.applyWithdrawalsForm.apply_money,
          remark: this.applyWithdrawalsForm.remark
        }
        API_distribution.applyWithdrawals(_params).then(() => {
          this.$message.success('已提交申请，请耐心等待。。。')
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .withdrawals-container {
    padding-top: 46px;
  }
  /deep/ .van-field__control {
    margin-left: 10px;
    border: 1px solid #ddd;
    padding: 0 5px;
    line-height: 30px;
  }
  /deep/ .readonly-input {
   .van-field__control {
     padding: 0 5px;
     border: none;
     line-height: 30px;
     margin-left: 10px;
    }
  }
</style>
