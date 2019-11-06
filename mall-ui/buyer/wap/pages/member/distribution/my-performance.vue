<template>
  <div id="my-performance">
    <nav-bar title="我的业绩"/>
    <van-tabs v-model="active" sticky @change="tabChange">
      <van-tab v-for="(item, index) in performance" :key="index" :title="item.title">
        <!--结算单信息-->
        <div class="settlement-total-container">
          <span>本期佣金:<span class="color-red">{{ settlementTotal.final_money | unitPrice('¥') }}</span></span>
          <span>订单佣金:<span class="color-red">{{ settlementTotal.push_money | unitPrice('¥') }}</span></span>
          <span>退单佣金返还:<span class="color-red">{{ settlementTotal.return_push_money | unitPrice('¥') }}</span></span>
        </div>

        <van-collapse v-model="activeNames" v-if="performanceList && performanceList.length">
          <van-collapse-item
            class="cell-content"
            :border="false"
            :title="`${sn_text}：${item.sn}`"
            :name="_index"
            v-for="(item, _index) in performanceList"
            :key="_index">
            <!--订单金额-->
            <span v-if="item.order_price">订单金额：
              <span style="color: #f42424;">{{ item.order_price | unitPrice('¥') }}</span>
            </span>
            <!--会员名称-->
            <span v-if="item.member_name">会员名称：<span>{{ item.member_name }}</span></span>
            <!--下单时间-->
            <span v-if="item.create_time">下单时间：
              <span>{{ item.create_time | unixToDate('yyyy-MM-dd hh:mm') }}</span>
            </span>
            <!--返利金额-->
            <span v-if="item.price" >返利金额：
              <span style="color: #f42424;">{{ item.price | unitPrice('¥') }}</span>
            </span>
            <!--结算时间-->
            <span v-if="item.end_time">结算时间：
              <span>{{ item.end_time | unixToDate('yyyy-MM-dd hh:mm') }}</span>
            </span>
            <!--结算金额-->
            <span v-if="item.final_money">结算金额：
              <span style="color: #f42424;">{{ item.final_money | unitPrice('¥') }}</span>
            </span>
            <!--操作-->
            <van-button
              class="btn-custom"
              v-if="active === 2"
              size="small"
              type="default"
              plain
              @click="handleDetails(item)">详情</van-button>
          </van-collapse-item>
        </van-collapse>
        <empty-member v-else>暂无数据</empty-member>
      </van-tab>
    </van-tabs>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_distribution from '@/api/distribution'
  export default {
    name: 'my-performance',
    data() {
      return {
        /** 当前面板序列 */
        active: 0,

        /** 我的结算单 */
        settlementTotal: {
          push_money: 0,

          final_money: 0,

          return_order_money: 0
        },

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

        /** 业绩列表 */
        performanceList: [],

        performance: [
          { title: '与我相关的订单'},
          { title: '与我相关的退货单'},
          { title: '我的历史业绩'}
        ],

        /** 结算单id */
        billId: '',

        /** 当前折叠面板名称 */
        activeNames: [],

        /** 当前面板对应的文本名称 */
        sn_text: '订单编号'
      }
    },
    mounted() {
      this.GET_SettlementTotal()
    },
    methods: {
      /** 获取与我相关的结算单信息 */
      GET_SettlementTotal(){
        API_distribution.getSettlementTotal({member_id: this.$route.query.member_id || 0}).then(response => {
          this.settlementTotal = response
          this.billId = response.total_id
          this.params = {
            ...this.params,
            member_id: response.member_id,
            bill_id: response.total_id
          }
          switch (this.active) {
            case 0: this.GET_RelevantList()
              break
            case 1: this.GET_RelevantRefundList()
              break
            case 2: this.GET_MyhistoryList()
              break
          }
        })
      },

      /** 标签改变时触发 */
      tabChange(index) {
        this.active = index
        switch (index) {
          case 0: this.GET_RelevantList()
            break
          case 1: this.GET_RelevantRefundList()
            break
          case 2: this.GET_MyhistoryList()
            break
        }
      },

      /** 当前页数发生改变 */
      handleCurrentPageChange(cur) {
        this.params.page_no = cur
        switch (this.active) {
          case 0: this.GET_RelevantList()
            break
          case 1: this.GET_RelevantRefundList()
            break
          case 2: this.GET_MyhistoryList()
            break
        }
      },

      /** 获取与我相关的订单记录 */
      GET_RelevantList() {
        API_distribution.getRelevantList(this.params).then(response => {
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
          this.performanceList = response.data
        })
      },

      /** 获取我的历史业绩记录 */
      GET_MyhistoryList() {
        API_distribution.getMyHistoryList(this.params).then(response => {
          this.sn_text = '结算单编号'
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
          this.performanceList = response.data
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
          this.performanceList = response.data
        })
      },

      /** 查看详情 */
      handleDetails(item) {
        this.active = 0
        this.params = {
          ...this.params,
          member_id: item.member_id,
          bill_id: item.total_id
        }
        this.GET_RelevantList()
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .settlement-total-container {
    padding: 15px;
    width: 100%;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: space-around;
    align-items: center;
    .color-red {
      color: #f42424;
    }
  }
  /deep/ .van-collapse-item__wrapper {
    width: 100%;
  }
  /deep/ .van-collapse-item__content {
    width: 100%;
  }
  .cell-content {
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    .van-collapse-item__content {
      & > span {
        font-size: 12px;
        display: inline-block;
        margin: 10px 0;
        text-align: left;
        width: 50%;
      }
    }
    .btn-custom {
      flex-grow: 1;
    }
  }
</style>
