<template>
  <van-popup id="payment" v-model="show" position="bottom" style="height:100%;background-color:#f7f7f7">
    <van-nav-bar title="支付配送" fixed @click-right="$emit('close')">
      <i class="iconfont ea-icon-close" slot="right"/>
    </van-nav-bar>
    <div class="payment-container">
      <div class="pay-way">
        <div class="pay-way-title">
          <div class="pay-way-pic">
            <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAABkklEQVRoQ+2Z603EQAyEv6sAqADoADqgA6ACoDOoAKiAEqAEqACoADSnTZQLyS6S95Ho7H/RZm3PzF7im2xYeWxW3j8OoLWCroArYGQgdoQugTNj/lzb34DnqWRTANT0I3CSq3qmPO/ANSAwfYwBqOlX4DBT0dxpvoBzQGC2MQZwD9yEtW9A19rUMkTmLXAQmngI15MAPgfsC+mOXA1RXAAvob4IPZpT4CdyvBr2vy092dv4CDmAgjLttwJ6vHZPp4Ik76TW06Z/XFp/A0p0XKvzUEc1Twc1TUeoBYCP0TRgAqAjpJdJzdBLNNsRqtn4XC2TAg4gAwOuQAYSTSlcARN9GTa7AhlINKVwBUz0ZdhsUmBunNasorG3RpgAxKbRu+BelAax3wDmxmkpo7G3RpgUqNFgqsa/AMg06hywpRpbcgx76zNmLQrMkqzFrumotaizLjuxUyEla+11sS/3fNbcVUO64amBC5EiQ3/yr1L2+jCJbl7SBw6R+if8I19K99LrrkBphlP5XYEUQ6XXV6/ALxC1YjFI+/KwAAAAAElFTkSuQmCC">
          </div>
          <h2>支付方式</h2>
        </div>
        <div class="pay-way-goods">
          <div class="pay-way-checkbox">
            <a
              href="javascript:;"
              class="eui-checkbox-btn"
              :class="[payment_type === 'ONLINE' && 'checked']"
              @click="payment_type = 'ONLINE'"
            >在线支付</a>
            <a
              href="javascript:;"
              class="eui-checkbox-btn"
              :class="[payment_type === 'COD' && 'checked']"
              @click="payment_type = 'COD'"
            >货到付款</a>
          </div>
          <p></p>
        </div>
      </div>
      <div class="time-way">
        <div class="pay-way-title">
          <div class="pay-way-pic">
            <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAADeElEQVRoQ+2Zj5HNQQzHcxWgAypABagAFaACVIAKUAEqOCpABehAB6iA+ZjNm/zydn+b3c3dzM1cZt7Mu3v7J9/km2w2eyIXXE4uuP5yCcB48I6I3C2fqyJyy3n3u4j8FpEv5fM1w/urHkDJpyLyQERQekQA81FE3ooI4KZkFgCKvy7WntrYTcIrz2eAzABA8WcVrf8Ui2JVrMtHLQtgPHS9gMZjVyprvClAwkYZAcDmpxVuw2U2RvERAQSGIHasAPqhiPyMLBYFgAU/O56j+MsSkJG9WmMIfNaxQPDevQilIgBqysNXrJ4peAN6qoRA9ABAm2/G8vD88QRdokCh1XsTH4C4vUenHgCU13yO8rh7OuUFUbAfWUmDnP0AUZU9AFCEHK9CYEUDFU6/EJFXhd9B3Q/D8AQJQ4Wzopb5mqUEVsD6KqOc/2vm9rzcAudjAi8ceb+1OBkHuiBkG/0etWQGAPaCSpqd+E5m2kgNgLc+k5g8IlkAMBzGVLnhA7oGgCzwaMH6TM0C4L3woWTBA6IagF8mbY4ErvVQJgAb0KTVa3YjD8C6jLQ5WmHq2pkAWBPFNa1uKO0BaPpj0pG7BoIgG4Cl9SY1ewA26mfpkx0DrMfp/64Y8FO5f/z/0wOwJ+9M9jkrCllqb05mD8C6vnpwBGmUTSGfTg967wGYPUHPgkLNNc8DQM9p0Xqp6tXzoFAPQC0W/ZwwhQiQm2X2ShDbdNwDEPGABfDDXmv30uiTcrnoKXAev4fTaNZBlg0qfJBZVx3VHdlaDaxn67PdUsLXHSun8YB+u0NtMXdUn/XK6eolIkuz4Dr2chUqpzMuNEHdusOmLjSs2r3KdbfOGdC92rbKhdVLfYb6S5d6FFhpq6wC8G2V5mHXK9jsyRxq9a1qXk5Z24fdnLx+/R4AWouA0OscIDihow2uUTxYnouLXmVJm9C52anuAUAB3+rjf/CTblmm0AW0DeNQKzMCoAWCTAU3R3tGtUqTNqRtnoWUZ6EoAMZCJ6ij1aoqAgAsx111RO4XT/quH5yHSqkPHFYxn530N320A5Buri+R2h60T0y1lk2ktN4YacQDdiJxARD/PDTiATsWoMTVcOt+FoBujkUpwVuPdnuA9FGQ+SG61BZbBWDX1EduvAMwHytwG0Wxsj52z3rsMC8TwLIyMwtcApixWuacfy0ryzHCT+kiAAAAAElFTkSuQmCC">
          </div>
          <h2>送货时间</h2>
        </div>
        <div class="pay-way-time">
          <a
            href="javascript:;"
            class="eui-checkbox-btn"
            :class="[receive_time === '任意时间' && 'checked']"
            @click="receive_time = '任意时间'"
          >任意时间</a>
          <a
            href="javascript:;"
            class="eui-checkbox-btn"
            :class="[receive_time === '仅工作日' && 'checked']"
            @click="receive_time = '仅工作日'"
          >仅工作日</a>
          <a
            href="javascript:;"
            class="eui-checkbox-btn"
            :class="[receive_time === '仅休息日' && 'checked']"
            @click="receive_time = '仅休息日'"
          >仅休息日</a>
        </div>
      </div>
    </div>
    <div class="big-btn">
      <van-button type="danger" @click="handleSavePayment">确认</van-button>
    </div>
  </van-popup>
</template>

<script>
  /**
   * 结算页
   * 支付类型组件
   */
  import * as API_Trade from '@/api/trade'
  export default {
    name: 'checkout-payment',
    props: ['show', 'payment-type', 'receive-time'],
    data() {
      return {
        payment_type: '',
        receive_time: ''
      }
    },
    watch: {
      show() {
        this.payment_type = (this.paymentType || 'ONLINE')
        this.receive_time = (this.receiveTime || '任意时间')
      }
    },
    methods: {
      handleSavePayment(type) {
        const { payment_type, receive_time } = this
        Promise.all([
          API_Trade.setPaymentType(payment_type),
          API_Trade.setReceiveTime(receive_time)
        ]).then(() => {
          this.$emit('changed', { payment_type, receive_time })
          this.$emit('close')
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .payment-container {
    padding-top: 46px;
  }
  .pay-way {
    padding-top: 12px;
    background-color: #fff;
  }
  .pay-way-title {
    font-size: 0;
    margin: 3px 0 15px 15px;
    h2 {
      display: inline-block;
      font-size: 14px;
      color: #686868;
    }
  }
  .pay-way-pic {
    display: inline-block;
    width: 20px;
    height: 20px;
    margin-right: 5px;
    vertical-align: top;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .pay-way-goods {
    padding-bottom: 15px;
    padding-left: 10px;
    margin-bottom: 15px;
  }
  .pay-way-checkbox {
    position: relative;
  }
  .time-way {
    padding-top: 12px;
    background-color: #fff;
    margin-top: 15px;
    margin-bottom: 13vw;
  }
  .pay-way-time {
    padding-bottom: 15px;
    padding-left: 10px;
  }
</style>
