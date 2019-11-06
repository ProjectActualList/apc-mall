<template>
  <div id="website-message" style="background-color: #f7f7f7">
    <nav-bar title="站内消息"/>
    <van-tabs v-model="tabActive" :line-width="100">
      <van-tab title="未读消息"/>
      <van-tab title="全部消息"/>
    </van-tabs>
    <div class="message-container">
      <empty-member v-if="finished && !messageList.length">暂无消息</empty-member>
      <van-list
        v-else
        v-model="loading"
        :finished="finished"
        @load="onLoad"
      >
        <ul class="message-list">
          <li class="message-item" v-for="(message, index) in messageList" :key="index">
            <div class="msg-date">{{ message.send_time | unixToDate }}</div>
            <div class="msg-content">
              <van-swipe-cell :right-width="65">
                <p v-if="message.title" class="msg-title">{{ message.title }}</p>
                <div class="msg-detail">{{ message.content }}</div>
                <span slot="right" @click="handleDeleteMessage(message)">删除</span>
              </van-swipe-cell>
            </div>
          </li>
        </ul>
      </van-list>
    </div>
  </div>
</template>

<script>
  import { mapActions, mapGetters } from 'vuex'
  import * as API_Message from '@/api/message'
  export default {
    name: 'website-message',
    head() {
      return {
        title: `站内消息-${this.site.site_name}`
      }
    },
    data() {
      return {
        loading: false,
        finished: false,
        tabActive: this.$route.query.type === 'all' ? 1 : 0,
        params: {
          page_no: 0,
          page_size: 10
        },
        messageList: []
      }
    },
    watch: {
      tabActive: function () {
        this.GET_MessageList(true)
      }
    },
    methods: {
      onLoad(page) {
        this.params.page_no += 1
        this.GET_MessageList()
      },
      /** 删除消息 */
      handleDeleteMessage(message) {
        this.$confirm('确定要删除这条消息吗？', () => {
          API_Message.deleteMessage(message.id).then(() => {
            this.$message.success('删除成功！')
            this.GET_MessageList(true)
          })
        })
      },
      /** 标记消息为已读 */
      handleReadMessage(message) {
        API_Message.messageMarkAsRead(message.id).then(() => {
          this.GET_MessageList(true)
        })
      },
      /** 标记当前页消息问已读 */
      handleReadPageMessages() {
        const ids = this.tableData.data.map(item => item.id).join(',')
        API_Message.messageMarkAsRead(ids).then(() => {
          this.GET_MessageList()
        })
      },
      /** 获取站内消息 */
      GET_MessageList(reset = false){
        this.loading = true
        if (reset) {
          this.params.page_no = 1
          this.finished = false
          this.messageList = []
        }
        const params = JSON.parse(JSON.stringify(this.params))
        if (this.tabActive !== 1) {
          params.read = 0
        } else {
          delete params.read
        }
        API_Message.getMessages(params).then(async response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            if (params.read === 0) {
              const ids = data.map(item => item.id).join(',')
              await API_Message.messageMarkAsRead(ids)
            }
            this.messageList.push(...data)
          }
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .message-container {
    background-color: #f7f7f7;
  }
  .message-list {
    margin: 0 10px;
  }
  .msg-date {
    padding-top: 15px;
    padding-bottom: 10px;
    width: 100%;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    font-size: 10px;
    line-height: 14px;
    color: #848689;
    text-align: center;
    background-color: #f7f7f7;
  }
  .msg-content {
    overflow: hidden;
    font-size: 14px;
    color: #343434;
    line-height: 14px;
    background-color: #fff;
    border-radius: 8px;
  }
  .msg-title {
    padding: 10px 10px 0 10px;
    line-height: 21px;
    font-size: 15px;
    font-weight: 700;
    color: #343434;
  }
  .msg-detail {
    padding: 15px 10px;
  }
</style>
