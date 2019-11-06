<template>
  <van-popup v-model="show" position="bottom" style="height:100%; background-color: #f1f1f1;">
    <van-nav-bar title="推荐商品" @click-right="$emit('close')">
      <i class="iconfont ea-icon-close" slot="right"/>
    </van-nav-bar>
    <div class="distribution-container">
      <div class="share-box-code">
        <div class="goods-info">
          <img :src="thumbnail" alt="">
          <h2>{{ goods_name }}</h2>
        </div>
        <qrcode-vue
          :value="config.value"
          :size="config.size"
          level="H"
          style="text-align: center; padding: 10px 0;">
        </qrcode-vue>
        <p>分享二维码，好友注册立得佣金</p>
      </div>
      <div class="share-box-btn">
        <van-cell-group>
          <van-field
            v-model="config.value"
            center
            :border="false"
            clearable
            label="分享链接"
            placeholder="我的分享链接">
            <van-button
              slot="button"
              size="small"
              type="primary"
              v-clipboard:copy="config.value"
              v-clipboard:success="onCopy"
              v-clipboard:error="onError">复制网址</van-button>
          </van-field>
        </van-cell-group>
      </div>
    </div>
  </van-popup>
</template>

<script>
  /**
   * 分销组件
   */
  import Vue from 'vue'
  import VueClipboard from 'vue-clipboard2'
  import QrcodeVue from 'qrcode.vue'

  Vue.use(VueClipboard)
  import * as API_distribution from '@/api/distribution'
  import { domain } from '@/ui-domain'
  export default {
    name: 'goods-distribution',
    props: ['show', 'goods_name', 'thumbnail'],
    components:{ QrcodeVue },
    data() {
      return {
        /** 是否显示二维码弹框 */
        isShowQRCode: false,

        config: {
          value: '',
          size: 170
        }
      }
    },
    watch: {
      show: 'showQRCode'
    },
    methods: {
      /** 显示并生成二维码 */
      showQRCode() {
        if (!this.show) return
        /** 登录校验 */
        const { user } = this.$store.getters
        // 如果没有登录，跳转到登录页
        if (!user) {
          this.$router.push(`/login?forward=${this.$route.fullPath}`)
        } else {
          API_distribution.generateShortLink({ goods_id: this.$route.params.id}).then(response => {
            this.isShowQRCode = true
            const _query = response.message.substring(response.message.indexOf("?"), response.message.length)
            this.config.value = `${domain.buyer_wap}/goods/${this.$route.params.id}${_query}`
          })
        }
    },

      /** 复制成功 */
      onCopy(e) {
        this.$message.success('已复制')
      },

      /** 复制失败 */
      onError(e) {
        this.$message.error('抱歉，您的由于您的浏览器不支持，请手动复制。')
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  div.distribution-container {
    padding-top: 70px;
  }
  /deep/ .van-cell, .van-button {
    font-size: 12px;
  }
  /deep/ input.van-field__control {
    font-size: 12px;
  }
  .goods-info {
    text-align: center;
    & > img {
      display: inline-block;
      width: calc(100% - 150px);
    }
    & > h2 {
      padding: 20px 10px;
      font-size: 15px;
      color: #333;
      font-weight: normal;
    }
  }
  .share-box-code {
    width: 80%;
    margin: 0 auto;
    padding: 40px 0px;
    border-radius: 6px;
    background-color: white;
    p {
      text-align: center;
    }
  }
  .share-box-btn {
    margin: 30px auto;
  }
</style>
