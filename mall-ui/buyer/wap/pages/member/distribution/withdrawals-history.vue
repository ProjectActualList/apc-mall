<template>
  <div id="withdrawals-history">
    <nav-bar title="提现记录"/>
    <div class="withdrawals-container">
      <empty-member v-if="finished && !withdrawalsList.length">暂无提现记录</empty-member>
      <van-list
        v-else
        v-model="loading"
        :finished="finished"
        @load="onLoad"
      >
        <van-collapse v-model="activeNames" v-if="withdrawalsList && withdrawalsList.length">
          <van-collapse-item
            :border="false"
            :name="_index"
            v-for="(item, _index) in withdrawalsList"
            :key="_index">
            <!--提现时间-->
            <span slot="title">提现时间：{{ item.apply_time | unixToDate('yyyy-MM-dd hh:mm') }}</span>
            <div class="colla-content">
              <!--提现状态-->
              <span>提现状态:{{ item.status | withdraealsStatus }}</span>
              <!--提现金额-->
              <span>提现金额:<i style="color: #f42424;">{{ item.apply_money | unitPrice('¥') }}</i></span>
            </div>
            <div @click="lookDetails(item)" style="color: #ff853f;">查看详情</div>
          </van-collapse-item>
        </van-collapse>
      </van-list>
    </div>
    <van-popup v-model="isShowDialog"  position="bottom" style="height:100%">
      <van-nav-bar title="提现详情" @click-right="isShowDialog = false">
        <i class="iconfont ea-icon-close" slot="right"/>
      </van-nav-bar>
      <div class="pop-details">
        <ul>
          <li><span>提现金额</span>: <span style="color: #f42424;">{{ currentRow.apply_money | unitPrice('¥') }}</span></li>
          <li><span>当前状态</span>: <span>{{ currentRow.status | withdraealsStatus }}</span></li>
          <li><span>备注信息</span>: <span>{{ currentRow.apply_remark }}</span></li>
          <li><span>提现日志</span>: <span>{{ currentRow.transfer_remark }}</span></li>
          <li><span>审核备注</span>: <span>{{ currentRow.inspect_remark }}</span></li>
        </ul>
      </div>
    </van-popup>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_distribution from '@/api/distribution'
  export default {
    name: 'withdrawals-history',
    data() {
      return {
        loading: false,

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

        /** 折叠面板循环索引 */
        activeNames: [],

        /** 提现记录列表 */
        withdrawalsList: [],

        /** 是否显示详情 */
        isShowDialog: false,

        /** 当前行的对象 */
        currentRow: {},

        /** 是否加载完成 */
        finished: false
      }
    },
    filters: {
      withdraealsStatus(val) {
        switch (val) {
          case 'APPLY': return '申请中'
            break
          case 'TRANSFER_ACCOUNTS': return '已转账'
            break
          case 'VIA_AUDITING': return '审核通过'
            break
          case 'FAIL_AUDITING': return '审核失败'
            break
        }
      }
    },
    methods: {
      /** 加载数据 */
      onLoad() {
        this.params.page_no += 1
        this.GET_WithdrawalsList()
      },

      /** 获取提现记录 */
      GET_WithdrawalsList() {
        this.loading = true
        API_distribution.getWithdrawalsList(this.params).then(response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            this.withdrawalsList.push(...data)
          }
        })
      },

      /** 查看详情 */
      lookDetails(item) {
        this.isShowDialog = true
        this.currentRow = item
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .withdrawals-container {
    padding-top: 46px;
  }
  /deep/ .van-collapse-item__wrapper {
    width: 100%;
  }
  /deep/ .van-collapse-item__content {
    display: flex;
    flex-wrap: nowrap;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }
  .colla-content {
    display: flex;
    flex-wrap: nowrap;
    flex-direction: column;
    justify-content: space-between;
    align-items: flex-start;
    span {
      font-size: 12px;
      display: inline-block;
      margin: 5px 0;
      text-align: left;
    }
  }
  /** 弹层 */
  .pop-details {
    padding-top: 46px;
    ul {
      li {
        list-style:none;
        margin: 20px 0;
        padding: 0 20px;
        span {
          font-size: 14px;
        }
      }
    }
  }
</style>
