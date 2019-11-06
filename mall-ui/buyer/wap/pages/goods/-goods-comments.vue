<template>
  <div id="goods-comments" class="goods-comments">
    <van-cell-group class="comment-cells">
      <van-cell value="全部评价" is-link @click="showPopup = true">
        <div slot="title">好评率：<span class="rate">{{ grade }}%</span></div>
      </van-cell>
      <div v-if="finished && !comments.length" class="no-comments">暂无评价</div>
      <div v-else class="some-comments">
        <template v-for="(comment, index) in comments">
          <div v-if="index < 3" :key="index" class="com-item">
            <div class="com-user">
              <span class="user">{{ comment.member_name }}</span>
              <span class="credit">{{ comment.grade | filterGrade }}</span>
              <span class="date">{{ comment.create_time | unixToDate('yyyy-MM-dd') }}</span>
            </div>
            <div class="com-content">{{ comment.content }}</div>
            <div v-if="comment.images && comment.images.length" class="com-gallery">
            <span v-for="(img, index) in comment.images" :key="index" class="img">
              <img :src="img" @click="handleImagePreview(comment.images, index)">
            </span>
            </div>
          </div>
        </template>
        <div class="com-more">
          <a href="javascript:;" class="com-more-link" @click="showPopup = true">查看全部评价</a>
        </div>
      </div>
    </van-cell-group>
    <van-popup v-model="showPopup" position="bottom" style="height:100%">
      <van-nav-bar title="商品评价" left-arrow @click-left="showPopup = false"/>
      <div v-if="finished && !comments.length" class="no-comments" style="padding-top: 50px">暂无评价</div>
      <van-list
        v-else
        v-model="loading"
        :finished="finished"
        @load="onLoad"
        class="comments-list"
      >
        <div v-for="(comment, index) in comments" :key="index" class="com-item" :class="[index === comments.length - 1 && 'last']">
          <div class="com-user">
            <span class="user">{{ comment.member_name }}</span>
            <span class="credit">{{ comment.grade | filterGrade }}</span>
            <span class="date">{{ comment.create_time | unixToDate('yyyy-MM-dd') }}</span>
          </div>
          <div class="com-content">{{ comment.content }}</div>
          <div v-if="comment.images && comment.images.length" class="com-gallery">
            <span v-for="(img, index) in comment.images" :key="index" class="img">
              <img :src="img" @click="handleImagePreview(comment.images, index)">
            </span>
          </div>
          <div v-if="comment.reply_status === 1 && comment.reply" class="com-reply">
            <span>商家回复：</span>
            <span>{{ comment.reply.content }}</span>
          </div>
        </div>
      </van-list>
    </van-popup>
  </div>
</template>

<script>
  /**
   * 商品评论模块
   * 这里只负责展示商品的评论
   */
  import * as API_Members from '@/api/members'
  import { ImagePreview } from 'vant'
  export default {
    name: "goods-comments",
    props: ['goods-id', 'grade'],
    data() {
      return {
        loading: false,
        finished: false,
        params: {
          page_no: 1,
          page_size: 10,
          grade: ''
        },
        comments: [],
        // 显示全部评论
        showPopup: false
      }
    },
    mounted() {
      this.GET_GoodsComments()
    },
    filters: {
      /** 评分 */
      filterGrade(val) {
        switch (val) {
          case 'bad':
            return '差评'
          case 'neutral':
            return '中评'
          default:
            return '好评'
        }
      }
    },
    methods: {
      /** 显示图片预览 */
      handleImagePreview(images, index) {
        ImagePreview({images, startPosition: index})
      },
      /** 当页数发生改变时 */
      onLoad() {
        this.params.page_no += 1
        this.GET_GoodsComments()
      },
      /** 获取商品评论 */
      GET_GoodsComments(){
        this.loading = true
        API_Members.getGoodsComments(this.goodsId, this.params).then(response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            this.comments.push(...data)
          }
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .comment-cells {
    .rate {
      color: $color-main;
      margin-left: 3px;
      font-size: 12px;
    }
  }
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
      margin: 5px 0;
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
  .no-comments {
    line-height: 50px;
    text-align: center;
  }
  .com-reply {
    margin-top: 5px;
    color: $color-main;
  }
</style>
