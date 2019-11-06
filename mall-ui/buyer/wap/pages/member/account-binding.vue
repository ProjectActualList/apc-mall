<template>
  <div id="account-binding">
    <nav-bar title="账号绑定"/>
    <div class="account-binding-contaner">
      <van-cell-group>
        <van-cell is-link @click="handleClickCell('QQ')">
          <div slot="title">
            <img src="../../assets/images/icon-bind-qq.png">
            <span>QQ帐号</span>
          </div>
          <span :style="{color: bind['QQ'] ? '#67C23A' : '#ff6c00'}">{{ bind['QQ'] ? '已绑定' : '未绑定' }}</span>
        </van-cell>
        <van-cell is-link @click="handleClickCell('WECHAT')">
          <div slot="title">
            <img src="../../assets/images/icon-bind-weixin.png">
            <span>微信帐号</span>
          </div>
          <span :style="{color: bind['WECHAT'] ? '#67C23A' : '#ff6c00'}">{{ bind['WECHAT'] ? '已绑定' : '未绑定' }}</span>
        </van-cell>
        <van-cell is-link @click="handleClickCell('WEIBO')">
          <div slot="title">
            <img src="../../assets/images/icon-bind-sina.png">
            <span>微博帐号</span>
          </div>
          <span :style="{color: bind['WEIBO'] ? '#67C23A' : '#ff6c00'}">{{ bind['WEIBO'] ? '已绑定' : '未绑定' }}</span>
        </van-cell>
        <van-cell is-link @click="handleClickCell('ALIPAY')">
          <div slot="title">
            <img src="../../assets/images/icon-bind-alipay.png">
            <span>支付宝</span>
          </div>
          <span :style="{color: bind['ALIPAY'] ? '#67C23A' : '#ff6c00'}">{{ bind['ALIPAY'] ? '已绑定' : '未绑定' }}</span>
        </van-cell>
      </van-cell-group>
    </div>
    <van-actionsheet
      v-model="showAction"
      :actions="actions"
      cancel-text="取消"
      @select="onSelectAction"
    />
  </div>
</template>

<script>
  import Storage from '@/utils/storage'
  import * as API_Connect from '@/api/connect'
  export default {
    name: 'account-binding',
    head() {
      return {
        title: `账号绑定-${this.site.site_name}`
      }
    },
    data() {
      return {
        showAction: false,
        bind: '',
        actions: [
          { name: '绑定' },
          { name: '解绑' }
        ],
        cur_name: ''
      }
    },
    mounted() {
      this.GET_Connects()
    },
    methods: {
      getConnectUrl: API_Connect.getLogindConnectUrl,
      /** 点击cell */
      handleClickCell(name) {
        if (name === 'ALIPAY' && !this.MixinIsAliPayBrowser()) {
          this.$message.error('请在支付宝浏览器中进行操作！')
          return false
        }
        this.cur_name = name
        this.actions[0].disabled = this.bind[name]
        this.actions[1].disabled = !this.bind[name]
        this.showAction = true
      },
      /** 选择action */
      onSelectAction(item) {
        const { name } = item
        const { cur_name: type } = this
        this.showAction = false
        name === '绑定' ? this.bindConnect(type) : this.unbindConnect(type)
      },
      /** 解绑 */
      unbindConnect(type) {
        this.$confirm('确定要解绑吗？', () => {
          API_Connect.unbindConnect(type).then(() => {
            if (type === 'WECHAT') {
              Storage.removeItem('is_wechat_auth')
              Storage.removeItem('uuid_connect')
            }
            this.$message.success('解绑成功！')
            this.GET_Connects()
          })
        })
      },
      /** 发起绑定 */
      bindConnect(type) {
        API_Connect.getLogindConnectUrl(type).then(response => {
          location.href = response
        })
      },
      /** 获取绑定列表 */
      GET_Connects() {
        API_Connect.getConnectList().then(response => {
          const bind = {}
          response.forEach(item => {
            bind[item.union_type] = item.is_bind
          })
          this.bind = bind
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .account-binding-contaner {
    padding-top: 46px;
  }
  /deep/ {
    .van-cell__title {
      img {
        display: inline-block;
        vertical-align: middle;
        width: 30px;
        height: 30px;
      }
      span {
        margin-left: 10px;
      }
    }
  }
</style>
