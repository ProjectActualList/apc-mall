<template>
  <div class="container">
    <el-row :gutter="0">
      <el-col :span="12">
        <div class="d-header">订单信息</div>
        <div class="d-content">
          <div class="item">
            <span class="item-label">订单编号</span>
            <span class="item-value">{{ receipt.order_sn }}</span>
          </div>
          <div class="item">
            <span class="item-label">会员名称</span>
            <span class="item-value">{{ receipt.member_name }}</span>
          </div>
          <div class="item">
            <span class="item-label">收货地区</span>
            <span class="item-value">{{ `${receipt.ship_province} - ${receipt.ship_city} - ${receipt.ship_county} - ${receipt.ship_town}` }}</span>
          </div>
          <div class="item">
            <span class="item-label">详细地址</span>
            <span class="item-value">{{ receipt.ship_addr }}</span>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="d-header">发票信息</div>
        <div class="d-content">
          <div class="item">
            <span class="item-label">发票类型</span>
            <span class="item-value">{{ receipt.receipt_type }}</span>
          </div>
          <div class="item">
            <span class="item-label">发票抬头</span>
            <span class="item-value">{{ receipt.receipt_title || '无' }}</span>
          </div>
          <div class="item">
            <span class="item-label">发票内容</span>
            <span class="item-value">{{ receipt.receipt_content || '无' }}</span>
          </div>
          <div class="item">
            <span class="item-label">发票税号</span>
            <span class="item-value">{{ receipt.tax_no || '无' }}</span>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row v-if="skuList" :gutter="0">
      <el-col :span="24">
        <div class="d-header">商品列表</div>
        <el-table :data="skuList" :header-cell-style="{textAlign: 'center'}">
          <el-table-column label="商品图片" width="180">
            <template slot-scope="scope">
              <img :src="scope.row.goods_image" class="goods-image"/>
            </template>
          </el-table-column>
          <el-table-column label="商品名称" align="left">
            <template slot-scope="scope">
              <a :href="MixinBuyerDomain + '/goods/' + scope.row.goods_id" class="goods-name" target="_blank">{{ scope.row.name }}</a>
              <p class="sku-spec">{{ scope.row | formatterSkuSpec }}</p>
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
  </div>
</template>

<script>
  import * as API_Receipt from '@/api/receipt'

  export default {
    name: 'receiptDetail',
    data() {
      return {
        id: this.$route.params.id,
        receipt: '',
        skuList: ''
      }
    },
    beforeRouteUpdate(to, from, next) {
      this.id = to.params.id
      next()
    },
    activated() {
      this.id = this.$route.params.id
    },
    mounted() {
      this.GET_ReceiptDetail()
    },
    watch: {
      id: 'GET_ReceiptDetail'
    },
    methods: {
      /** 获取开票详情 */
      GET_ReceiptDetail() {
        API_Receipt.getHistoryReceiptDetail(this.id).then(response => {
          this.receipt = response
          this.skuList = response.sku_list
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .container {
    padding: 0;
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
</style>
