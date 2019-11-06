<template>
  <div>
    <en-table-layout
      :tableData="tableData.data"
      :loading="loading"
    >
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
          <el-button size="mini" type="primary" icon="el-icon-circle-plus-outline" @click="handleAddExpress">添加</el-button>
        </div>
      </div>
      <template slot="table-columns">
        <el-table-column prop="name" label="公司名称"/>
        <el-table-column prop="code" label="公司代码"/>
        <el-table-column prop="kdcode" label="快递鸟代码"/>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleEditExpress(scope.$index, scope.row)">编辑</el-button>
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
    <el-dialog
      title="物流公司"
      :visible.sync="dialogExpressVisible"
      width="30%"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form :model="expressForm" :rules="expressRules" ref="expressForm" label-width="120px">
        <el-form-item label="物流公司名称" prop="name">
          <el-input v-model="expressForm.name"></el-input>
        </el-form-item>
        <el-form-item label="物流公司代码" prop="code">
          <el-input v-model="expressForm.code"></el-input>
        </el-form-item>
        <el-form-item label="快递鸟物流代码" prop="kdcode">
          <el-input v-model="expressForm.kdcode"></el-input>
        </el-form-item>
        <el-form-item label="电子面单" prop="is_waybill">
          <el-radio-group v-model="expressForm.is_waybill">
            <el-radio :label="1">支持</el-radio>
            <el-radio :label="0">不支持</el-radio>
          </el-radio-group>
        </el-form-item>
        <template v-if="expressForm.is_waybill">
          <el-form-item label="客户号" prop="customer_name">
            <el-input v-model="expressForm.customer_name"></el-input>
          </el-form-item>
          <el-form-item label="电子面单密码" prop="customer_pwd">
            <el-input v-model="expressForm.customer_pwd"></el-input>
          </el-form-item>
        </template>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogExpressVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitExpressForm('expressForm')">确 定</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
  import * as API_Express from '@/api/express'

  export default {
    name: 'expressSettings',
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
        tableData: '',
        /** 快递公司 表单 */
        expressForm: {},
        /** 快递公司 表单规则 */
        expressRules: {
          name: [this.MixinRequired('请输入物流公司名称！')],
          code: [this.MixinRequired('请输入物流公司代码！')],
          kdcode: [this.MixinRequired('请输入快递鸟物流代码！')],
          customer_name: [this.MixinRequired('请输入物流公司客户号！')],
          customer_pwd: [this.MixinRequired('请输入物流公司电子面单密码！')]
        },
        /** 快递公司 dialog */
        dialogExpressVisible: false
      }
    },
    watch: {
      'expressForm.is_waybill': function waybillChange(newVal) {
        this.expressRules.kdcode[0].required = !!newVal
        this.expressRules.customer_name[0].required = !!newVal
        this.expressRules.customer_pwd[0].required = !!newVal
      }
    },
    mounted() {
      this.GET_ExpressList()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_ExpressList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_ExpressList()
      },

      /** 添加快递公司 */
      handleAddExpress() {
        this.expressForm = { is_waybill: 0 }
        this.dialogExpressVisible = true
      },

      /** 编辑物流公司 */
      handleEditExpress(index, row) {
        const params = JSON.parse(JSON.stringify(row))
        if (typeof params.is_waybill !== 'number') {
          params.is_waybill = 0
        }
        this.expressForm = params
        this.dialogExpressVisible = true
      },

      /** 快递公司 提交表单 */
      submitExpressForm(formName) {
        this.$refs[formName].validate(valid => {
          if (valid) {
            const { id } = this.expressForm
            if (!id) {
              API_Express.addExpress(this.expressForm).then(response => {
                this.dialogExpressVisible = false
                this.$message.success('添加成功！')
                this.GET_ExpressList()
              })
            } else {
              API_Express.editExpress(id, this.expressForm).then(response => {
                this.dialogExpressVisible = false
                this.$message.success('保存成功！')
                this.MixinSetTableData(this.tableData, 'id', id, response)
              })
            }
          } else {
            this.$message.error('表单填写有误，请检查！')
            return false
          }
        })
      },
      /** 获取物流公司列表 */
      GET_ExpressList() {
        this.loading = true
        API_Express.getExpressList(this.params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => {
          this.loading = false
        })
      }
    }
  }
</script>
