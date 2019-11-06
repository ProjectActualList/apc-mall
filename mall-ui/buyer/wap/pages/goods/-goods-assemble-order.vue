<template>
  <div v-if="assembleOrder.length" id="goods-assemble-order" class="goods-assemble-order">
    <van-cell-group class="comment-cells">
      <van-cell value="查看更多" is-link @click="showPopup = true">
        <div slot="title"><span class="rate">{{ assembleOrderAll.length }}人在拼团，可直接参与</span></div>
      </van-cell>
      <div class="some-comments">
        <van-swipe :autoplay="3000" loop :show-indicators="false" vertical class="assemble-swipe">
          <van-swipe-item v-for="(orderList, index) in assembleOrder"  :key="index">
            <div v-for="(order, _index) in orderList" :key="++_index">
              <div class="assemble-slide">
                <div class="assemble-person">
                  <!--团长头像-->
                  <img :src="order.face" alt="">
                  <!--团长昵称-->
                  <span>{{ order.name }}</span>
                </div>
                <div class="assemble-info">
                  <div>
                    <p>还差{{ order.required_num - order.offered_num }}人成团</p>
                  </div>
                  <van-button type="danger" @click="toBuyNow(order)" size="small">去拼团</van-button>
                </div>
              </div>
            </div>
          </van-swipe-item>
        </van-swipe>
      </div>
    </van-cell-group>
    <van-popup v-model="showPopup" position="bottom" style="height:100%">
      <van-nav-bar title="待成团列表" left-arrow @click-left="showPopup = false"/>
      <van-list class="comments-list">
        <div v-for="(order, _index) in assembleOrderAll" :key="++_index">
          <div class="assemble-slide">
            <div class="assemble-person">
              <!--团长头像-->
              <img :src="order.face" alt="">
              <!--团长昵称-->
              <span>{{ order.name }}</span>
            </div>
            <div class="assemble-info">
              <div>
                <p>还差{{ order.required_num - order.offered_num }}人成团</p>
              </div>
              <van-button type="danger" @click="toBuyNow(order)" size="small">去拼团</van-button>
            </div>
          </div>
        </div>
      </van-list>
    </van-popup>
  </div>
</template>

<script>
  import * as API_Promotions from '@/api/promotions'
  export default {
    name: "goods-assemble-order",
    props: ['goods-id', 'sku-id','is_assemble'],
    data() {
      return {
        /** 弹出 */
        showPopup: false,

        /** 加载loading */
        loading: false,

        /** 待成团订单 */
        assembleOrder: [],

        /** 查看更多待成团订单 */
        assembleOrderAll: []
      }
    },
    watch: {
      is_assemble(){
        if (this.goodsId, this.is_assemble) {
          this.getAssembleInfo()
        }
      }
    },
    mounted() {
      if (this.goodsId && this.is_assemble) {
        this.getAssembleInfo()
      }
    },
    methods: {
      /** 获取拼团信息 获取此商品所有待成团的订单*/
      getAssembleInfo() {
        API_Promotions.getAssembleOrderList(this.goodsId, { sku_id: this.skuId }).then(response => {
          const orderList = response.map(key => {
            return {
              left_time: key.left_time,
              offered_num: key.offered_num,
              required_num: key.required_num,
              face: key.participants.map(item => {
                if (item.is_master === 1) {
                  return item.face
                }
              })[0],
              order_id: key.order_id,
              name: key.participants.map(item => {
                if (item.is_master === 1) {
                  return item.name
                }
              })[0]
            }
          })
          this.assembleOrderAll = orderList
          if(orderList && orderList.length) {
            for(let i = 0,len = orderList.length; i < len; i +=2){
              this.assembleOrder.push(orderList.slice(i, i+2))
            }
          }
        })
      },

      /**去拼团 */
      toBuyNow(order) {
        this.$emit('to-assemble-buy-now', order)
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
  .assemble-swipe {
    width: 100%;
    height: 120px;
  }
  .assemble-slide {
    padding: 10px 0;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    & > .assemble-person {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-items: center;
      & > img {
        width: 35px;
	      height: 35px;
        border-radius: 50%;
        margin: 0 5px;
	      object-fit: cover;
      }
    }
    & > .assemble-info {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-items: center;
      & > div:first-child {
        margin-right: 10px;
        & > p:first-child {
          text-align: right;
        }
      }
    }
  }

</style>
