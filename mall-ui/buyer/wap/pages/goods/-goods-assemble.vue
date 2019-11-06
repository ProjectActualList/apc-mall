<template>
  <div class="prom-bar-container" v-if="is_assemble">
    <div class="msg">
      <div class="text">
        <span class="goods-price">{{ assemble.sales_price | unitPrice('¥') }}</span>
        <span>拼团省{{ reduce_moeny | unitPrice('¥') }}</span>
      </div>
      <div class="text">
        <!--3人拼团-->
        <span>
          <span class="icon assembld">{{ assemble.required_num }}人拼团</span>
          <del class="old-price">{{ assemble.origin_price | unitPrice('¥') }}</del>
        </span>
        <span>已团{{ assemble.sold_quantity }}件</span>
      </div>
    </div>
    <!--倒计时-->
    <div class="count-down">
      <p class="count-down-text">距离结束还剩</p>
      <p class="count-down-times">
        <span v-if="day > 0" class="day">{{ day }}天</span>
        <span class="time">{{ hours }}</span>:
        <span class="time">{{ minutes }}</span>:
        <span class="time">{{ seconds }}</span>
      </p>
    </div>
  </div>
</template>

<script>
  import { Foundation } from '@/ui-utils'
  export default {
    name: 'goods-assemble',
    props: ['assemble', 'is_assemble'],
    data() {
      return {
        timer: null,
        day: 0,
        hours: '00',
        minutes: '00',
        seconds: '00'
      }
    },
    mounted() {
      this.assemble.time_left && this.contDown(this.assemble.time_left)
    },
    watch: {
      'assemble.time_left': function () {
        this.assemble.time_left && this.contDown(this.assemble.time_left)
      }
    },
    computed: {
      reduce_moeny() {
        return this.assemble.origin_price - this.assemble.sales_price
      }
    },
    methods: {
      contDown(times) {
        let end_ime = times
        this.timer = setInterval(() => {
          if (end_ime <= 0) {
            clearInterval(this.timer)
            this.$emit('count-end')
          } else {
            const time = Foundation.countTimeDown(end_ime)
            this.day = parseInt(time.day)
            this.hours = time.hours
            this.minutes = time.minutes
            this.seconds = time.seconds
            end_ime--
          }
        }, 1000)
      }
    },
    destroyed() {
      clearInterval(this.timer)
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .prom-bar-container {
    position: relative;
    display: flex;
    align-items: center;
    z-index: 10;
    height: 49px;
    color: #fff;
    background: url(../../assets/images/background-promotion-bar.png) no-repeat,-webkit-linear-gradient(top,#fc5997,#ef4747);
    background-size: 259px,100%;
    .msg {
      flex: 1;
      font-size: 12px;
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      align-items: flex-start;
      height: 100%;
      padding: 5px;
      .text {
        width: calc(100% - 125px);
        display: flex;
        flex-wrap: nowrap;
        justify-content: space-between;
        align-items: center;
        .goods-price {
          font-weight: bold;
          & > em {
            font-size: 16px;
          }
        }
      }
      .icon {
        position: relative;
        display: inline-block;
        margin: -2px 15px 0 0;
        padding: 0 3px 0 18px;
        height: 15px;
        vertical-align: middle;
        line-height: 15px;
        font-size: 10px;
        &.assembld::before {
          background: #fff url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYBAMAAAASWSDLAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAwUExURUdwTNgqL+M4O+Q5O/8AAOQ5POM4O+Q4POQ5POQ4O+M4O+Q5OeQ5O+M5POQ4POQ5PF/0sUwAAAAPdFJOUwAGVuAC+ZDVrGlwKL3xQxKSapEAAACzSURBVBjTY2BgYOD9////BQYoQOEwMPwHYgFkjg2EJ9T8v0KRIf8giM1kD9TzWcHtB5DNkv99teCu+m8uOUAO29cpQNIzPgGkSj8OrPHpJyDBaD+BgecAAwPnZ6BpTN8dGPg/ALXWKzAwcPxiAHMY1jcwMDD/hnL2GzAwsH6EcuQDgK5E5oCUgUxjASkDGQAGIANARoMA2GiQpSAAthTFOagOhXkBohXqOaihQs2hFoogBgDIkUU0B3LhTAAAAABJRU5ErkJggg==) no-repeat 50%;
          background-size: 12px
        }
        &::before {
          content: "";
          position: absolute;
          left: 0;
          top: 0;
          width: 15px;
          height: 15px;
          border-top-left-radius: 1px;
          border-bottom-left-radius: 1px;
        }
        &::after {
          content: "";
          display: block;
          border: 1px solid #fff;
          position: absolute;
          top: 0;
          left: 0;
          pointer-events: none;
          bottom: 0;
          right: 0;
          border-radius: 2px;
        }
      }
    }
    /*倒计时*/
    .count-down {
      position: absolute;
      right: 0;
      top: 0;
      min-width: 125px;
      height: 100%;
      text-align: center;
      background-color: #f8f8f8;
      .count-down-text {
        margin: 5px 0 2px;
        font-size: 10px;
        color: #666;
      }
      .count-down-times {
        font-size: 14px;
        color: $color-main;
      }
      .time {
        display: inline-block;
        margin: 0 1px;
        width: 23px;
        height: 21px;
        font-weight: 700;
        color: #fff;
        background-color: $color-main;
      }
      .day {
        font-size: 12px;
      }
    }
  }
</style>


