<template>
  <div>
    <en-table-layout
      toolbar
      pagination
      :tableData="tableData"
      :loading="loading">
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
          <el-button @click="showaddShopNav" type="primary">新增</el-button>
        </div>
      </div>
      <template slot="table-columns">
        <el-table-column prop="sort" label="排序"/>
        <el-table-column prop="name" label="导航名称"/>
        <el-table-column label="是否显示">
          <template slot-scope="scope">
            <span v-if="scope.row.disable === 1">已显示</span>
            <span v-if="scope.row.disable === 0">未显示</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              type="success"
              @click="handleEditShopNav(scope.row)">编辑
            </el-button>
            <el-button
              type="danger"
              @click="handleDelShopNav(scope.row)">删除
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
    <el-dialog title="新增导航" :visible.sync="addShopNavshow"  width="35%">
      <el-form :model="navform" :rules="rules" ref="navform" label-position="right" status-icon label-width="120px">
        <el-form-item label="导航名称:" prop="name">
          <el-input v-model="navform.name" @change="()=> { navform.name = navform.name.trim() }" />
        </el-form-item>
        <el-form-item label="是否显示:" prop="disable">
          <el-radio-group v-model="navform.disable">
            <el-radio :label="1">显示</el-radio>
            <el-radio :label="0">不显示</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序:" prop="sort">
          <el-input-number v-model="navform.sort" controls-position="right"  :min="0" :max="99999"/>
        </el-form-item>
        <el-form-item label="URL:" prop="nav_url">
          <el-input placeholder="请输入URL" @change="() => { navform.nav_url = navform.nav_url.trim() }" v-model="navform.nav_url"></el-input>
          <span class="URLtip">请填写包含http://的完整URL地址，否则会跳转到外链，长度最多255个字符</span>
          <span class="URLtip">例: http://www.baidu.com填写http://www.baidu.com</span>
        </el-form-item>
        <el-form-item label="新窗口打开:" prop="target">
          <el-radio-group v-model="navform.target">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addShopNavshow = false">取 消</el-button>
        <el-button type="primary" @click="POST_AddShopNav('navform')">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import * as API_ShopNav from '@/api/shopNav'
  import { RegExp } from '~/ui-utils'
  export default {
    name: 'shopNav',
    data() {
      const shopNavURL = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入链接地址'))
        } else if (!RegExp.URL.test(value)) {
          callback(new Error('请输入正确的完整链接地址'))
        } else {
          callback()
        }
      }
      const shopNavSort = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入排序'))
        } else if (!RegExp.integer.test(value) && value === 0) {
          callback(new Error('请输入正整数'))
        } else {
          callback()
        }
      }
      return {
        /** 列表loading状态 */
        loading: false,

        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10
        },

        /** 列表数据*/
        tableData: [],

        /** 列表分页数据 */
        pageData: [],

        /** 新增导航表单*/
        navform: {
          name: '',
          disable: 1,
          sort: '',
          nav_url: '',
          target: 1
        },

        /** 是否显示导航表单*/
        addShopNavshow: false,

        /** 是否是编辑 1编辑 2新增 */
        isEdit: 1,

        /** 当前主键 */
        navId: '',

        /** 校验规则 */
        rules: {
          name: [
            { required: true, message: '请填写导航名称', trigger: 'blur' },
            { max: 15, message: '名称长度最多为15个字符', trigger: 'blur' },
            { whitespace: true, message: '导航名称不可为纯空格', trigger: 'blur' }
          ],
          sort: [
            { validator: shopNavSort, trigger: 'blur' }
          ],
          nav_url: [
            { max: 255, message: 'url长度最多为255个字符', trigger: 'blur' },
            { validator: shopNavURL, trigger: 'blur' }
          ]
        }
      }
    },
    mounted() {
      this.GET_ShopNavList()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_ShopNavList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_ShopNavList()
      },

      GET_ShopNavList() {
        this.loading = true
        API_ShopNav.getShopNavList(this.params).then(response => {
          this.loading = false
          this.tableData = response.data
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
        })
      },

      /** 编辑导航 */
      handleEditShopNav(row) {
        this.addShopNavshow = true
        this.isEdit = 1
        this.navId = row.id
        this.navform = {
          ...row
        }
      },

      /** 新增导航 */
      showaddShopNav() {
        this.addShopNavshow = true
        this.isEdit = 2
        // 清空表单
        this.navform = {
          name: '',
          disable: 1,
          sort: '',
          nav_url: '',
          target: 1
        }
      },

      /** 删除店铺导航 */
      handleDelShopNav(row) {
        this.$confirm('确认删除此导航, 是否继续?', '提示', { type: 'warning' }).then(() => {
          const _params = {}
          API_ShopNav.delShopNav(row.id, _params).then(response => {
            this.$message.success('删除成功')
            this.addShopNavshow = false
            this.GET_ShopNavList()
          })
        }).catch(() => {
          this.$message.info({ message: '已取消删除' })
        })
      },

      /** 导航表单 */
      POST_AddShopNav(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const _params = { ... this.navform }
            if (this.isEdit === 2) {
              API_ShopNav.addShopNav(_params).then(() => {
                this.$message.success('保存成功')
                this.addShopNavshow = false
                this.GET_ShopNavList()
              })
            } else if (this.isEdit === 1) {
              API_ShopNav.editShopNav(this.navId, _params).then(() => {
                this.$message.success('保存成功')
                this.addShopNavshow = false
                this.GET_ShopNavList()
              })
            }
          }
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

  /deep/.el-input__inner{
    width: 60%;
  }
  span.URLtip {
    display: block;
    font-size: 12px;
  }

</style>

