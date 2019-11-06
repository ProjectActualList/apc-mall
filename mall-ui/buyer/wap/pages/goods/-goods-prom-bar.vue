<template>
  <div class="prom-bar-container">
    <div class="prom-price">¥<em>{{ price | unitPrice('', 'before') }}</em>.{{ price | unitPrice('', 'after') }}</div>
    <div class="msg">
      <div class="text">
        <span></span>
        <del class="old-price">¥{{ oldPrice | unitPrice }}</del>
      </div>
      <div class="text">
        <span class="icon" :class="[type]">{{ title }}</span>
      </div>
    </div>
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
    name: 'goods-prom-bar',
    props: ['title', 'end-time', 'price', 'old-price', 'type'],
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
      this.contDown(this.endTime)
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
    .prom-price {
      margin: 0 10px;
      font-size: 14px;
      font-weight: 700;
      em {
        font-size: 24px;
      }
    }
    .msg {
      flex: 1;
      font-size: 12px;
      .icon {
        position: relative;
        display: inline-block;
        margin: -2px 3px 0 0;
        padding: 0 3px 0 18px;
        height: 15px;
        vertical-align: middle;
        line-height: 15px;
        font-size: 10px;
        &.seckill::before {
          background: #fff url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYBAMAAAASWSDLAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAwUExURUdwTNgqL+M4O+Q5O/8AAOQ5POM4O+Q4POQ5POQ4O+M4O+Q5OeQ5O+M5POQ4POQ5PF/0sUwAAAAPdFJOUwAGVuAC+ZDVrGlwKL3xQxKSapEAAACzSURBVBjTY2BgYOD9////BQYoQOEwMPwHYgFkjg2EJ9T8v0KRIf8giM1kD9TzWcHtB5DNkv99teCu+m8uOUAO29cpQNIzPgGkSj8OrPHpJyDBaD+BgecAAwPnZ6BpTN8dGPg/ALXWKzAwcPxiAHMY1jcwMDD/hnL2GzAwsH6EcuQDgK5E5oCUgUxjASkDGQAGIANARoMA2GiQpSAAthTFOagOhXkBohXqOaihQs2hFoogBgDIkUU0B3LhTAAAAABJRU5ErkJggg==) no-repeat 50%;
          background-size: 12px
        }
        &.groupbuy::before {
          background: #fff url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAACP0lEQVQ4T32TwWvUQBSHfy/JDEULolIt3U1Q0SZV9OBBW1EPCgVBb0pVUMSDip70JAiC+heIJ8ViRbyJgmyxVKngtoLgoQqSXbdSTbJ21YtiizaT5ElWt7bbbec0MO/7ZubNbwiLjGLWuJxodIlAsmEZ8xtaiHdNcQOgbmIccoLwbX2dm8FK0kW5oaAKEx1o+hV2rv2GSgq7WblZj8OofQJuTeaaYmqewLXELWLapydh14YygipsyiMEvs4Ej4Bp21O7CYjnCBiggiVupjCrcFdHBR9rOxVMkWfwtQ4/GnQt8VpnvtDuRy9mBFXYFH0g6kYYds2G/55A9AGYBCUDBK1XKrVt3QQ+VQX/YRwWidq0PsBYfcPGVmOVkvIeCFs08EXbU3f/iaeoYMqrTLwfjCIIPx1PnSaAF3vedK3QBhuGGCXXEhWEqtOpwC+a8iGDvzu+OrGYZLwFrb+bZB6E+2njRijBHTtQtxkwiqbMMfjLQpJSK1oiIV8SOOf46jyVLLkxAg9TnJxyyvGDd4DULfGEGUG9xM9ixZQmR8D83PHV2fQq1RyU2sTW2MAQODnu+PHj8TVomk5EPzO+Or46ml7nw3IsU80iD2C4Bs8I0sn7rNieaBhEwj1OEA1UJbF4ykDQzOrcpCaeEWPU8dXJ2Q2ek8SiZexkUH9N8rkNS37oYogIDoCc7alj9c2dF+VCxtgDnR5p4IM0Gb2KlopBIvJtL+whIKl/3oafqZgx9rJOV8DYAUKv7akzafYbZeMPqbAF8MaxdIEAAAAASUVORK5CYII=) no-repeat 50%;
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
    .count-down {
      position: absolute;
      right: 0;
      top: 0;
      min-width: 116px;
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
