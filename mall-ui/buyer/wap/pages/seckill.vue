<template>
  <div id="seckill" class="seckill-container">
    <nav-bar title="限时抢购" fixed/>
    <van-tabs @click="handleClickTimeLine">
      <van-tab v-for="(timeLine, index) in timeLines" :key="index">
        <div slot="title" class="tab-title">
          <p class="time-title-timeline">{{ timeLine.time_text + ':00' }}</p>
          <p class="text-title-timeline">{{ timeLine.distance_time === 0 ? '正在抢购' : '即将开始' }}</p>
        </div>
      </van-tab>
    </van-tabs>
    <div v-if="timeLines" class="list-head">
      <div class="buy-txt" :class="[ timeLines[timeLineIndex].distance_time && 'no-start' ]">{{ timeLines[timeLineIndex].distance_time === 0 ? '抢购中' : '即将开始' }} 先下单先得哦</div>
      <div class="time">
        <span class="static-txt">{{ timeLines[timeLineIndex].distance_time === 0 ? '距结束' : '距开始' }}</span>
        <div class="time-text">
          <span class="seckill-time">{{ timesText[timeLineIndex].hours }}</span>
          <span class="time-separator">:</span>
          <span class="seckill-time">{{ timesText[timeLineIndex].minutes }}</span>
          <span class="time-separator">:</span>
          <span class="seckill-time">{{ timesText[timeLineIndex].seconds }}</span>
        </div>
      </div>
    </div>
    <div v-if="timeLines" class="goods-container">
      <en-empty v-if="finished && !goodsList.length" style="line-height:420px">暂无商品</en-empty>
      <van-list
        v-model="loading"
        :finished="finished"
        @load="onLoad"
      >
        <div v-for="(goods, index) in goodsList" :key="index" class="item-goods">
          <a :href="'/goods/' + goods.goods_id">
            <div class="skill-pic">
              <div class="img">
                <img :src="goods.goods_image">
              </div>
            </div>
            <p class="goods-name">{{ goods.goods_name }}</p>
            <p class="goods-price">
              <i>￥</i>{{ goods.seckill_price | unitPrice }}<span></span>
            </p>
            <div class="seckill-price">
              <p class="goods-price-odd">
                <del>￥{{ goods.original_price | unitPrice }}</del>
              </p>
              <div class="seckill-load">
                <span class="sale-count">已抢<em>{{ countProgress(goods) }}%</em></span>
                <div class="seckill-progress">
                  <div class="seckill-pro-bg">
                    <p class="seckill-iteam-progress">
                      <span class="seckill-pro-insetbg">
                        <span class="seckill-iteam-pro" :style="{ width: countProgress(goods) + '%' }"></span>
                      </span>
                    </p>
                  </div>
                </div>
              </div>
            </div>
            <span class="to-seckill-btn">去抢购</span>
          </a>
        </div>
      </van-list>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_Promotions from '@/api/promotions'
  import { Foundation } from '@/ui-utils'
  export default {
    name: 'seckill',
    head() {
      return {
        title: `限时抢购-${this.site.site_name}`
      }
    },
    data() {
      return {
        loading: false,
        finished: false,
        timeLines: '',
        timeLineIndex: 0,
        times: [],
        timesText: [],
        goodsList: [],
        params: {
          page_no: 0,
          page_size: 10
        },
        onlyOne: false
      }
    },
    computed: {
      /** 获取当前选中的时间节点是否开始 */
      seckillIsStart() {
        return this.timeLines.filter(item => item.active)[0].distance_time === 0
      }
    },
    mounted() {
      this.GET_TimeLine()
    },
    methods: {
      /** 加载数据 */
      onLoad(page_no) {
        this.params.page_no += 1
        this.GET_TimeLineGoods()
      },
      /** 时间段被选中 */
      handleClickTimeLine(timeLineIndex) {
        const { timeLines } = this
        const timeLine = timeLines[timeLineIndex]
        timeLines.map((item, index) => {
          item.active = index === timeLineIndex
          return item
        })
        this.timeLineIndex = timeLineIndex
        this.finished = false
        this.goodsList = []
        this.params.page_no = 1
        this.params.range_time = timeLine.time_text
        this.GET_TimeLineGoods()
      },
      /** 开始倒计时 */
      startCountDown() {
        this.interval = setInterval(() => {
          const { times, timesText } = this
          for (let i = 0; i < times.length; i ++) {
            if (i === 0 && times[i] === 0) {
              clearInterval(this.interval)
              this.$alert('新的限时抢购开始啦，请确认查看！', function () {
                location.reload()
              })
              break
            }
            times[i] -= 1
            const timeText = Foundation.countTimeDown(times[i])
            this.$set(this.timesText, i, Foundation.countTimeDown(times[i]))
          }
          this.$set(this, 'times', times)
        }, 1000)
      },
      /** 计算销售百分比 */
      countProgress(goods) {
        if (!this.seckillIsStart) return 0
        return (goods.sold_num / (goods.sold_num + goods.sold_quantity) * 100).toFixed(0)
      },
      /** 获取时间线 */
      GET_TimeLine() {
        API_Promotions.getSeckillTimeLine().then(response => {
          if (!response || !response.length) {
            this.$alert('暂时还没有限时抢购正在进行，去别处看看吧！', () => {
              this.$router.push({ path: '/' })
            })
            return false
          }
          response = response.sort((x, y) => (Number(x.time_text) - Number(y.time_text)))
          response = response.slice(0, 5)
          this.params.range_time = response[0].time_text
          const times = []
          const timesText = []
          const onlyOne = response.length === 1
          this.onlyOne = onlyOne
          response.map((item, index) => {
            item.active = index === 0
            if (item.distance_time === 0 && index === 0) {
              if (onlyOne) {
                times.push(Foundation.theNextDayTime())
              } else {
                times.push(response[1].distance_time)
              }
            } else {
              times.push(item.distance_time)
            }
            timesText.push({ hours: '00', minutes: '00', seconds: '00' })
            return item
          })
          this.times = times
          this.timesText = timesText
          this.timeLines = response
          this.startCountDown()
        })
      },
      /** 获取对应时刻的商品 */
      GET_TimeLineGoods() {
        API_Promotions.getSeckillTimeGoods(this.params).then(response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            this.goodsList.push(...data)
          }
        }).catch(() => {
          this.loading = false
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../assets/styles/color";
  /deep/ {
    .van-tabs {
      position: fixed;
      z-index: 10;
      top: 46px;
      width: 100%;
      .van-tabs__wrap {
        background-color: #fff;
      }
    }
    .tab-title {
      .time-title-timeline {
        font-size: 17px;
        line-height: 23px;
        font-weight: 700;
      }
      .text-title-timeline {
        font-size: 11px;
        line-height: 11px;
        margin-top: 3px;
      }
    }
  }
  .list-head {
    padding: 90px 10px 0 10px;
    vertical-align: middle;
    color: #252525;
    font-size: 0;
    background: #fff;
    height: 28px;
    line-height: 28px;
    border-top: none;
    > div {
      display: inline-block;
    }
    .buy-txt {
      float: left;
      color: $color-main;
      font-size: 13px;
      &.no-start {
        color: #232326;
      }
    }
    .time {
      float: right;
      line-height: 25px;
      height: 25px;
    }
    .static-txt {
      color: #232326;
      display: inline-block;
      font-size: 12px;
      margin-right: 5px;
    }
    .time-text {
      height: 16px;
      display: inline-block;
    }
    .seckill-time {
      display: inline-block;
      background-color: #000;
      width: 16px;
      height: 16px;
      color: #fff;
      font-size: 10px;
      line-height: 16px;
      text-align: center;
      border-radius: 3px;
      -webkit-border-radius: 3px;
    }
    .time-separator {
      display: inline-block;
      width: 6px;
      font-size: 12px;
    }
  }
  .goods-container {
    position: relative;
    &::before {
      background-color: #e3e5e9;
      position: absolute;
      top: -1px;
      left: 0;
      -webkit-transform: scale(.5,.5);
      transform: scale(.5,.5);
      -webkit-transform-origin: 0 0;
      transform-origin: 0 0;
      width: 200%;
      height: 1px;
      content: '';
    }
    /deep/ .van-list {
      padding: 0 10px;
    }
  }
  .item-goods {
    height: 120px;
    position: relative;
    padding: 15px 0;
    min-height: 132px;
    box-sizing: border-box;
    &::after {
      background-color: #e3e5e9;
      position: absolute;
      bottom: -1px;
      left: 0;
      transform: scale(.5,.5);
      transform-origin: 0 0;
      width: 200%;
      height: 1px;
      content: '';
    }
    a {
      display: block;
      text-decoration: none;
      overflow: hidden;
      font-size: 14px;
      color: #232326;
    }
    .skill-pic {
      position: relative;
    }
    .img {
      float: left;
      vertical-align: middle;
      text-align: center;
      margin-right: 4px;
      border-radius: 0;
      width: 102px;
      height: 102px;
      img {
        width: 100%;
        height: 100%;
      }
    }
    .goods-name {
      height: 32px;
      overflow: hidden;
      font-size: 14px;
      line-height: 16px;
      margin-bottom: 5px;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      color: #232326;
    }
    .goods-price {
      margin: 29px 0 7px;
      color: $color-main;
      font-size: 20px;
      i {
        font-size: 11px;
      }
    }
    .goods-price-odd {
      color: #848689;
      font-size: 12px;
    }
    .seckill-price {
      line-height: 12px;
    }
    .seckill-load {
      position: absolute;
      right: 0;
      bottom: 16px
    }
    .sale-count {
      font-size: 10px;
      color: #848689;
      display: inline-block;
    }
    .seckill-progress {
      display: inline-block;
      width: 72px;
      margin-left: 4px;
      height: 8px;
      overflow: hidden;
    }
    .seckill-pro-bg {
      background: rgba(255,255,255,.8);
      width: 100%;
      position: relative;
      left: auto;
      bottom: auto;
      padding: 0;
      height: 7px;
    }
    .seckill-iteam-progress {
      position: relative;
      left: 0;
      width: 200%;
      transform: scale(.5,.5);
      transform-origin: 0 0;
      border: solid 1px $color-main;
      border-radius: 50px;
      overflow: hidden;
      top: 0;
      height: 15px;
      box-sizing: border-box;
    }
    .seckill-pro-insetbg {
      width: 100%;
      height: 100%;
      display: inline-block;
      border: solid 1px rgba(255,255,255,.2);
    }
    .seckill-iteam-pro {
      position: absolute;
      top: 0;
      left: -1px;
      background-image: linear-gradient(to right,#ff8989,#f67373);
      height: 100%;
      display: block;
      width: 0;
      transition: width 1.8s;
    }
    .to-seckill-btn {
      position: absolute;
      right: 0;
      color: #fff;
      text-align: center;
      vertical-align: middle;
      width: 72px;
      height: 27px;
      line-height: 27px;
      background-color: $color-main;
      font-size: 14px;
      bottom: 32px;
      border-radius: 2px;
    }
  }
</style>
