<template>
  <div>
    <en-table-layout
      :tableData="tableData.data"
      :loading="loading"
    >
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
          <el-button size="mini" type="primary" icon="el-icon-circle-plus-outline" @click="handleAddSeckill">添加</el-button>
        </div>
      </div>
      <template slot="table-columns">
        <el-table-column prop="seckill_name" label="活动名称"/>
        <el-table-column prop="start_day" :formatter="MixinUnixToDate" label="活动开始时间">
          <template slot-scope="scope">
            {{ formatterDate(scope.row.start_day) }}
          </template>
        </el-table-column>
        <el-table-column prop="apply_end_time" :formatter="MixinUnixToDate" label="报名截止时间"/>
        <el-table-column prop="seckill_status_text" label="状态"/>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.seckill_status === 'RELEASE'"
              size="mini"
              type="primary"
              @click="handleAuditSeckill(scope.$index, scope.row)"
            >审核商品</el-button>
            <el-button
              size="mini"
              type="primary"
              @click="handleViewSeckillGoods(scope.$index, scope.row)"
            >查看商品</el-button>
            <el-button
              v-if="scope.row.seckill_status === 'EDITING'"
              size="mini"
              type="primary"
              @click="handleEditSeckill(scope.$index, scope.row)"
            >编辑</el-button>
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
      title="添加限时抢购"
      :visible.sync="dialogSeckillVisible"
      width="500px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form :model="seckillForm" :rules="seckillRules" ref="seckillForm" label-width="120px">
        <el-form-item label="活动名称" prop="seckill_name">
          <el-input v-model="seckillForm.seckill_name" :maxlength="20" clearable></el-input>
        </el-form-item>
        <el-form-item label="报名截止时间" prop="apply_end_time">
          <el-date-picker
            v-model="seckillForm.apply_end_time"
            type="datetime"
            placeholder="选择报名截止时间"
            :editable="false"
            :default-time="MixinDefaultTime"
            value-format="timestamp"
            :picker-options="{disabledDate(time) { return time.getTime() < Date.now() - 8.64E7 }}">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="活动时间" prop="start_day">
          <el-date-picker
            v-model="seckillForm.start_day"
            type="date"
            :editable="false"
            :default-time="MixinDefaultTime"
            placeholder="选择活动日期"
            value-format="timestamp"
            :picker-options="{disabledDate(time) { return time.getTime() < Date.now() - 8.64E7 }}">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="抢购阶段" prop="range_list">
          <el-tag
            :key="tag"
            v-for="tag in seckillForm.range_list"
            closable
            :disable-transitions="false"
            class="time-tag"
            @close="() => { seckillForm.range_list.splice(seckillForm.range_list.indexOf(tag), 1) }">
            {{ tag < 10 ? '0' + tag : tag }} : 00
          </el-tag>
          <el-input
            class="input-new-tag"
            v-if="inputVisible"
            v-model.number="inputValue"
            type="number"
            ref="saveTagInput"
            size="small"
            @keyup.enter.native="handleTagInputConfirm"
            @blur="handleTagInputConfirm"
            :min="0" :max="23"
          />
          <el-button v-else class="button-new-tag" size="small" @click="showTagInput">+ 添加时间段</el-button>
        </el-form-item>
        <el-form-item label="申请规则" prop="seckill_rule">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入申请规则"
            v-model="seckillForm.seckill_rule"
            :maxlength="200"
            clearable
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogSeckillVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitSeckillForm('release')">立即发布</el-button>
        <el-button type="primary" @click="submitSeckillForm('save')">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import * as API_Promotion from '@/api/promotion'
  import { Foundation } from '~/ui-utils'

  export default {
    name: 'seckillList',
    data() {
      return {
        // 列表loading状态
        loading: false,
        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10
        },
        /** 列表数据 */
        tableData: '',
        /** 添加、编辑限时抢购 dialog */
        dialogSeckillVisible: false,

        /** 添加、编辑限时抢购 表单 */
        seckillForm: {},

        /** 添加、编辑限时抢购 表单规则 */
        seckillRules: {
          seckill_name: [this.MixinRequired('请输入活动名称！')],
          apply_end_time: [this.MixinRequired('请选择截止报名时间！')],
          start_day: [this.MixinRequired('请选择活动日期！')],
          range_list: [{ type: 'array', required: true, message: '请至少添加一个时间段', trigger: 'change' }],
          seckill_rule: [this.MixinRequired('申请规则不能为空！')]
        },
        inputVisible: false,
        inputValue: ''
      }
    },
    mounted() {
      this.GET_SeckillList()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_SeckillList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_SeckillList()
      },

      /** 添加限时抢购 */
      handleAddSeckill() {
        this.seckillForm = { range_list: [] }
        this.dialogSeckillVisible = true
      },

      /** 编辑限时抢购 */
      handleEditSeckill(index, row) {
        this.seckillForm = this.MixinClone(row)
        this.seckillForm.apply_end_time *= 1000
        this.seckillForm.start_day *= 1000
        this.dialogSeckillVisible = true
      },

      /** 审核 */
      handleAuditSeckill(index, row) {
        this.$router.push({ path: `/promotions/seckill-manage/seckill-list/audit/${row.seckill_id}` })
      },

      /** 查看限时抢购商品 */
      handleViewSeckillGoods(index, row) {
        this.$router.push({ path: `/promotions/seckill-manage/seckill-list/pass/${row.seckill_id}` })
      },

      // 显示时间段Tag输入框
      showTagInput() {
        this.inputVisible = true
        this.$nextTick(_ => {
          this.$refs.saveTagInput.$refs.input.focus()
        })
      },

      // 时间段Tag输入确认事件
      handleTagInputConfirm() {
        let inputValue = this.inputValue
        const { range_list } = this.seckillForm
        if (inputValue !== undefined && inputValue !== '') {
          if (inputValue < 0 || inputValue > 23) {
            this.$message.error('时间段应在0-23之间！')
            this.$refs.saveTagInput.$refs.input.focus()
            return false
          }
          if (inputValue <= range_list[range_list.length - 1]) {
            this.$message.error('不能小于或等于上一时间段！')
            this.$refs.saveTagInput.$refs.input.focus()
            return false
          }
          range_list.push(inputValue)
        }
        this.inputVisible = false
        this.inputValue = ''
      },

      /** 提交限时抢购表单 */
      submitSeckillForm(type) {
        this.$refs['seckillForm'].validate((valid) => {
          if (valid) {
            let { seckill_id } = this.seckillForm
            const params = this.MixinClone(this.seckillForm)
            params.apply_end_time /= 1000
            params.start_day /= 1000
            if (type === 'save') {
              if (!seckill_id) {
                API_Promotion.addSeckill(params).then(response => {
                  this.dialogSeckillVisible = false
                  this.$message.success('添加成功！')
                  this.GET_SeckillList()
                })
              } else {
                API_Promotion.editSeckill(seckill_id, params).then(response => {
                  this.dialogSeckillVisible = false
                  this.$message.success('编辑成功！')
                  this.GET_SeckillList()
                })
              }
            } else {
              if (!seckill_id) seckill_id = 0
              API_Promotion.releaseSeckill(seckill_id, params).then(() => {
                this.dialogSeckillVisible = false
                this.$message.success('发布成功！')
                this.GET_SeckillList()
              })
            }
          } else {
            this.$message.error('表单填写有误，请检查！')
            return false
          }
        })
      },

      /** 格式化时间 */
      formatterDate(date) {
        return Foundation.unixToDate(date, 'yyyy-MM-dd')
      },

      /** 获取限时抢购列表 */
      GET_SeckillList() {
        this.loading = true
        API_Promotion.getSeckillList(this.params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .time-tag {
    display: block;
    width: 80px;
    margin: 5px 0;
  }
  .input-new-tag {
    width: 112px;
  }

  /deep/ input::-webkit-outer-spin-button,
  /deep/ input::-webkit-inner-spin-button {
    -webkit-appearance: none !important;
    margin: 0;
  }
</style>
