<template>
  <div id="express" style="background-color: #f7f7f7">
    <nav-bar title="物流详情"></nav-bar>
    <div class="express-container">
      <div class="logistics">
        <div class="logi-logo">
          <i class="iconfont ea-icon-express"></i>
        </div>
        <div class="text">
          <p>物流公司：<span>{{ express.name || '获取中...'}}</span></p>
          <p>物流单号：<span>{{ express.courier_num || '获取中...' }}</span></p>
        </div>
      </div>
      <div class="logistics-info">
        <p v-if="!express" style="display:block; width: 100%; text-align: center; line-height: 100px;">物流信息获取中...</p>
        <ul v-else>
          <li v-for="(ship, index) in express.data" :key="index" class="ship-item">
            <i class="ship-dot"></i>
            <div class="ship-msg">
              <p class="ship-msg-text">{{ ship.context }}</p>
              <p class="ship-msg-time">{{ ship.time }}</p>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
  import * as API_Trade from '@/api/trade'
  export default {
    name: 'express',
    data() {
      return {
        express: ''
      }
    },
    mounted() {
      const { logi_id, ship_no } = this.$route.query
      API_Trade.getExpress(logi_id, ship_no).then(response => {
        this.express = response
      })
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  .express-container {
    padding-top: 46px;
    font-size: 14px;
    .logistics {
      display: flex;
      align-items: center;
      height: 70px;
      background-color: #fff;
    }
    .logi-logo {
      display: flex;
      justify-content: center;
      align-items: center;
      float: left;
      width: 90px;
      height: 70px;
      .iconfont {
        font-size: 65px;
      }
    }
    .logistics-info {
      clear: both;
      margin-top: 10px;
      background-color: #fff;
      padding: 0 10px;
      .ship-item {
        position: relative;
        display: flex;
        flex-wrap: wrap;
        &:first-child {
          .ship-dot {
            background-color: $color-main;
          }
          p {
            color: $color-main;
          }
        }
        &:not(:last-child)::after {
          content: "";
          position: absolute;
          z-index: 1;
          pointer-events: none;
          background-color: #e5e5e5;
          width: 1px;
          top: 37px;
          left: 10px;
          bottom: -20px;
        }
      }
      .ship-dot {
        position: relative;
        line-height: 20px;
        font-size: 10px;
        text-align: center;
        color: #666;
        margin: 24px 15px 0 5px;
        width: 10px;
        height: 10px;
        border-radius: 5px;
        background-color: #ccc;
      }
      .ship-msg {
        flex: 1;
        padding: 15px 0;
      }
      .ship-msg-text {
        color: #333;
        font-size: 14px;
        line-height: 18px;
      }
      .ship-msg-time {
        font-size: 12px;
        color: #999;
      }
    }
  }
</style>
