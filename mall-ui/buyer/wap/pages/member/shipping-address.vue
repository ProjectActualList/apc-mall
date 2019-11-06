<template>
  <div id="shipping-address">
    <van-nav-bar
      left-arrow
      @click-left="MixinRouterBack"
      right-text="添加"
      @click-right="handleAddAddress"
      title="收货地址"
    />
    <div class="address-container">
      <empty-member v-if="finished && !addressList.length">暂无收货地址</empty-member>
      <ul class="address-list">
        <li
          class="address-item"
          v-for="(address, index) in addressList"
          :key="index"
          @click="handleSelectAddress(address)"
        >
          <van-swipe-cell :right-width="65">
            <div class="address-content">
              <div class="info-address">
                <div class="add-name">{{ address.name }}</div>
                <div class="add-mobile">{{ address.mobile | secrecyMobile }}</div>
              </div>
              <div class="address-detail">
                <span v-if="address.def_addr === 1" class="add-def-icon">默认</span>
                {{ address.province }} {{ address.city }} {{ address.county }} {{ address.town }} {{ address.addr }}
                <span v-if="address.ship_address_name" class="add-alias">{{ address.ship_address_name }}</span>
              </div>
            </div>
            <i class="iconfont ea-icon-edit" @click.stop="handleEaitAddress(address)"></i>
            <span slot="right" @click.stop="handleDeleteAddress(address)">删除</span>
          </van-swipe-cell>
        </li>
      </ul>
    </div>
    <van-popup
      v-model="showEditDialog"
      position="bottom"
      :close-on-click-overlay="false"
      class="edit-dialog"
    >
      <van-nav-bar
        :title="(addressForm.addr_id ? '编辑' : '添加') + '收货地址'"
        left-arrow
        @click-left="showEditDialog = false"
        style="position: relative"
      />
      <van-cell-group>
        <van-field v-model="addressForm.name" label="收货人" clearable placeholder="请填写收货人"/>
        <van-field v-model="addressForm.mobile" label="手机号码" clearable placeholder="请填写手机号码" :maxlength="11"/>
        <van-field v-model="addressForm.addrs" label="所在地区" readonly icon="arrow" placeholder="请选择所在地区" @click.native="showAddressSelector = true"/>
        <van-field v-model="addressForm.addr" label="详细地址" clearable placeholder="请填写详细地址"/>
        <van-field v-model="addressForm.ship_address_name" label="地址别名" clearable placeholder="请填写地址别名，如：公司" :maxlength="4"/>
        <van-switch-cell v-model="addressForm.def_addr" title="默认地址" />
        <div class="big-btn">
          <van-button size="large" @click="submitAddressForm">保&nbsp;存</van-button>
        </div>
        <div class="cancel-btn">
          <van-button size="large" @click="showEditDialog = false">取&nbsp;消</van-button>
        </div>
      </van-cell-group>
    </van-popup>
    <en-region-picker
      :show="showAddressSelector"
      :api="MixinRegionApi"
      @closed="showAddressSelector = false"
      @changed="handleAddressSelectorChanged"
    />
  </div>
</template>

<script>
  import * as API_Address from '@/api/address'
  import * as API_Trade from '@/api/trade'
  import { Foundation, RegExp } from '@/ui-utils'
  export default {
    name: 'shipping-address',
    head() {
      return {
        title: `我的收货地址-${this.site.site_name}`
      }
    },
    data() {
      return {
        loading: false,
        finished: false,
        addressList: [],
        addressForm: {},
        showEditDialog: false,
        showAddressSelector: false
      }
    },
    mounted() {
      this.GET_AddressList()
    },
    methods: {
      /** 地址选择器发生改变 */
      handleAddressSelectorChanged(object) {
        this.addressForm.region = object.last_id
        this.addressForm.addrs = object.string
      },
      /** 提交修改地址表单 */
      submitAddressForm() {
        const params = JSON.parse(JSON.stringify(this.addressForm))
        if (!params.name) {
          this.$message.error('请填写收货人姓名！')
        } else if (!params.mobile) {
          this.$message.error('请填写手机号码！')
        } else if (!RegExp.mobile.test(params.mobile)) {
          this.$message.error('手机号码格式有误！')
        } else if (!params.region) {
          this.$message.error('请选择收货地区！')
        } else if (!params.addr) {
          this.$message.error('请填写收货详细地址！')
        } else {
          const { addr_id } = params
          params.def_addr = params.def_addr ? 1 : 0
          if (addr_id) {
            API_Address.editAddress(addr_id, params).then(() => {
              this.showEditDialog = false
              this.finished = false
              this.GET_AddressList()
            })
          } else {
            API_Address.addAddress(params).then(() => {
              this.showEditDialog = false
              this.finished = false
              this.GET_AddressList()
            })
          }
        }
      },
      /** 添加地址 */
      handleAddAddress() {
        this.addressForm = {
          def_addr: false,
          ship_address_name: '',
          mobile: '',
          name: '',
          addrs: '',
          region: ''
        }
        this.showEditDialog = true
      },
      /** 编辑地址 */
      handleEaitAddress(address) {
        const params = JSON.parse(JSON.stringify(address))
        params.def_addr = !!params.def_addr
        params.addrs = `${params.province} ${params.city} ${params.county} ${params.town}`
        params.region = params.county_id || params.town_id
        this.addressForm = params
        this.showEditDialog = true
      },
      /** 删除地址 */
      handleDeleteAddress(address) {
        this.$confirm('确定要删除这个地址吗？', () => {
          API_Address.deleteAddress(address.addr_id).then(this.GET_AddressList)
        })
      },
      /** 选择地址 */
      handleSelectAddress(address) {
        if (this.$route.query.from === 'checkout') {
          API_Trade.setAddressId(address.addr_id).then(() => {
            this.MixinRouterBack()
          })
        }
      },
      /** 获取地址列表 */
      GET_AddressList() {
        API_Address.getAddressList().then(response => {
          this.addressList = response
          this.finished = true
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .address-container {
    padding-top: 46px;
  }
  .address-list {
    margin-left: 10px;
  }
  .address-item {
    display: flex;
    position: relative;
  }
  .address-content {
    position: relative;
    float: left;
    width: calc(100% - 50px);
    padding-top: 10px;
    padding-bottom: 10px;
    border-bottom: 1px solid #f1f1f1;
    .add-name, .add-mobile {
      display: inline-block;
      font-size: 14px;
      line-height: 15px;
      margin-right: 15px;
      overflow: hidden;
    }
    .address-detail {
      color: #666;
      font-size: 13px;
      line-height: 20px;
      word-break: break-all;
      word-wrap: break-word;
    }
    .add-def-icon {
      padding: 1px 7px;
      background: #ff002d;
      color: #fff;
      font-size: 12px;
      font-style: normal;
      margin-right: 5px;
    }
    .add-alias {
      display: inline-block;
      height: 15px;
      font-size: 12px;
      border: 1px solid #686868;
      line-height: 15px;
      text-align: center;
      padding: 0 10px;
    }
  }
  .address-place {
    float: right;
    width: 50px;
    height: 100%;
  }
  .ea-icon-edit {
    position: absolute;
    top: 50%;
    right: 0;
    height: 36px;
    line-height: 36px;
    margin-top: -18px;
    border-left: 1px solid #cbcacf;
    font-size: 24px;
    padding-left: 10px;
    padding-right: 10px;
  }
  /deep/ {
    .van-swipe-cell__wrapper {
      height: 100%;
    }
    .van-swipe-cell__wrapper, .van-swipe-cell {
      width: 100%;
    }
    .edit-dialog {
      height: 100%;
    }
  }
  .cancel-btn {
    padding: 10px 10px 0 10px;
  }
</style>
