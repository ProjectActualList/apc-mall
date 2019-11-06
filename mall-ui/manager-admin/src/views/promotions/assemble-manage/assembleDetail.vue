<template>
    <div class="assemble-container">
      <p>拼团活动名称：{{ assembleForm.promotion_name }}</p>
      <div>
        <ul>
          <li>
            <span>活动状态</span><span >{{ assembleForm.status | assembleStatusText }}</span>
          </li>
          <li>
            <span>店铺名称</span><span>{{ assembleForm.seller_name }}</span>
          </li>
          <li>
            <span>开始时间</span><span>{{ assembleForm.start_time | unixToDate }}</span>
          </li>
          <li>
            <span>结束时间</span><span>{{ assembleForm.end_time | unixToDate }}</span>
          </li>
          <li>
            <span>成团人数</span><span>{{ assembleForm.required_num }}</span>
          </li>
          <li>
            <span>限购数量</span><span>{{ assembleForm.limit_num }}</span>
          </li>
        </ul>
      </div>
      <en-table-layout
        :tableData="tableData.data"
        :loading="loading"
      >
        <template slot="table-columns">
          <el-table-column prop="goods_name" label="商品名称"/>
          <el-table-column label="商品图片" width="300">
            <template slot-scope="scope">
              <img :src="scope.row.thumbnail" class="goods-image"  alt="">
            </template>
          </el-table-column>
          <el-table-column label="商品SKU" width="300">
            <template slot-scope="scope">
              <div class="goods-info">
                <div class="sku-list" v-for="spec in getSkuList(scope.row.specs)">
                  <span>{{ spec.name }}</span>:<span>{{ spec.value }}</span>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="250">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                @click="handleViewLink(scope.$index, scope.row)"
              >商品链接地址</el-button>
              <el-button
                size="mini"
                type="danger"
                @click="handleViewQRCode(scope.row)"
              >查看二维码</el-button>
            </template>
          </el-table-column>
        </template>
        <el-pagination
          v-if="tableData"
          slot="pagination"
          @size-change="handlePageSizeChange"
          @current-change="handlePageCurrentChange"
          :current-page="tableData.page_no"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="tableData.page_size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="tableData.data_total">
        </el-pagination>
      </en-table-layout>
      <!--编辑商品链接-->
      <el-dialog title="商品链接地址" :visible.sync="isShowGoodsLink" width="25%" align="center">
        <el-input v-model="currentLink"></el-input>
        <div slot="footer" class="dialog-footer">
          <el-button @click="isShowGoodsLink = false">关 闭</el-button>
        </div>
      </el-dialog>
      <!--商品二维码-->
      <el-dialog title="商品二维码" :visible.sync="isShowGoodsQRcode" width="15%" align="center">
        <qrcode-vue
          :value="config.value"
          :size="config.size"
          level="H"
          style="text-align: center; padding: 10px 0;">
        </qrcode-vue>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="isShowGoodsQRcode = false">关闭</el-button>
        </div>
      </el-dialog>
    </div>
</template>
<script>
  import * as API_Promotion from '@/api/promotion'
  import QrcodeVue from 'qrcode.vue'
  export default {
    name: 'assembleDetail',
    data() {
      return {
        /** 活动信息 */
        assembleForm: {},

        params: {
          page_no: 1,
          page_size: 10
        },

        /** 活动商品信息 */
        tableData: [],

        pagination: null,

        /** 加载中。。。 */
        loading: false,

        /** 是否显示商品链接 */
        isShowGoodsLink: false,

        /** 是否显示二维码弹框 */
        isShowGoodsQRcode: false,

        /** 当前显示的链接 */
        currentLink: '',

        /** 二维码配置 */
        config: {
          value: '',
          size: 170
        }
      }
    },
    mounted() {
      // 获取活动信息&商品信息
      this.GET_AssembleDetail()
      // 获取拼团商品列表
      this.GET_AssembleGoodsList()
    },
    activated() {
      this.GET_AssembleDetail()
      // 获取拼团商品列表
      this.GET_AssembleGoodsList()
    },
    components: { QrcodeVue },
    filters: {
      /** 状态文本显示 */
      assembleStatusText(status) {
        switch (status) {
          case 'WAIT': return '待开始'
          case 'UNDERWAY': return '进行中'
          case 'END': return '已结束'
        }
      }
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_AssembleDetail()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_AssembleDetail()
      },

      /** 获取拼团活动详情 */
      GET_AssembleDetail() {
        API_Promotion.getAssembleDetail(this.$route.params.promotion_id, this.params).then(response => {
          this.assembleForm = { ...response }
        })
      },

      /** 获取拼团商品列表 */
      GET_AssembleGoodsList() {
        API_Promotion.getAssembleGoodsList(this.$route.params.promotion_id).then(response => {
          this.tableData = response
        })
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

      /** 查看商品链接 */
      handleViewLink($index, row) {
        this.currentLink = `${this.MixinBuyerWapDomain}/goods/${row.goods_id}?sku_id=${row.sku_id}&from_nav=assemble`
        this.isShowGoodsLink = true
      },

      /** 展示二维码 */
      handleViewQRCode(row) {
        this.isShowGoodsQRcode = true
        this.config.value = `${this.MixinBuyerWapDomain}/goods/${row.goods_id}?sku_id=${row.sku_id}&from_nav=assemble`
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .assemble-container {
    /*拼团活动信息*/
    & > p {
      font-size: 15px;
      font-weight: bold;
      margin: 0;
      padding: 15px 0 15px 20px;
      color: #333;
      background-color: #f5f7fa;
      border-bottom: 1px solid #e1e8ed;
    }
    & > div {
      padding: 0 0 10px 0;
      ul {
        margin: 0;
        padding: 0;
        background-color: #fff;
        li {
          list-style: none;
          width: 100%;
          vertical-align: top;
          -webkit-box-sizing: border-box;
          box-sizing: border-box;
          padding: 10px 0 10px 20px;
          line-height: 24px;
          white-space: nowrap;
          & > span:first-child {
            margin-right: 20px;
            font-size: 14px;
            color: #999;
          }
          & > span:last-child {
            white-space: normal;
            word-break: break-all;
            font-size: 14px;
            color: #666;
          }
        }
        li:first-child {
          span:last-child {
            color: #ff7349;
          }
        }
      }
    }
    /*拼团商品信息*/
    .goods-image {
      width: 75px;
      height: 75px;
    }
    /** 商品表格 */
    /deep/ thead>tr {
      /deep/ th:nth-child(3) {
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
      .sku-list {
        display: inline-block;
        margin-right: 15px;
        color: #9cb945;
      }
    }
  }
</style>
