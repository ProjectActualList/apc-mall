<template>
  <div v-if="time !== false && goodsList && goodsList.length" class="seckill-container">
    <div class="title-seckill">
      <nuxt-link class="seckill-left-link" to="/seckill">
        <strong class="seckill-tit-txt">限时抢购</strong>
        <!--<strong class="seckill-nth"><em>10</em>点场</strong>-->
        <div class="seckill-timer" id="seckill_time">
          <div class="seckill-time"><span>{{ times.hours }}</span></div>
          <span class="seckill-time-separator">:</span>
          <div class="seckill-time"><span>{{ times.minutes }}</span></div>
          <span class="seckill-time-separator">:</span>
          <div class="seckill-time"><span>{{ times.seconds }}</span></div>
        </div>
        <span id="seckill_loading"></span>
      </nuxt-link>
      <nuxt-link class="seckill-more-link" to="/seckill">
        更多限时抢购
        <i class="seckill-more-icon">&gt;</i>
      </nuxt-link>
    </div>
    <div class="seckill-slider">
      <no-ssr>
        <swiper :options="swiperOptions">
          <swiper-slide v-for="(goods, index) in goodsList" :key="index" class="seckill-item">
            <div class="seckill-item-img">
              <a :href="'/goods/' + goods.goods_id" class="seckill-new-link">
                <img :src="goods.goods_image" class="seckill-photo">
              </a>
            </div>
            <div class="seckill-item-price">
              <span class="new-price"><em>¥</em>{{ goods.seckill_price | unitPrice }}</span>
              <a class="original-price"><em>¥</em>{{ goods.original_price | unitPrice }}</a>
            </div>
          </swiper-slide>
        </swiper>
      </no-ssr>
    </div>
  </div>
</template>

<script>
  import * as API_Promotions from '@/api/promotions'
  import { Foundation } from '@/ui-utils'
  export default {
    name: 'index-seckill',
    data() {
      return {
        // 时间
        time: false,
        // 商品列表
        goodsList: '',
        // 时间
        times: {
          hours: '00',
          minutes: '00',
          seconds: '00'
        },
        // 是否只有一场
        onlyOne: false,
        // swiper配置
        swiperOptions: {
          autoplay: false,
          slidesPerView: 'auto'
        }
      }
    },
    mounted() {
      this.GET_TimeLine()
    },
    methods: {
      /** 开始倒计时 */
      startCountDown() {
        this.interval = setInterval(() => {
          let { time } = this
          if (time <= 0) {
            clearInterval(this.interval)
            this.$alert(this.onlyOne ? '本轮限时抢购已结束！' : '下一轮限时抢购已经开始啦！请确认查看', function () {
              location.reload()
            })
            return false
          }
          time -= 1
          const timeText = Foundation.countTimeDown(time)
          this.$set(this, 'times', Foundation.countTimeDown(time))
          this.$set(this, 'time', time)
        }, 1000)
      },
      /** 获取时间段 */
      GET_TimeLine() {
        API_Promotions.getSeckillTimeLine().then(response => {
          if (!response || !response.length) {
            return
          }
          response = response.sort((x, y) => (Number(x.time_text) - Number(y.time_text)))
          const onlyOne = response.length === 1
          this.onlyOne = onlyOne
          this.time = onlyOne ? Foundation.theNextDayTime() : response[1].distance_time
          this.startCountDown()
          this.GET_GoodsList(response[0].time_text)
        })
      },
      /** 获取商品列表 */
      GET_GoodsList(range_time) {
        API_Promotions.getSeckillTimeGoods({ range_time }).then(response => {
          this.goodsList = response.data
        })
      }
    },
    destroyed() {
      this.interval && clearInterval(this.interval)
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .seckill-container {
    box-shadow: 0 1px 1px #f2f2f2;
    background: #fff;
    margin-bottom: 10px;
  }
  .title-seckill {
    display: flex;
    height: 34px;
    line-height: 34px;
    vertical-align: middle;
    .seckill-left-link {
      display: inline-block;
      width: 68%;
    }
    .seckill-tit-txt {
      margin: 0 6px 0 6px;
      display: inline-block;
      vertical-align: middle;
      font-size: 16px;
      color: $color-main;
      font-weight: 700;
    }
    .seckill-nth {
      font-size: 12px;
      color: #232326;
      font-weight: 700;
      display: inline-block;
      vertical-align: middle;
      em {
        font-size: 13px;
        position: relative;
        top: 1px;
      }
    }
    .seckill-timer {
      display: inline-block;
      vertical-align: middle;
      margin-left: 6px;
    }
    .seckill-time, .seckill-time-separator {
      float: left;
      display: inline-block;
      line-height: 16px;
      height: 16px;
      font-size: 12px;
      text-align: center;
      color: #232326;
    }
    .seckill-time {
      width: 18px;
      line-height: 17px;
      color: #232326;
      font-size: 12px;
      position: relative;
      &::after {
        border-radius: 2px;
        height: 200%;
        content: "";
        width: 200%;
        border: 1px solid #dfdfdf;
        position: absolute;
        top: -1px;
        left: -1px;
        transform: scale(.5);
        transform-origin: left top;
        z-index: 10;
      }
    }
    .seckill-time-separator {
      width: 6px;
    }
    .seckill-more-link {
      width: 32%;
      display: inline-block;
      color: $color-main;
      font-size: 10px;
      text-align: right;
      position: relative;
      padding-right: 22px;
      .seckill-more-icon {
        width: 12px;
        height: 12px;
        background-color: $color-main;
        color: #fff;
        border-radius: 100%;
        display: inline-block;
        font-size: 12px;
        vertical-align: middle;
        text-align: center;
        line-height: 12px;
      }
    }
  }
  .seckill-slider {
    width: 100%;
    height: 100%;
    overflow: hidden;
    font-size: 0;
  }
  .seckill-item {
    width: 80px;
  }
  .seckill-item-img {
    width: 100%;
    padding: 0 5px;
    box-sizing: border-box;
    .seckill-new-link {
      display: block;
      position: relative;
    }
    img {
      width: 100%;
      height: auto;
      overflow: hidden;
    }
  }
  .seckill-item-price {
    margin: 0 auto;
    text-align: center;
    .new-price {
      margin-top: 10px;
      display: block;
      color: $color-main;
      font-size: 12px;
      line-height: 16px;
      height: 16px;
      text-align: center;
      font-weight: 700;
      em {
        font-size: 11px;
        padding-right: 2px;
      }
    }
    .original-price {
      color: #686868;
      font-size: 12px;
      line-height: 12px;
      margin: 4px 0 11px;
      text-align: center;
      padding: 0 2px;
      display: inline-block;
      position: relative;
      text-decoration: line-through;
      em {
        font-size: 9px;
        position: relative;
        padding-right: 2px;
      }
    }
  }
</style>
