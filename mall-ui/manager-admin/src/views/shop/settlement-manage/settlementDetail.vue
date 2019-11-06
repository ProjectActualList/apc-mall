<template>
  <div class="refund-detail-container">
    <el-card v-if="settlement">
      <div slot="header" class="clearfix">
        <span>结算单详细信息</span>
        <el-button type="primary" size="mini" @click="handleExportBill" style="margin-left: 10px">导出结算单</el-button>
        <el-button v-if="settlement.operate_allowable.allow_auth" type="primary" size="mini" @click="handleNextOperate">通过审核</el-button>
        <el-button v-if="settlement.operate_allowable.allow_pay" type="primary" size="mini" @click="handleNextOperate">确认付款</el-button>
      </div>
      <el-row :gutter="0">
        <el-col :span="4">结算单号</el-col>
        <el-col :span="8">{{ settlement.bill_sn || '无' }}</el-col>
        <el-col :span="4">起止日期</el-col>
        <el-col :span="8">{{ settlement.start_time | unixToDate }} - {{ settlement.end_time | unixToDate }}</el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="4">出账日期</el-col>
        <el-col :span="8">{{ settlement.create_time | unixToDate }}</el-col>
        <el-col :span="4">结算状态</el-col>
        <el-col :span="8">{{ settlement.status_text || '无' }}</el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="4">店铺名称</el-col>
        <el-col :span="20">{{ settlement.shop_name || '无' }}</el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="4">银行开户名</el-col>
        <el-col :span="8">{{ settlement.bank_account_name || '无' }}</el-col>
        <el-col :span="4">公司银行账号</el-col>
        <el-col :span="8">{{ settlement.bank_account_number || '无' }}</el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="4">开户银行支行名称</el-col>
        <el-col :span="8">{{ settlement.bank_name || '无' }}</el-col>
        <el-col :span="4">支行联行号</el-col>
        <el-col :span="8">{{ settlement.bank_code || '无' }}</el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="4">银行地址</el-col>
        <el-col :span="20">{{ settlement.bank_address || '无' }}</el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="4">货到付款金额</el-col>
        <el-col :span="8">￥{{ settlement.cod_price | unitPrice }}</el-col>
        <el-col :span="4">货到付款退款金额</el-col>
        <el-col :span="8">￥{{ settlement.cod_refund_price | unitPrice }}</el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="4">平台应付金额</el-col>
        <el-col :span="20">
          <em class="plus">￥{{ settlement.bill_price | unitPrice }}</em>
          <em class="equal"> = </em>
          <em class="plus">￥{{ settlement.price | unitPrice }}</em>(在线支付金额)
          <em class="minus"> - ￥{{ settlement.refund_price | unitPrice }}</em>(在线退款金额)
          <em class="minus"> - ￥{{ settlement.commi_price | unitPrice }}</em>(佣金金额)
          <em class="plus"> + ￥{{ settlement.refund_commi_price | unitPrice }}</em>(退还佣金)
          <em class="minus"> - ￥{{ settlement.distribution_rebate | unitPrice }}</em>(分销佣金)
          <em class="plus"> + ￥{{ settlement.distribution_return_rebate | unitPrice }}</em>(退还分销佣金)
        </el-col>
      </el-row>
    </el-card>
    <el-card v-loading="loading_order" style="margin-top: 10px">
      <div slot="header" class="clearfix">
        <span>订单列表</span>
      </div>
      <en-table-layout :tableData="tableDataOrder.data" :toolbar="false">
        <template slot="table-columns">
          <!--订单编号-->
          <el-table-column prop="order_sn" label="订单编号"/>
          <!--下单时间-->
          <el-table-column prop="add_time" :formatter="MixinUnixToDate" label="下单时间"/>
          <!--订单总额-->
          <el-table-column prop="price" :formatter="MixinFormatPrice" label="订单总额"/>
          <!--支付方式-->
          <el-table-column label="支付方式">
            <template slot-scope="scope">{{ scope.row.payment_type === 'COD' ? '货到付款' : '在线支付' }}</template>
          </el-table-column>
          <!--操作-->
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button
                v-if="scope.row.order_status !== 'COMPLETE'"
                size="mini"
                type="primary"
                @click="() => { $router.push({ path: '/order/detail/' + scope.row.order_sn }) }"
              >查看</el-button>
            </template>
          </el-table-column>
        </template>
        <el-pagination
          v-if="tableDataOrder"
          slot="pagination"
          @size-change="handlePageSizeChangeOrder"
          @current-change="handlePageCurrentChangeOrder"
          :current-page="params_order.page_no"
          :page-sizes="[5, 10, 50, 100]"
          :page-size="params_order.page_size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="tableDataOrder.data_total">
        </el-pagination>
      </en-table-layout>
    </el-card>
    <el-card v-loading="loading_refund" style="margin-top: 10px">
      <div slot="header" class="clearfix">
        <span>退款单</span>
      </div>
      <en-table-layout :tableData="tableDataRefund.data" :toolbar="false">
        <template slot="table-columns">
          <el-table-column prop="refund_sn" label="退款单号"/>
          <el-table-column prop="order_sn" label="退款订单号"/>
          <el-table-column label="支付方式">
            <template slot-scope="scope">{{ scope.row.payment_type === 'COD' ? '货到付款' : '在线支付' }}</template>
          </el-table-column>
          <el-table-column prop="refund_time" :formatter="MixinUnixToDate" label="操作时间"/>
          <el-table-column prop="price" :formatter="MixinFormatPrice" label="退款金额"/>
        </template>
        <el-pagination
          v-if="tableDataRefund"
          slot="pagination"
          @size-change="handlePageSizeChangeRefund"
          @current-change="handlePageCurrentChangeRefund"
          :current-page="params_refund.page_no"
          :page-sizes="[5, 10, 50, 100]"
          :page-size="params_refund.page_size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="tableDataRefund.data_total">
        </el-pagination>
      </en-table-layout>
    </el-card>
  </div>
</template>

<script>
  import * as API_Order from '@/api/order'
  import { Foundation } from '~/ui-utils'

  export default {
    name: 'settlementDetail',
    data() {
      return {
        // 结算单详情
        settlement: '',
        /** 订单sn */
        id: this.$route.params.id,
        /** 商品 */
        goods: '',
        // 结算单里的订单参数
        params_order: {
          page_no: 1,
          page_size: 5
        },
        // 结算单订单 loading
        loading_order: false,
        // 结算单里的订单列表
        tableDataOrder: '',
        // 结算单里的退款单参数
        params_refund: {
          page_no: 1,
          page_size: 5
        },
        // 结算单退款单loading
        loading_refund: false,
        // 结算单里的退款单列表
        tableDataRefund: ''
      }
    },
    mounted() {
      this.GET_SettlementDetail()
    },
    beforeRouteUpdate(to, from, next) {
      this.id = to.params.id
      next()
    },
    activated() {
      this.id = this.$route.params.id
    },
    watch: {
      id: 'GET_SettlementDetail'
    },
    methods: {
      /** 导出结算单 */
      handleExportBill() {
        API_Order.exportBill(this.settlement.bill_id).then(response => {
          const { bill: b } = response
          const f = Foundation
          const jsonArray = []
          jsonArray[0] = {
            sheet_name: '结算单详细',
            sheet_values: [
              {
                A: '结算单号',
                B: '起止日期',
                C: '出账日期',
                D: '结算状态',
                E: '店铺名称',
                F: '银行开户名',
                G: '公司银行账号',
                H: '开户银行支行名称',
                I: '支行联行号',
                J: '银行地址',
                K: '平台应付金额'
              },
              {
                A: b.bill_sn,
                B: f.unixToDate(b.start_time) + ' - ' + f.unixToDate(b.end_time),
                C: f.unixToDate(b.create_time),
                D: b.status_text,
                E: b.shop_name,
                F: b.bank_account_name || '无',
                G: b.bank_account_number || '无',
                H: b.bank_name || '无',
                I: b.bank_code || '无',
                J: b.bank_address || '无',
                K: `￥${f.formatPrice(b.bill_price)} = ￥${f.formatPrice(b.price)}(在线支付金额) - ￥${f.formatPrice(b.refund_price)}(在线退款金额) - ￥${f.formatPrice(b.commi_price)}(佣金金额) + ￥${f.formatPrice(b.refund_commi_price)}(退还佣金)`
              }
            ],
            sheet_options: { header: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'], skipHeader: true }
          }
          jsonArray[1] = {
            sheet_name: '订单列表',
            sheet_values: response.order_list.map(item => ({
              '订单编号': item.order_sn,
              '下单时间': f.unixToDate(item.add_time),
              '订单总额（元）': f.formatPrice(item.price),
              '支付方式': item.payment_type === 'COD' ? '货到付款' : '在线支付'
            }))
          }
          jsonArray[2] = {
            sheet_name: '退款单列表',
            sheet_values: response.refund_list.map(item => ({
              '退款单号': item.refund_sn,
              '退款订单号': item.order_sn,
              '支付方式': item.payment_type === 'COD' ? '货到付款' : '在线支付',
              '操作时间': f.unixToDate(item.refund_time),
              '退款金额（元）': f.formatPrice(item.price)
            }))
          }
          this.MixinExportJosnToExcel(jsonArray, `结算单-${response.bill.shop_name}`)
        })
      },
      /** 订单列表分页大小发生改变 */
      handlePageSizeChangeOrder(page_size) {
        this.params_order.page_size = page_size
        this.GET_SettlementOrderList()
      },
      /** 订单列表分页数发生改变 */
      handlePageCurrentChangeOrder(page_no) {
        this.params_order.page_no = page_no
        this.GET_SettlementOrderList()
      },
      /** 退款单列表分页大小发生改变 */
      handlePageSizeChangeRefund(page_size) {
        this.params_refund.page_size = page_size
        this.GET_SettlementRefundList()
      },
      /** 退款单列表分页数发生改变 */
      handlePageCurrentChangeRefund(page_no) {
        this.params_refund.page_no = page_no
        this.GET_SettlementRefundList()
      },
      /** 对结算单进行下一步操作 【通过审核、确认付款】 */
      handleNextOperate() {
        this.$confirm('确定进行此操作吗？', '提示', { type: 'warning' }).then(() => {
          API_Order.operateSettlement(this.settlement.bill_id).then(() => {
            this.$message.success('操作成功！')
            this.GET_SettlementDetail()
          })
        }).catch(() => {})
      },
      /** 获取售后订单详情 */
      GET_SettlementDetail() {
        API_Order.getSettlementDetail(this.id).then(response => {
          this.settlement = response
          this.GET_SettlementOrderList()
          this.GET_SettlementRefundList()
        })
      },
      /** 获取结算单里的订单 */
      GET_SettlementOrderList() {
        this.loading_order = true
        API_Order.getSettlementOrderList(this.settlement.bill_id, 'PAYMENT', this.params_order).then(response => {
          this.loading_order = false
          this.tableDataOrder = response
        }).catch(() => { this.loading_order = false })
      },
      /** 获取结算单里的退款单 */
      GET_SettlementRefundList() {
        this.loading_refund = true
        API_Order.getSettlementOrderList(this.settlement.bill_id, 'REFUND', this.params_refund).then(response => {
          this.loading_refund = false
          this.tableDataRefund = response
        }).catch(() => { this.loading_refund = false })
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
  .equal, .plus, .minus {
    font-weight: 700;
    font-style: normal;
  }
  // 等于
  .equal {
    color: #409EFF
  }
  // 加
  .plus {
    color: #67C23A
  }
  // 减
  .minus {
    color: #F56C6C
  }
</style>

