<template>
  <div>
    <en-table-layout
      toolbar
      pagination
      :tableData="tableData"
      :loading="loading">
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
          <div class="conditions">
            <span>活动状态：</span>
            <el-select v-model="currentGroupBuyStatus" placeholder="请选择" @change="groupBuyStatusChange" clearable>
            <el-option
                v-for="item in groupBuyStatus"
                :key="item.group_buy_status_id"
                :label="item.group_buy_status_name"
                :value="item.group_buy_status_id">
              </el-option>
            </el-select>
          </div>
          <el-button type="primary" @click="handleAddGroupBuyGoods">新增团购商品</el-button>
        </div>
        <div class="toolbar-search">
          <en-table-search @search="searchEvent"/>
        </div>
      </div>
      <template slot="table-columns">
        <el-table-column label="商品图片" width="120">
          <template slot-scope="scope">
            <img :src="scope.row.img_url" class="goods-image"/>
          </template>
        </el-table-column>
        <el-table-column label="团购名称" >
          <template slot-scope="scope">
            <div>
              <a :href="`${MixinBuyerDomain}/goods/${scope.row.goods_id}`" target="_blank" style="color: #266fd4;">{{ scope.row.gb_name }}</a>
            </div>
            <div>{{ scope.row.gb_title }}</div>
          </template>
        </el-table-column>
        <el-table-column  label="活动信息" >
          <template slot-scope="scope">
            <!--活动名称-->
            <div>{{ scope.row.title }}</div>
            <!--活动开始时间----活动结束时间-->
            <div>{{ scope.row.start_time| unixToDate('yyyy-MM-dd') }}
               - {{ scope.row.end_time| unixToDate('yyyy-MM-dd') }}
            </div>
          </template>
        </el-table-column>
        <!--已团购数量-->
        <el-table-column prop="buy_num" label="已团购" width="120"/>
        <el-table-column prop="gb_status_text" label="活动状态" width="80"/>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button
              type="success"
              v-if="scope.row.examine_status !== 1"
              :disabled="scope.row.gb_status_text=='已结束'"
              @click="handleEditGroupGoods(scope.row)">编辑
            </el-button>
            <el-button
              type="danger"
              v-if="scope.row.examine_status !== 1"
              @click="handleDeleteGroupGoods(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </template>
      <el-pagination
        slot="pagination"
        v-if="pageData"
        @size-change="handlePageSizeChange"
        @current-change="handlePageCurrentChange"
        :current-page="pageData.page_no"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageData.page_size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageData.data_total">
      </el-pagination>
    </en-table-layout>
  </div>
</template>

<script>
  import * as API_groupBuy from '@/api/groupBuy'
  import { CategoryPicker } from '@/components'
  export default {
    name: 'groupBuyManager',
    components: {
      [CategoryPicker.name]: CategoryPicker
    },
    data() {
      return {
        /** 列表loading状态 */
        loading: false,

        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10
        },

        /** 列表数据 */
        tableData: [],

        /** 列表分页数据 */
        pageData: [],

        /** 当前团购状态*/
        currentGroupBuyStatus: '',

        /** 团购状态*/
        groupBuyStatus: [
          { group_buy_status_id: 0, group_buy_status_name: '未审核' },

          { group_buy_status_id: 1, group_buy_status_name: '已通过' },

          { group_buy_status_id: 2, group_buy_status_name: '审核失败' }

          // { group_buy_status_id: 3, group_buy_status_name: '已结束' }
        ]
      }
    },
    mounted() {
      this.GET_GroupGoodsList()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_GroupGoodsList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_GroupGoodsList()
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        delete this.params.keywords
        this.params = {
          ...this.params,
          keywords: data
        }
        this.GET_GroupGoodsList()
      },

      /** 团购状态发生改变*/
      groupBuyStatusChange(val) {
        delete this.params.status
        if (val >= 0) {
          this.params = {
            ...this.params,
            status: val
          }
        }
        this.GET_GroupGoodsList()
      },

      /** 获取团购商品列表 */
      GET_GroupGoodsList() {
        this.loading = true
        API_groupBuy.getGroupBuyGoodsList(this.params).then(response => {
          this.loading = false
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
          this.tableData = response.data
        })
      },

      /** 新增团购商品商品 */
      handleAddGroupBuyGoods() {
        this.$router.push({ name: 'groupBuyGoods', query: { group_name: '新增' }, params: { callback: this.GET_GroupGoodsList }})
      },

      /** 编辑团购商品 */
      handleEditGroupGoods(row) {
        this.$router.push({ name: 'groupBuyGoods', query: { group_name: '编辑' }, params: { goods_id: row.gb_id, callback: this.GET_GroupGoodsList }})
      },

      /** 删除团购商品 */
      handleDeleteGroupGoods(row) {
        this.$confirm('确认删除此团购商品, 是否继续?', '提示', { type: 'warning' }).then(() => {
          API_groupBuy.deleteGroupBuyGoods(row.gb_id, {}).then(() => {
            this.GET_GroupGoodsList()
            this.$message.success('删除团购商品成功！')
          })
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /deep/ div.toolbar {
    height: 70px;
    padding: 20px 0;
  }

  div.toolbar-btns {
    width: 100%;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: flex-start;
    align-items: center;
    div {
      span {
        display: inline-block;
        font-size: 14px;
        color: #606266;
      }
    }
    .conditions {
      margin-right: 30px;
    }
  }

  /deep/ .el-table td:not(.is-left) {
    text-align: center;
  }

  .inner-toolbar {
    display: flex;
    width: 100%;
    justify-content: space-between;
  }

  .toolbar-search {
    margin-right: 10px;
  }

  .goods-image {
    width: 50px;
    height: 50px;
  }

</style>
