<template>
  <div class="sign-up-bg" v-loading="loading">
    <el-form
      :model="activityData"
      v-if="activityData"
      label-position="right"
      ref="activityData"
      label-width="120px"
      class="demo-ruleForm">
      <!--活动名称-->
      <el-form-item  label="活动名称:">
        {{ activityBaseInfo.promotion_name }}
      </el-form-item>
      <!--开始时间-->
      <el-form-item label="开始时间:">
        {{ activityBaseInfo.start_time | unixToDate }}
      </el-form-item>
      <!--结束时间-->
      <el-form-item label="结束时间:">
        {{ activityBaseInfo.end_time | unixToDate }}
      </el-form-item>
      <!--抢购阶段-->
      <el-form-item label="活动商品:">
        <en-table-layout
          toolbar
          pagination
          :tableData="activityData.goods_data"
          :loading="loading"
          :selectionChange="selectionChange">
          <div slot="toolbar" class="inner-toolbar">
            <div class="toolbar-btns">
              <el-button type="primary" @click="selectgoodslist" >选择商品</el-button>
              <el-button type="danger" @click="cancelall">批量取消</el-button>
            </div>
          </div>
          <template slot="table-columns">
            <el-table-column type="selection"/>
            <el-table-column label="商品信息(SKU)" width="800" class="goods-sku-header">
              <template slot-scope="scope">
                <div class="goods-info">
                  <img :src="scope.row.thumbnail" alt="商品图片">
                  <div>
                    <p> {{ scope.row.goods_name }} </p>
                    <div class="sku-list" v-for="spec in getSkuList(scope.row.specs)">
                      <span>{{ spec.name }}</span>:<span>{{ spec.value }}</span>
                    </div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="原价">
              <template slot-scope="scope">{{ scope.row.price | unitPrice('￥') }}</template>
            </el-table-column>
            <el-table-column label="拼团价" width="180">
              <template slot-scope="scope">
                <el-input v-model="scope.row.sales_price" @blur="intMoney(scope.row)" clearable>
                  <template slot="prepend">¥</template>
                </el-input>
              </template>
            </el-table-column>
            <el-table-column label="库存(件)">
              <template slot-scope="scope">{{ scope.row.enable_quantity }}</template>
            </el-table-column>
            <el-table-column label="操作" width="300">
              <template slot-scope="scope">
                <el-button v-if="canLookQrcode" type="primary" @click="handleQRcode(scope.row)">查看二维码</el-button>
                <el-button type="danger" @click="canceljoin(scope)">取消参加 </el-button>
              </template>
            </el-table-column>
          </template>
        </en-table-layout>
      </el-form-item>
    </el-form>
    <div style="text-align: center;">
      <el-button type="primary" @click="handleSaveAssembleGoods">保存设置</el-button>
    </div>
    <en-goods-sku-picker
      type="seller"
      goods-type="NORMAL"
      :show="showDialog"
      :sku="true"
      :api="goodsApi"
      :multipleApi="multipleApi"
      :defaultData="skuIds"
      :categoryApi="categoryApi"
      :limit="maxsize"
      @confirm="refreshFunc"
      @close="showDialog = false"/>
    <el-dialog
      title="分享链接"
      :visible.sync="dialogShare"
      width="30%">
      <div>
        <el-input placeholder="请输入内容" v-model="config.value">
          <template slot="append">
            <el-button
              size="small"
              type="primary"
              v-clipboard:copy="config.value"
              v-clipboard:success="onCopy"
              v-clipboard:error="onError">复制</el-button>
          </template>
        </el-input>
      </div>
      <div class="QR-code">
        <qrcode-vue
          :value="config.value"
          :size="config.size"
          level="H"
          style="text-align: center; padding: 10px 0;">
        </qrcode-vue>
        <p style="padding: 0 15px;">提示: 使用微信扫一扫,直达微信多人拼团活动商品页面</p>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_activity from '@/api/activity'
  import { CategoryPicker, GoodsSkuPicker } from '@/components'
  import { RegExp } from '~/ui-utils'
  import { cloneObj } from '@/utils/index'
  import QrcodeVue from 'qrcode.vue'
  import VueClipboard from 'vue-clipboard2'
  Vue.use(VueClipboard)
  export default {
    name: 'assembleGoods',
    components: {
      [CategoryPicker.name]: CategoryPicker,
      [GoodsSkuPicker.name]: GoodsSkuPicker,
      QrcodeVue
    },
    data() {
      return {
        /** loading状态*/
        loading: false,

        /** 活动信息*/
        activityData: {
          goods_data: []
        },

        /** 活动基本信息 */
        activityBaseInfo: {
          /** 活动名称 */
          promotion_name: '',

          start_time: '',

          end_time: ''
        },

        /** 活动ID*/
        seckillID: '',

        /** 表格信息*/
        tableData: [],

        /** 商品skuIds */
        skuIds: [],

        /** 选中的商品sku_id */
        selectionids: [],

        /** 当前操作的表格序列*/
        _order: 0,

        /** 商品选择器最大长度*/
        maxsize: 0,

        /** 商品选择器列表api*/
        goodsApi: 'seller/goods/skus?market_enable=1&is_auth=1',

        multipleApi: 'seller/goods/skus/@ids/details',

        /** 商城分类api */
        categoryApi: 'seller/goods/category/0/children',

        /** 显示/隐藏商品选择器 */
        showDialog: false,

        /** 是否显示当前分享弹框 */
        dialogShare: false,

        /** 二维码配置文件 */
        config: {
          value: '',
          size: 170
        }
      }
    },
    mounted() {
      this.activityID = this.$route.params.promotion_id
      this.GET_assembleGoodsDetails()
      this.activityBaseInfo = { ...this.$route.query }
    },
    beforeRouteUpdate(to, from, next) {
      this.activityID = to.params.promotion_id
      this.activityBaseInfo = { ...to.query }
      this.GET_assembleGoodsDetails()
      next()
    },
    computed: {
      canLookQrcode() {
        const current = parseInt(new Date().getTime() / 1000)
        return current >= parseInt(this.activityBaseInfo.start_time)
      }
    },
    methods: {
      /** 获取活动商品详情 */
      GET_assembleGoodsDetails() {
        API_activity.getAssembleGoods(this.activityID).then(response => {
          this.loading = false
          this.activityData.goods_data = response
          this.skuIds = this.activityData.goods_data.map(key => key.sku_id)
        })
      },

      /** 选中商品触发 */
      selectionChange(val) {
        this.selectionids = val.map(item => item.sku_id)
      },

      /**  取消参加 */
      canceljoin(scope) {
        this.activityData.goods_data.forEach((elem, index) => {
          if (elem.sku_id === scope.row.sku_id) {
            this.activityData.goods_data.splice(index, 1)
          }
        })
        this.skuIds = this.activityData.goods_data.map(key => key.sku_id)
      },

      /** 批量取消 */
      cancelall() {
        if (this.selectionids.length <= 0) {
          this.$message.error('请选择要取消的商品')
          return
        }
        this.selectionids.forEach(key => {
          this.activityData.goods_data.forEach((elem, index) => {
            if (elem.sku_id === key) {
              this.activityData.goods_data.splice(index, 1)
            }
          })
        })
        this.skuIds = this.activityData.goods_data.map(key => key.sku_id)
      },

      /** 查看二维码 */
      handleQRcode(row) {
        this.dialogShare = true
        this.config.value = `${this.MixinBuyerWapDomain}/goods/${row.goods_id}?sku_id=${row.sku_id}&from_nav=assemble`
      },

      /** 复制成功 */
      onCopy(e) {
        this.$message.success('已复制')
      },

      /** 复制失败 */
      onError(e) {
        this.$message.error('抱歉，您的由于您的浏览器不支持，请手动复制。')
      },

      /** 删除已选中的商品*/
      handleDelgoods($index, row) {
        this.activityData.goods_data.forEach((elem, index) => {
          if (row.sku_id === elem.sku_id) {
            this.activityData.goods_data.splice(index, 1)
          }
        })
      },

      /**  显示商品选择器*/
      selectgoodslist($index) {
        this.showDialog = true
        this._order = $index
      },

      intMoney(row) {
        if (!RegExp.money.test(row.sales_price)) {
          row.sales_price = 0.00
        } else {
          row.sales_price = Number.parseFloat(row.sales_price).toFixed(2)
        }
      },

      /** 获取规格列表 */
      getSkuList(val) {
        const _val = typeof val === 'string' ? JSON.parse(val) : val
        if (_val) {
          const _specs = _val.map(key => {
            return {
              name: key.spec_name,
              value: key.spec_value
            }
          })
          return _specs
        }
        return []
      },

      /** 保存商品选择器选择的商品 */
      refreshFunc(val) {
        const _val = val
        this.activityData.goods_data.forEach(key => {
          _val.forEach(item => {
            if (item.sku_id === key.sku_id) {
              item.sales_price = key.sales_price
            }
          })
        })
        this.activityData.goods_data = cloneObj(_val)
        this.skuIds = this.activityData.goods_data.map(key => key.sku_id)
      },

      /** 保存拼团活动商品详情信息 */
      handleSaveAssembleGoods() {
        if (!this.activityData.goods_data || !Array.isArray(this.activityData.goods_data) || !this.activityData.goods_data.length) {
          this.$message.error('请选择要参加拼团的商品')
          return
        } else if (this.activityData.goods_data.some(key => { return !key.sales_price && parseInt(key.sales_price) !== 0 })) {
          this.$message.error('请填写拼团价格')
          return
        }
        const _params = this.activityData.goods_data.map(key => {
          return {
            sku_id: key.sku_id,
            sales_price: key.sales_price
          }
        })
        API_activity.saveAssembleGoods(this.activityID, _params).then(() => {
          this.$message.success('保存成功')
          this.$router.push({ path: '/promotions/assembleManager' })
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /*背景颜色*/
  .sign-up-bg {
    background: #fff;
    border: 1px solid #ddd;
    padding: 15px;
  }
  /** 商品表格 */
  /deep/ thead>tr {
    /deep/ th:nth-child(2) {
      text-align: left !important;
    }
  }
  .goods-info {
    text-align: left;
    display: flex;
    flex-direction: row;
    img {
      margin-right: 10px;
      width: 75px;
      height: 75px;
    }
    p:last-child {
      color: #9cb945;
    }
    .sku-list {
      display: inline-block;
      margin-right: 15px;
      color: #9cb945;
    }
  }
</style>
