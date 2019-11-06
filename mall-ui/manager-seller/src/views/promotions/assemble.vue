<template>
  <div>
    <div class="content-goods-publish">
      <el-form
        :model="activityForm"
        status-icon
        :rules="rules"
        label-position="right"
        ref="activityForm"
        label-width="120px"
        class="demo-ruleForm">
        <el-form-item  label="活动名称：" prop="promotion_name">
          <el-input
            v-model="activityForm.promotion_name"
            @change="activityForm.promotion_name  = activityForm.promotion_name.trim()"
            placeholder="不超过25个字符"
            maxLength="50"
          ></el-input>
        </el-form-item>
        <el-form-item  label="活动标题：" prop="promotion_title">
          <el-input
            v-model="activityForm.promotion_title"
            @change="activityForm.promotion_title  = activityForm.promotion_title.trim()"
            placeholder="不超过5个字符"
            maxLength="5"
          ></el-input>
        </el-form-item>
        <el-form-item label="生效时间：" prop="take_effect_time">
          <el-date-picker
            v-model="activityForm.take_effect_time"
            type="datetimerange"
            value-format="timestamp"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="[MixinDefaultTime, MixinDefaultTime]"
            :picker-options="{disabledDate(time) { return time.getTime() < Date.now() - 8.64E7 }}">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="参团人数：" prop="required_num">
          <el-input v-model="activityForm.required_num" @change="activityForm.required_num = activityForm.required_num.trim()">
            <template slot="append">人</template>
          </el-input>
        </el-form-item>
        <el-form-item label="限购数量：">
          <el-input v-model="activityForm.limit_num" @change="activityForm.limit_num = activityForm.limit_num.trim()">
            <template slot="append">件/人</template>
          </el-input>
        </el-form-item>
        <el-form-item label="虚拟成团：">
          <el-switch
            v-model="activityForm.enable_mocker"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="开启"
            inactive-text="关闭"
          />
          <p class="tip">开启虚拟成团后，24小时内人数未满的团，系统将会模拟匿名买家凑满人数，使该团成团；开启以提高成团率</p>
        </el-form-item>
        <el-form-item>
          <el-button v-show="editEnabled" type="primary" @click="handleSaveActivity('activityForm')">保存设置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--遮罩层-->
    <div :class="{'cus-mask': !editEnabled}"></div>
  </div>
</template>

<script>
  import * as API_activity from '@/api/activity'
  import { RegExp } from '~/ui-utils'

  export default {
    name: 'secondHalfPrice',
    data() {
      return {
        /** 列表loading状态 */
        loading: false,

        /** 列表参数 */
        params: {
          promotion_id: ''
        },

        /** 新增满减表单信息*/
        activityForm: {},

        /** 表单校验规则*/
        rules: {
          promotion_name: [
            { required: true, message: '请输入活动名称', trigger: 'blur' },
            { min: 2, max: 25, message: '长度在2-25个字符之内', trigger: 'blur' }
          ],
          promotion_title: [
            { min: 0, max: 5, message: '长度在5个字符之内', trigger: 'blur' }
          ],
          take_effect_time: [
            { type: 'array', required: true, message: '请选择起止时间', trigger: 'blur' }
          ],
          required_num: [
            { required: true, message: '请填写参团人数', trigger: 'blur' },
            {
              validator: (rule, value, callback) => {
                if (!value) {
                  callback(new Error('请填写参团人数'))
                } else if (!RegExp.integer.test(value)) {
                  callback(new Error('请填写正整数'))
                } else {
                  callback()
                }
              }
            }
          ]
        },

        // 是否不可编辑 即是否是进行中 或者已失效状态 默认可以
        editEnabled: true
      }
    },
    mounted() {
      this.activityForm = {}
      if (this.$route.params.promotion_id) {
        this.params.promotion_id = this.$route.params.promotion_id
        this.GET_AssembleActivity()
      }
    },
    methods: {
      /** 查询单个活动信息 */
      GET_AssembleActivity() {
        API_activity.getAssembleActivity(this.params.promotion_id).then(response => {
          this.activityForm = {
            ...response,
            take_effect_time: [parseInt(response.start_time) * 1000, parseInt(response.end_time) * 1000],
            enable_mocker: response.enable_mocker === 1
          }
        })
      },

      /** 保存表单设置*/
      handleSaveActivity(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.activityForm.start_time = parseInt(this.activityForm.take_effect_time[0] / 1000)
            this.activityForm.end_time = parseInt(this.activityForm.take_effect_time[1] / 1000)
            const _params = { ...this.activityForm }
            _params.enable_mocker = _params.enable_mocker ? 1 : 0
            delete _params.take_effect_time
            if (this.activityForm.promotion_id) {
              API_activity.editAssembleActivity(_params.promotion_id, _params).then(() => {
                this.$message.success('保存成功！')
                this.$router.push({ path: '/promotions/assembleManager' })
                this.$route.params.callback()
              }).catch(() => {})
            } else {
              API_activity.addAssembleActivity(_params).then(() => {
                this.$message.success('添加成功！')
                this.$router.push({ path: '/promotions/assembleManager' })
                this.$route.params.callback()
              }).catch(() => {})
            }
          } else {
            this.$message.error('表单存在错误，请修正')
          }
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /deep/ {
    div.toolbar {
      height: 70px;
      padding: 20px 0;
    }
    .el-input {
      max-width: 400px;
    }
    .el-textarea__inner {
      max-width: 500px;
    }
  }

  .content-goods-publish {
    padding: 15px;
    margin: 0 auto;
    text-align: center;
    border: 1px solid #ddd;
    background: none repeat scroll 0 0 #fff;
  }

  /*表单信息*/
  .el-form {
    padding-bottom: 80px;
    .el-form-item {
      width: 100%;
      text-align: left;

      /*送积分*/
      .integral-show {
        .el-input {
          width: 50px;
        }
      }
      /** 下拉列表 */
      /deep/ .el-select .el-select--medium {
        width: 160px;
      }
    }
    .discount-model {
      div {
        margin: 5px 0;
      }
    }
    p.tip {
      font-size: 14px;
      color: #ccc;
    }
  }

  /** 表格信息 */
  .goods-info {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: flex-start;
    align-items: center;
    div {
      a {
        color: #409EFF;
      }
      display: flex;
      flex-direction: column;
      flex-wrap: nowrap;
      justify-content: space-between;
      align-items: flex-start;
      margin-left: 20px;
    }
  }

  /*遮罩层*/
  .cus-mask {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 10000;
  }
</style>


