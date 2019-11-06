<template>
  <van-popup id="receipt" v-model="show" position="bottom" style="height:100%;background-color:#f7f7f7">
    <van-nav-bar title="发票信息" fixed @click-right="$emit('close')">
      <i class="iconfont ea-icon-close" slot="right"/>
    </van-nav-bar>
    <div class="receipt-container">
      <!--发票类型 start-->
      <div class="item receipt-type">
        <div class="title-receipt">
          <h2>发票类型</h2>
        </div>
        <div class="type-receipt-type">
          <a
            href="javascript:;"
            class="eui-checkbox-btn"
            :class="[!need_receipt && 'checked']"
            @click="need_receipt = false"
          >不开具发票</a>
          <a
            href="javascript:;"
            class="eui-checkbox-btn"
            :class="[need_receipt && 'checked']"
            @click="need_receipt = true"
          >开具发票</a>
        </div>
      </div>
      <!--发票类型 end-->
      <div v-show="need_receipt">
        <!--发票抬头 start-->
        <div class="item receipt-head">
          <div class="title-receipt">
            <h2>发票抬头</h2>
            <span>可输入个人/单位名称</span>
          </div>
          <div class="head-receipt">
            <div class="receipt-word">
              <input
                v-model="receiptForm.receipt_title"
                placeholder="请输入个人或单位名称"
                @focus="show_receipts = true"
                @blur="show_receipts = false"
              >
              <div v-if="receipts.length && show_receipts" class="receipts">
                <div
                  class="rep-item"
                  v-for="(rep, index) in receipts"
                  :key="index"
                  @click="handleSelectReceipt(rep)"
                >
                  {{ rep.receipt_title }}
                </div>
              </div>
            </div>
          </div>
        </div>
        <!--发票抬头 end-->
        <!--发票税号 start-->
        <div class="item receipt-duty">
          <div class="title-receipt">
            <h2>税号</h2>
            <span>请输入纳税人识别号</span>
          </div>
          <div class="head-receipt">
            <div class="receipt-word">
              <input v-model="receiptForm.tax_no" placeholder="抬头为个人无须税号">
            </div>
          </div>
        </div>
        <!--发票税号 end-->
        <!--发票内容 start-->
        <div class="item receipt-content">
          <div class="title-receipt">
            <h2>发票内容</h2>
          </div>
          <div class="content-receipt">
            <a href="javascript:;" class="eui-checkbox-btn checked">明细</a>
          </div>
        </div>
        <!--发票内容 end-->
      </div>
    </div>
    <div class="big-btn">
      <van-button type="danger" @click="handleConfirmReceipt">保存</van-button>
    </div>
  </van-popup>
</template>

<script>
  /**
   * 结算页
   * 发票组件
   */
  import * as API_Members from '@/api/members'
  import * as API_Trade from '@/api/trade'
  import { RegExp } from '@/ui-utils'
  export default {
    name: 'checkout-receipt',
    props: ['show', 'receipt'],
    data() {
      return {
        // 需要发票
        need_receipt: false,
        // 发票表单
        receiptForm: {
          receipt_content: "",
          receipt_title: "",
          receipt_type: "",
          tax_no: ""
        },
        // 会员发票列表
        receipts: [],
        // 发票内容统一设置
        content: '明细',
        // 显示已有发票
        show_receipts: false
      }
    },
    mounted() {
      // 获取会员发票列表
      this.GET_ReceiptList()
    },
    watch: {
      show: function() {
        const { receipt_title } = this.receipt || {}
        this.need_receipt = !!receipt_title
        this.receiptForm = { ...this.receipt }
      }
    },
    methods: {
      /** 选择发票 */
      handleSelectReceipt(receipt) {
        this.need_receipt = true
        this.receiptForm = JSON.parse(JSON.stringify(receipt))
      },
      /** 确认发票 */
      handleConfirmReceipt() {
        if (!this.need_receipt) {
          API_Trade.cancelReceipt().then(() => {
            this.$emit('changed', {})
            this.$emit('close')
          })
          return false
        }
        const receipt = { receipt_content: '明细' }
        const { receiptForm } = this
        if (receiptForm.receipt_title === '个人') {
          receipt.type = 0
          receipt.receipt_title = '个人'
        } else {
          receipt.type = 1
          receipt.receipt_title = receiptForm.receipt_title
          receipt.tax_no = receiptForm.tax_no
        }
        if (receipt.receipt_title === '个人' && receipt.tax_no && !RegExp.TINumber.test(receipt.tax_no)) {
          this.$message.error('纳税人识别号不正确')
          return false
        }
        API_Trade.setRecepit(receipt).then(() => {
          this.$message.success('设置成功！')
          this.$emit('changed', receipt)
          this.$emit('close')
        })
      },
      /** 获取发票列表 */
      GET_ReceiptList() {
        API_Members.getReceipts().then(response => {
          this.receipts = response.VATORDINARY.reverse()
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .receipt-container {
    padding-top: 46px;
    .item {
      position: relative;
      margin-bottom: 10px;
      background-color: #fff;
      padding-top: 12px;
      padding-left: 10px;
    }
    .title-receipt {
      padding-bottom: 10px;
      overflow: hidden;
      h2 {
        float: left;
        font-size: 14px;
        color: #232326;
      }
    }
    .content-receipt {
      padding: 0 10px 10px 0;
    }
    .type-receipt-type {
      padding-bottom: 10px;
    }
    .head-receipt {
      padding: 0 10px 10px 0;
    }
    .receipt-word {
      width: 100%;
      position: relative;
      input {
        width: 90%;
        height: 26px;
        background: #f0f2f5;
        border: none;
        outline: none;
        padding: 2px 5%;
        margin: 8px 0 0 0;
        -webkit-appearance: none;
      }
    }
    .receipts {
      position: absolute;
      top: 40px;
      width: 100%;
      background-color: #fff;
      box-shadow: 0 5px 16px 0 #ccc;
      z-index: 10;
      .rep-item {
        padding: 5px;
      }
    }
  }
</style>
