<template>
  <div id="my-performance-refund">
    <div class="member-nav">
      <ul class="member-nav-list">
        <li><nuxt-link to="./my-performance">与我相关的订单</nuxt-link></li>
        <li><nuxt-link to="./my-performance-refund">与我相关的退货单</nuxt-link></li>
        <li><nuxt-link to="./my-performance-history">我的历史业绩</nuxt-link></li>
      </ul>
    </div>
    <div class="recommend-container">
      <div class="achievement-summary">
        <div>
          <span class="current-money performance-money">本期佣金：</span>
          <span> {{ settlementTotal.start_time | unixToDate('yyyy-MM-dd') }} ～ {{ settlementTotal.end_time | unixToDate('yyyy-MM-dd') }} </span>
        </div>
        <div>
          <span class="finally-money performance-money">{{ settlementTotal.final_money | unitPrice('¥') }}</span>
          <span>最终佣金</span>
        </div>
        <div>
          <span class="performance-symbol">=</span>
        </div>
        <div>
          <span class="summary-money performance-money">{{ settlementTotal.push_money | unitPrice('¥') }}</span>
          <span>订单佣金</span>
        </div>
        <div>
          <span class="performance-symbol">-</span>
        </div>
        <div>
          <span class="refund-money performance-money">{{ settlementTotal.return_push_money | unitPrice('¥') }}</span>
          <span>退单佣金返还</span>
        </div>
      </div>
      <el-table :data="relevantRefundList" style="width: 100%">
        <el-table-column prop="sn" label="订单编号" align="center" width="200"/>
        <el-table-column label="订单金额" align="center">
          <template slot-scope="scope">
            <span class="price">{{ scope.row.orer_price | unitPrice('¥') }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="member_name" label="会员名称" align="center"/>
        <el-table-column label="返利金额" align="center">
          <template slot-scope="scope">
            <span class="price">{{ scope.row.price | unitPrice('¥') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下单时间" align="center" width="200">
          <template slot-scope="scope">
            <span class="price">{{ scope.row.create_time | unixToDate('yyyy-MM-dd hh:mm') }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div class="member-pagination">
        <el-pagination
          @current-change="handleCurrentPageChange"
          :current-page.sync="pageData.page_no"
          :page-size="pageData.page_size"
          layout="total, prev, pager, next"
          :total="pageData.data_total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import { Table, TableColumn, Pagination } from 'element-ui'
  Vue.use(Table).use(TableColumn).use(Pagination)
  import * as API_distribution from '@/api/distribution'
  export default {
    name: 'my-performance-refund',
    data() {
      return {
        /** 分页请求参数 */
        params: {
          page_no: 1,
          page_size: 10
        },

        /** 分页信息 */
        pageData: {
          page_no: 1,
          page_size: 10,
          data_total: 0
        },

        /** 我的结算单 */
        settlementTotal: {
          start_time: 0,

          end_time: 0,

          push_money: 0,

          final_money: 0,

          return_order_money: 0
        },

        /** 与我相关的订单 */
        relevantRefundList: []
      }
    },
    mounted() {
      this.GET_SettlementTotal()
    },
    methods: {
      /** 当前页数发生改变 */
      handleCurrentPageChange(cur) {
        this.params.page_no = cur
        this.GET_RelevantRefundList()
      },

      /** 获取与我相关的结算单信息 */
      GET_SettlementTotal(){
        API_distribution.getSettlementTotal({member_id: this.$route.query.member_id || 0}).then(response => {
          this.settlementTotal = response
          this.params = {
            ...this.params,
            member_id: response.member_id,
            bill_id: response.total_id
          }
          this.GET_RelevantRefundList()
        })
      },

      /** 获取与我相关的退款单记录 */
      GET_RelevantRefundList() {
        API_distribution.getRelevantRefundList(this.params).then(response => {
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
          this.relevantRefundList = response.data
        })
      },
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /*业绩总结*/
  .achievement-summary {
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: stretch;
    margin: 15px 0 20px;
    padding: 0;
    border: 1px solid #ddd;
    & > div {
      margin:0 25px;
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      align-items: center;
      & > span {
        display:inline-block;
        line-height: 40px;
      }
      span.performance-money {
        font-size: 20px;
        font-weight: 700;
      }
      span.current-money {
        color: #00a0e9;
      }
      span.finally-money, span.summary-money {
        color: #D93600;
      }
      span.refund-money {
        color: #aaa;
      }
      span.performance-symbol {
        font-family: "Yuanti SC", Arial, Helvetica, sans-serif;
        font-size: 60px;
        color: #aaa;
      }
    }
  }
</style>
