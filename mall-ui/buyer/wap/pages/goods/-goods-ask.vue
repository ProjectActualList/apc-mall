<template>
  <div id="goods-ask">
    <van-cell-group class="ask-cells">
      <van-cell title="商品咨询" value="查看全部咨询" is-link @click="showPopup = true"/>
      <div v-if="finished && !asks.length" class="no-ask">暂无咨询</div>
      <div v-else class="some-comments">
        <template v-for="(ask, index) in asks">
          <div v-if="index < 3" :key="index" class="com-item">
            <div class="com-content">{{ ask.content }}</div>
          </div>
        </template>
      </div>
      <div class="com-more">
        <a href="javascript:;" class="com-more-link" @click="handleAsk">我也要咨询</a>
      </div>
    </van-cell-group>
    <van-popup v-model="showPopup" position="bottom" style="height:100%">
      <van-nav-bar title="商品咨询" left-arrow @click-left="showPopup = false">
        <span slot="right" @click="handleAsk">我要咨询</span>
      </van-nav-bar>
      <div v-if="finished && !asks.length" class="no-ask" style="padding-top: 50px">暂无咨询</div>
      <van-list
        v-else
        v-model="loading"
        :finished="finished"
        @load="onLoad"
        class="comments-list"
      >
        <div v-for="(ask, index) in asks" :key="index" class="com-item" :class="[index === asks.length - 1 && 'last']">
          <div class="com-user">
            <span class="user">{{ ask.member_name }}</span>
            <span class="date">{{ ask.create_time | unixToDate('yyyy-MM-dd') }}</span>
          </div>
          <div class="com-content">{{ ask.content }}</div>
          <div v-if="ask.reply_status === 1 && ask.reply" class="com-reply">
            <span>商家回复：</span>
            <span>{{ ask.reply }}</span>
          </div>
        </div>
      </van-list>
    </van-popup>
    <van-dialog
      v-model="showAskDialog"
      show-cancel-button
      :before-close="beforeClose"
    >
      <van-field
        v-model="ask"
        type="textarea"
        clearable
        placeholder="请输入咨询内容，长度在200个字以内。"
      />
    </van-dialog>
  </div>
</template>

<script>
  import * as API_Members from '@/api/members'
  import Storage from '@/utils/storage'
  export default {
    name: 'goods-ask',
    props: ['goodsId'],
    data() {
      return {
        loading: false,
        finished: false,
        params: {
          page_no: 1,
          page_size: 10
        },
        asks: [],
        // 咨询列表弹出框
        showPopup: false,
        // 咨询内容
        ask: '',
        // 显示咨询dialog
        showAskDialog: false
      }
    },
    mounted() {
      this.GET_Asks()
    },
    methods: {
      /** 我也要咨询 */
      handleAsk() {
        if (Storage.getItem('refresh_token')) {
          this.ask = ''
          this.showAskDialog = true
        } else {
          this.$message.error('您未登录！')
        }
      },
      beforeClose(action, done) {
        if(!this.ask && action === 'confirm'){
          this.$message.error('请输入内容')
          done(false)
          return
        }
        if (action === 'confirm') {
          API_Members.consultating(this.goodsId, this.ask).then(() => {
            done()
            this.ask = ''
            this.$message.success('已提交咨询！')
          })
        } else {
          done()
        }
      },
      /** 当页数发生改变时 */
      onLoad() {
        this.params.page_no += 1
        this.GET_Asks()
      },
      /** 获取商品咨询 */
      GET_Asks() {
        this.loading = true
        API_Members.getGoodsConsultations(this.goodsId, this.params).then(response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            this.asks.push(...data)
          }
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .some-comments {
    padding: 0 15px;
  }
  .com-item {
    position: relative;
    padding: 10px 0;
    &::after {
      content: "";
      height: 0;
      display: block;
      border-bottom: 1px solid #ddd;
      position: absolute;
      left: 0;
      right: 0;
      bottom: 0;
    }
    .user {
      display: inline-block;
      color: #999;
      width: 60px;
      vertical-align: middle;
      overflow: hidden;
      text-overflow:ellipsis;
      white-space: nowrap;
    }
    .credit {
      display: inline-block;
      vertical-align: middle;
      margin-left: 3px;
    }
    .date {
      float: right;
      color: #999;
      margin-left: -60px;
    }
    .com-content {
      position: relative;
      line-height: 1.3;
      /*margin: 5px 0;*/
      word-break: break-all;
      overflow: hidden;
    }
    .com-gallery {
      margin-right: -10px;
      padding-bottom: 4px;
      white-space: nowrap;
      -webkit-overflow-scrolling: touch;
      overflow: hidden;
      overflow-x: auto;
      .img {
        width: 68px;
        height: 68px;
        display: inline-block;
        margin-right: 3px;
        overflow: hidden;
        text-align: center;
        background: #f3f3f3;
        margin-bottom: 1px;
      }
      img {
        width: auto;
        display: inline-block;
        height: auto;
        min-width: 68px;
        max-height: 68px;
      }
    }
  }
  .com-more {
    text-align: center;
    background-color: #fff;
    .com-more-link {
      height: 25px;
      line-height: 25px;
      font-size: 12px;
      text-align: center;
      color: #333;
      padding: 0 10px;
      margin: 10px 0;
      position: relative;
      display: inline-block;
      &::after {
        content: "";
        display: block;
        border: 2px solid #ccc;
        position: absolute;
        top: 0;
        left: 0;
        pointer-events: none;
        -webkit-transform: scale(.5);
        -webkit-transform-origin: 0 0;
        bottom: -100%;
        right: -100%;
        border-radius: 40px;
      }
    }
  }
  .comments-list {
    padding: 46px 15px 15px 15px;
    .com-item.last::after { content: none }
  }
  .com-reply {
    margin-top: 2px;
    color: $color-main;
  }
  .no-ask {
    line-height: 50px;
    text-align: center;
  }
</style>
