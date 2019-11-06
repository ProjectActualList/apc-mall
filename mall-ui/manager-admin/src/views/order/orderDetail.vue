<template>
  <div v-loading="loading" class="order-detail-container">
    <el-row v-if="orderDetail && orderDetail.order_operate_allowable_vo" :gutter="0">
      <el-col :span="24" style="padding: 10px 20px">
        <el-button
          type="primary"
          size="mini"
          :disabled="!orderDetail.order_operate_allowable_vo.allow_pay"
          @click="confirmPay"
        >确认收款</el-button>
        <el-button
          type="danger"
          size="mini"
          :disabled="!orderDetail.order_operate_allowable_vo.allow_cancel"
          @click="cancelOrder"
        >取消订单</el-button>
      </el-col>
    </el-row>
    <el-row v-for="(row, index) in orderInfo" :key="index" :gutter="0">
      <el-col v-for="col in row" :key="col.key" :span="12">
        <div class="d-header">{{ col.title }}</div>
        <div class="d-content">
          <div v-for="item in col.items" :key="item.key" class="item">
            <span class="item-label" v-html="item.label"></span>
            <span class="item-value">{{ item.value }}</span>
          </div>
        </div>
      </el-col>
    </el-row>
    <!--产品列表-->
    <el-row v-if="productList" :gutter="0">
      <el-col :span="24">
        <div class="d-header">商品列表</div>
        <el-table :data="productList" :header-cell-style="{textAlign: 'center'}">
          <el-table-column label="商品图片" width="180">
            <template slot-scope="scope">
              <img :src="scope.row.goods_image" class="goods-image"/>
            </template>
          </el-table-column>
          <el-table-column label="商品名称" align="center">
            <template slot-scope="scope">
              <a :href="MixinBuyerDomain + '/goods/' + scope.row.goods_id" class="goods-name" target="_blank">{{ scope.row.name }}</a>
              <p class="sku-spec">{{ scope.row | formatterSkuSpec }}</p>
              <p class="sku-act-tags" v-if="scope.row.promotion_tags && scope.row.promotion_tags.length">
                <span class="sku-act-tag" v-for="(tag, index) in scope.row.promotion_tags" :key="index">{{ tag }}</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column label="商品价格" width="150">
            <template slot-scope="scope">￥{{ scope.row.purchase_price | unitPrice }}</template>
          </el-table-column>
          <el-table-column prop="num" label="购买数量" width="120"/>
          <el-table-column label="小计" width="120">
            <template slot-scope="scope">￥{{ scope.row.subtotal | unitPrice }}</template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <!--赠品列表-->
    <el-row v-if="orderDetail && orderDetail.gift_list && orderDetail.gift_list.length" :gutter="0">
      <el-col :span="24">
        <div class="d-header">赠品列表</div>
        <el-table :data="orderDetail.gift_list" :header-cell-style="{textAlign: 'center'}">
          <el-table-column label="赠品图片" width="180">
            <template slot-scope="scope">
              <img :src="scope.row.gift_img" class="goods-image"/>
            </template>
          </el-table-column>
          <el-table-column label="赠品名称" align="left">
            <template slot-scope="scope">
              <a :href="scope.row.gift_img" class="goods-name" target="_blank">{{ scope.row.gift_name }}</a>
            </template>
          </el-table-column>
          <el-table-column label="赠品价格" width="150">
            <template slot-scope="scope">￥{{ scope.row.gift_price | unitPrice }}</template>
          </el-table-column>
          <el-table-column label="" width="120"/>
          <el-table-column label="" width="120"/>
        </el-table>
      </el-col>
    </el-row>
    <!--订单日志-->
    <el-row v-loading="loading_log" :gutter="0">
      <el-col :span="24">
        <div class="d-header">订单日志</div>
        <el-table :data="orderLog" :header-cell-style="{textAlign: 'center'}">
          <el-table-column prop="log_id" label="操作ID" width="100"/>
          <el-table-column prop="op_name" label="操作人员" width="200"/>
          <el-table-column prop="op_time" :formatter="MixinUnixToDate" label="操作时间" width="250"/>
          <el-table-column prop="message" label="操作详情" width="400"/>
          <el-table-column/>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import * as API_order from '@/api/order'
  import { Foundation } from '~/ui-utils'

  export default {
    name: 'orderDetail',
    data() {
      return {
        /** 列表loading状态 */
        loading: false,
        /** 订单日志loading状态 */
        loading_log: false,
        /** 订单详情数据 */
        orderDetail: null,
        /** 订单日志 */
        orderLog: [],
        /** 订单sn */
        sn: this.$route.params.sn,
        /** 基本信息、发票信息、买家信息、商家信息 */
        orderInfo: [],
        /** 产品列表 */
        productList: null
      }
    },
    filters: {
      paymentTypeFilter(val) {
        return val === 'online' ? '在线支付' : '货到付款'
      },
      unixToDate(val) {
        return Foundation.unixToDate(val)
      }
    },
    beforeRouteUpdate(to, from, next) {
      this.sn = to.params.sn
      next()
    },
    activated() {
      this.sn = this.$route.params.sn
      this.GET_OrderDetail()
    },
    mounted() {
      this.GET_OrderDetail()
    },
    methods: {
      GET_OrderDetail() {
        this.loading = true
        this.GET_OrderLog()
        API_order.getOrderDetail(this.sn).then(response => {
          this.loading = false
          this.orderDetail = response
          this.productList = response.order_sku_list
          this.countShowData()
        }).catch(() => { this.loading = false })
      },

      /** 获取订单日志 */
      GET_OrderLog() {
        this.loading_log = true
        API_order.getOrderLog(this.sn).then(response => {
          this.loading_log = false
          this.orderLog = response
        }).catch(() => { this.loading_log = false })
      },

      /** 确认收款 */
      confirmPay() {
        this.$confirm('确定要确认收款吗？', '提示', { type: 'warning' }).then(() => {
          API_order.confirmPay(this.sn, this.orderDetail.order_price).then(response => {
            this.$message.success('订单确认收款成功！')
            this.$route.params.callback()
            this.GET_OrderDetail()
          })
        }).catch(() => {})
      },

      /** 取消订单 */
      cancelOrder() {
        this.$confirm('确定要取消这个订单吗？', '提示', { type: 'warning' }).then(() => {
          API_order.cancleOrder(this.sn).then(() => {
            this.$message.success('订单取消成功！')
            this.$route.params.callback()
            this.GET_OrderDetail()
          })
        }).catch(() => {})
      },

      /** 组合基本信息、发票信息、买家信息、商家信息 */
      countShowData() {
        const o = this.orderDetail
        const f = Foundation
        const promotions = []
        if (o.coupon_price) {
          promotions.push({ label: '优惠券抵扣', value: '-￥' + f.formatPrice(o.coupon_price) })
        }
        if (o.cash_back) {
          promotions.push({ label: '返现金额', value: '-￥' + f.formatPrice(o.cash_back) })
        }
        if (o.use_point) {
          promotions.push({ label: '积分抵扣', value: '-' + o.use_point + '积分' })
        }
        promotions.push({ label: '运&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;费', value: '￥' + f.formatPrice(o.shipping_price) })
        if (o.gift_point) {
          promotions.push({ label: '赠送积分', value: o.gift_point })
        }
        this.orderInfo = [
          [
            {
              title: '基本信息',
              key: 'base',
              items: [
                { label: '订单编号', value: o.sn },
                { label: '订单金额', value: '￥' + f.formatPrice(o.need_pay_money) },
                { label: '商品金额', value: '￥' + f.formatPrice(o.goods_price) },
                ...promotions,
                { label: '支付方式', value: (o.payment_type === 'ONLINE' ? '在线支付' : '货到付款') + (o.payment_method_name ? ('-' + o.payment_method_name) : '') },
                { label: '订单状态', value: o.order_status_text + (o.cancel_reason ? '（' + o.cancel_reason + '）' : '') },
                { label: '下单时间', value: f.unixToDate(o.create_time) }
              ]
            },
            {
              title: '发票信息',
              key: 'receipt',
              items: [
                { label: '发票类型', value: o.receipt_history ? o.receipt_history.receipt_type || '无' : '不需要发票' },
                { label: '发票抬头', value: o.receipt_history ? o.receipt_history.receipt_title || '无' : '无' },
                { label: '发票内容', value: o.receipt_history ? o.receipt_history.receipt_content || '无' : '无' },
                { label: '发票税号', value: o.receipt_history ? o.receipt_history.tax_no || '无' : '无' },
                { label: '发票金额', value: o.receipt_history ? f.formatPrice(o.receipt_history.receipt_amount) || '无' : '无' }
              ]
            }
          ],
          [
            {
              title: '买家信息',
              key: 'buyer',
              items: [
                { label: '收&ensp;货&ensp;人', value: o.ship_name },
                { label: '收货地址', value: o.ship_province + o.ship_city + o.ship_county + o.ship_town + ' ' + o.ship_addr },
                { label: '联系方式', value: o.ship_mobile },
                { label: '买家留言', value: o.remark || '无' }
              ]
            },
            {
              title: '商家信息',
              key: 'seller',
              items: [
                { label: '卖家账号', value: o.seller_name },
                { label: '发货时间', value: o.ship_time ? f.unixToDate(o.ship_time) : '未发货' },
                { label: '物流公司', value: o.logi_name || '未发货' },
                { label: '快递单号', value: o.ship_no || '未发货' }
              ]
            }
          ]
        ]
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .order-detail-container {
    background-color: #fff;
  }

  .d-header {
    padding: 10px 0 10px 20px;
    font-size: 14px;
    color: #333;
    background-color: #f5f7fa;
    border-bottom: 1px solid #e1e8ed;

    &h2 {
      font-size: 14px;
      font-weight: 400;
    }
  }

  .item {
    width: 100%;
    position: relative;
    display: inline-block;
    vertical-align: top;
    box-sizing: border-box;
    padding: 16px 0 16px 20px;
    line-height: 24px;
    white-space: nowrap;

    & .item-label {
      float: left;
      margin-right: 20px;
      font-size: 14px;
      color: #999;
    }
    & .item-value {
      overflow: hidden;
      white-space: normal;
      word-break: break-all;
      font-size: 14px;
      color: #666;
    }
  }

  /deep/ .el-table td:not(.is-left) {
    text-align: center;
  }

  .goods-image {
    width: 50px;
    height: 50px;
  }
  .goods-name {
    color: #4183c4;
    &:hover { color: #f42424 }
  }
  .sku-spec {
    color: #ff9800;
    margin: 0;
  }
  .sku-act-tags {
    padding: 0;
    margin: 0;
  }
  .sku-act-tag {
    display: inline-block;
    padding: 0 5px;
    line-height: 15px;
    margin-right: 5px;
    border: 1px solid #f42424;
    color: #f42424;
  }
</style>

