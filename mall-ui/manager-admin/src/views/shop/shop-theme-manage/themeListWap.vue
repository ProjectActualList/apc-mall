<template>
  <div>
    <en-table-layout
      :tableData="tableData.data"
      :loading="loading"
    >
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
          <el-button size="mini" type="primary" icon="el-icon-circle-plus-outline" @click="handleAddShopTheme">添加</el-button>
        </div>
      </div>
      <template slot="table-columns">
        <el-table-column prop="name" label="模板名称"/>
        <el-table-column prop="mark" label="模板标识"/>
        <el-table-column label="是否为默认">
          <template slot-scope="scope">{{ scope.row.is_default ? '是' : '否' }}</template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleEditShopTheme(scope.$index, scope.row)">修改</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDeleteShopTheme(scope.$index, scope.row)">删除</el-button>
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
    <!--添加、编辑店铺模板 dialog-->
    <el-dialog
      :title="(shopThemeForm.id ? '编辑' : '添加') + '店铺模板'"
      :visible.sync="dialogShopThemeVisible"
      width="450px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form :model="shopThemeForm" :rules="shopThemeRules" ref="shopThemeForm" label-width="100px">
        <el-form-item label="模板名称" prop="name">
          <el-input v-model="shopThemeForm.name" :maxlength="20" placeholder="请输入模板名称"></el-input>
        </el-form-item>
        <el-form-item label="模板标识" prop="mark">
          <el-input v-model="shopThemeForm.mark" :maxlength="50" placeholder="确保唯一性，且不可与WAP模板重复"></el-input>
        </el-form-item>
        <el-form-item label="模板图片" prop="image_path">
          <el-upload
            :action="MixinUploadApi"
            :on-success="(res) => { shopThemeForm.image_path = res.url }"
            :on-remove="() => { shopThemeForm.image_path = '' }"
            :file-list="shopThemeForm.image_path ? [{ name: '模板图片', url: shopThemeForm.image_path }] : []"
            list-type="picture"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="是否默认">
          <el-radio-group v-model="shopThemeForm.is_default">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogShopThemeVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitShopThemeForm('shopThemeForm')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import * as API_Shop from '@/api/shop'

  export default {
    name: 'themeListWap',
    data() {
      return {
        /** 列表loading状态 */
        loading: false,
        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10,
          type: 'WAP'
        },
        /** 列表数据 */
        tableData: '',
        /** 添加、编辑店铺模板 dialog */
        dialogShopThemeVisible: false,
        /** 添加、编辑店铺模板 */
        shopThemeForm: {},
        /** 添加、编辑店铺模板 规则 */
        shopThemeRules: {
          name: [this.MixinRequired('请输入模板名称！')],
          mark: [this.MixinRequired('请输入模板标识！')],
          image_path: [this.MixinRequired('请上传模板图片！')]
        }
      }
    },
    mounted() {
      this.GET_ShopThemeList()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_ShopThemeList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_ShopThemeList()
      },

      /** 添加店铺模板 */
      handleAddShopTheme() {
        this.shopThemeForm = {
          type: 'WAP',
          is_default: 0
        }
        this.dialogShopThemeVisible = true
      },

      /** 编辑店铺模板 */
      handleEditShopTheme(index, row) {
        this.shopThemeForm = this.MixinClone(row)
        this.dialogShopThemeVisible = true
      },

      /** 删除店铺模板 */
      handleDeleteShopTheme(index, row) {
        this.$confirm('确定要删除这个模板吗？', '提示', { type: 'warning' }).then(() => {
          API_Shop.deleteShopTheme(row.id).then(response => {
            this.$message.success('删除成功！')
            this.GET_ShopThemeList()
          })
        }).catch(() => {})
      },

      /** 添加、编辑店铺模板 提交表单 */
      submitShopThemeForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const { id } = this.shopThemeForm
            if (!id) {
              API_Shop.addShopTheme(this.shopThemeForm).then(response => {
                this.dialogShopThemeVisible = false
                this.$message.success('添加成功！')
                this.GET_ShopThemeList()
              })
            } else {
              API_Shop.editShopTheme(id, this.shopThemeForm).then(resposne => {
                this.dialogShopThemeVisible = false
                this.$message.success('保存成功！')
                this.MixinSetTableData(this.tableData, 'id', id, resposne)
              })
            }
          } else {
            this.$message.error('表单填写有误，请检查！')
            return false
          }
        })
      },

      /** 获取会员列表 */
      GET_ShopThemeList() {
        this.loading = true
        API_Shop.getShopThemeList(this.params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>
