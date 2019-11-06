<template>
  <div v-loading="loading" class="refund-detail-container">
    <el-card v-if="refund">
      <div slot="header" class="clearfix">
        <span>{{ refund.refuse_type_text }}单</span>
      </div>
      <el-row :gutter="0">
        <el-col :span="4">{{ refund.refuse_type_text }}单号</el-col>
        <el-col :span="8">{{ refund.sn }}</el-col>
        <el-col :span="4">申请时间</el-col>
        <el-col :span="8">{{ refund.create_time | unixToDate }}</el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="4">{{ refund.refuse_type_text }}原因</el-col>
        <el-col :span="8">{{ refund.refund_reason }}</el-col>
        <el-col :span="4">申请退款金额</el-col>
        <el-col :span="8">{{ refund.refund_price | unitPrice('￥') }}</el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="4">退款方式</el-col>
        <el-col :span="8">{{ refund.refund_way === 'ORIGINAL' ? '原路退回' : (refund.account_type_text || '无') }}</el-col>
        <el-col :span="4">售后状态</el-col>
        <el-col :span="8">{{ refund.refund_status_text }}</el-col>
      </el-row>
      <template v-if="refund.account_type === 'BANKTRANSFER'">
        <el-row :gutter="0">
          <el-col :span="4">银行名称</el-col>
          <el-col :span="20">{{ refund.bank_name }}</el-col>
        </el-row>
        <el-row :gutter="0">
          <el-col :span="4">银行开户行</el-col>
          <el-col :span="20">{{ refund.bank_deposit_name }}</el-col>
        </el-row>
        <el-row :gutter="0">
          <el-col :span="4">银行开户名</el-col>
          <el-col :span="20">{{ refund.bank_account_name }}</el-col>
        </el-row>
        <el-row :gutter="0">
          <el-col :span="4">银行账号</el-col>
          <el-col :span="20">{{ refund.bank_account_number }}</el-col>
        </el-row>
      </template>
      <el-row v-else :gutter="0">
        <el-col :span="4">退款账户</el-col>
        <el-col :span="20">{{ refund.return_account }}</el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="4">客户备注</el-col>
        <el-col :span="20">{{ refund.customer_remark || '&nbsp;' }}</el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="4">审核操作</el-col>
        <el-col :span="20">
          <el-input placeholder="请输入内容" size="small" v-model="refundPrice" style="width: 150px;">
            <template slot="prepend">￥</template>
          </el-input>
          <el-button v-if="refund.after_sale_operate_allowable.allow_admin_refund" @click="handleRefundMoney" class="refund-btn">退款</el-button>
          <p v-if="refund.payment_type === 'COD'" class="refund-tip">货到付款订单只能由商家退款</p>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
  import * as API_Refund from '@/api/refund'

  export default {
    name: 'refundDetail',
    data() {
      return {
        /** 列表loading状态 */
        loading: false,
        // 退款（货）单详情
        refund: '',
        /** 订单sn */
        sn: this.$route.params.sn,
        /** 商品 */
        goods: '',
        /** 审核退款金额 */
        refundPrice: 0
      }
    },
    mounted() {
      this.GET_RefundDetail()
    },
    beforeRouteUpdate(to, from, next) {
      this.sn = to.params.sn
      next()
    },
    activated() {
      this.sn = this.$route.params.sn
    },
    watch: {
      sn: 'GET_RefundDetail'
    },
    methods: {
      /** 退款操作 */
      handleRefundMoney() {
        this.$prompt('请输入退款备注！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '请填写退款备注！'
        }).then(({ value }) => {
          API_Refund.refundMoney(this.sn, {
            refund_price: this.refundPrice,
            remark: value
          }).then(response => {
            this.$message.success('操作成功！')
            this.$route.params.callback()
            this.GET_RefundDetail()
          })
        }).catch(() => {})
      },
      /** 获取售后订单详情 */
      GET_RefundDetail() {
        this.loading = true
        API_Refund.getRefundDetail(this.sn).then(response => {
          this.loading = false
          this.refund = response.refund
          this.goods = response.goods
          this.refundPrice = JSON.parse(JSON.stringify(response.refund.refund_price))
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .refund-detail-container {
    padding: 10px;
    background-color: #fff;
  }

  .el-row {
    border-bottom: 1px solid #ebeef5;
    position: relative;
  }

  .el-col {
    padding: 10px;
    position: relative;
    &:not(:first-child)::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      width: 1px;
      height: 100%;
      background-color: #ebeef5;
    }
  }
  /deep/ .el-card__body {
    margin: 10px;
    padding: 0;
    border: 1px solid #ebeef5;
    border-bottom: 0;
  }
  .refund-btn {
    position: relative;
    top: -1px;
    margin-left: 5px;
  }
  .refund-tip {
    color: red;
    font-size: 12px;
    margin: 5px 0 0 0;
    padding: 0;
  }
</style>

