<template>
  <el-tabs v-model="activeName" @tab-click="handleScoreClick">
    <el-tab-pane label="评价列表" name="commentlist">
      <en-table-layout
        toolbar
        pagination
        :tableData="tableData">
        <div slot="toolbar" class="inner-toolbar">
          <div class="toolbar-btns">
          </div>
          <div class="toolbar-search">
            <en-table-search
              placeholder="请输入商品名称或评论内容"
              @search="searchEvent"
              @advancedSearch="advancedSearchEvent"
              advanced>
              <template slot="advanced-content">
                <el-form ref="advancedForm" :model="advancedForm" label-width="80px">
                  <el-form-item label="会员名称">
                    <el-input v-model="advancedForm.member_name" clearable></el-input>
                  </el-form-item>
                  <el-form-item label="商品名称">
                    <el-input v-model="advancedForm.goods_name" clearable></el-input>
                  </el-form-item>
                  <el-form-item label="评论内容">
                    <el-input v-model="advancedForm.content" clearable></el-input>
                  </el-form-item>
                  <el-form-item label="评价">
                    <el-select v-model="advancedForm.grade" placeholder="请选择" clearable>
                      <el-option label="好评" value="good"/>
                      <el-option label="中评" value="neutral"/>
                      <el-option label="差评" value="bad"/>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="回复状态">
                    <el-select v-model="advancedForm.reply_status" placeholder="请选择" clearable>
                      <el-option label="已回复" value="1"/>
                      <el-option label="未回复" value="0"/>
                    </el-select>
                  </el-form-item>
                </el-form>
              </template>
            </en-table-search>
          </div>
        </div>
      </en-table-layout>
      <div class="my-table-out" :style="{maxHeight: tableMaxHeight + 'px'}">
        <table class="my-table" v-loading="loading">
          <thead>
          <tr class="bg-order">
            <th class="shoplist-header">评论</th>
            <th style="width: 120px;">用户</th>
            <th style="width: 240px;">时间</th>
            <th style="width: 120px;">操作</th>
          </tr>
          </thead>
          <tbody v-for="item in tableData">
          <tr style="width: 100%;height: 10px;"></tr>
          <tr class="bg-order">
            <!--商品名称-->
            <td colspan="4">
              <a class="shop-name" target="_blank" :href="`${MixinBuyerDomain}/goods/${item.goods_id}`" style="color: #00a2d4;">{{ item.goods_name }}</a>
            </td>
          </tr>
          <tr>
            <!--评论-->
            <td>
              <div class="comment-content">
                <!--评论内容-->
                <p v-if="item.content" class="comment-info">
                  <i class="comment-content-name">评论内容 :</i> {{ item.content }}
                </p>
                <!--评论图片信息-->
                <p v-if="item.have_image === 1">
                  <img v-for="imgsrc in item.images" :src="imgsrc" class="goods-image"/>
                </p>
                <!--回复评论-->
                <p v-if="item.reply && item.reply.content" class="reply-comment">
                  <i class="seller-reply">回复评论 :</i> {{ item.reply.content }}
                </p>
              </div>
            </td>
            <!--用户-->
            <td>{{ item.member_name }}</td>
            <!--时间-->
            <td>{{item.create_time | unixToDate }}</td>
            <!--操作-->
            <td>
              <el-button
                type="primary"
                v-if="item.reply_status == 0"
                @click="handleReplyComment(item)">回复
              </el-button>
            </td>
          </tr>
          </tbody>
          <div v-if="tableData.length === 0" class="empty-block">
            暂无数据
          </div>
        </table>
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
      </div>
      <el-dialog title="回复评论" :visible.sync="replyCommentShow" width="30%">
        <el-form :model="commentForm" :rules="replyRules" ref="commentForm">
          <el-form-item label="评论内容" :label-width="formLabelWidth">
            <span>{{commentForm.comment_content}}</span>
          </el-form-item>
          <el-form-item label="评论图片" :label-width="formLabelWidth" v-if="commentForm.have_image === 1">
            <img v-for="imgsrc in commentForm.comment_imgs" :src="imgsrc"
                 style="margin-right:3px;width:50px;height:50px;">
          </el-form-item>
          <el-form-item label="回复内容" :label-width="formLabelWidth" prop="reply_content">
            <el-input type="textarea" rows="5" :maxlength="500" v-model="commentForm.reply_content" auto-complete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="replyCommentShow = false">取 消</el-button>
          <el-button type="primary" @click="saveCommentReply('commentForm')">确 定</el-button>
        </div>
      </el-dialog>
    </el-tab-pane>
    <el-tab-pane label="店铺评分" name="score" class="shop-score-container">
      <div class="shop-score">
        <div class="score-title">宝贝与描述相符</div>
        <div class="score-pic">
          <dl class="score-pic-content">
            <dt>
              <em :style="socreDescription">{{ shopInfo.shop_description_credit | socre }}</em>
            </dt>
            <dd>非常不满</dd>
            <dd>不满意</dd>
            <dd>一般</dd>
            <dd>满意</dd>
            <dd>非常满意</dd>
          </dl>
        </div>
      </div>
      <div class="shop-score">
        <div class="score-title">卖家的服务态度</div>
        <div class="score-pic">
          <dl class="score-pic-content">
            <dt>
              <em :style="socreService">{{ shopInfo.shop_service_credit | socre }}</em>
            </dt>
            <dd>非常不满</dd>
            <dd>不满意</dd>
            <dd>一般</dd>
            <dd>满意</dd>
            <dd>非常满意</dd>
          </dl>
        </div>
      </div>
      <div class="shop-score">
        <div class="score-title">卖家的发货速度</div>
        <div class="score-pic">
          <dl class="score-pic-content">
            <dt>
              <em :style="socreDelivery">{{ shopInfo.shop_delivery_credit | socre  }}</em>
            </dt>
            <dd>非常不满</dd>
            <dd>不满意</dd>
            <dd>一般</dd>
            <dd>满意</dd>
            <dd>非常满意</dd>
          </dl>
        </div>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import * as API_comment from '@/api/comment'
  import { CategoryPicker } from '@/components'

  export default {
    name: 'goodsList',
    components: {
      [CategoryPicker.name]: CategoryPicker
    },
    data() {
      return {
        /** 当前面板 */
        activeName: 'commentlist',

        /** 店铺信息 */
        shopInfo: this.$store.getters.shopInfo,

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

        /** 高级搜索数据 */
        advancedForm: {
          /** 商品名称 */
          goods_name: '',

          /** 会员名称 */
          member_name: '',

          /** 评论内容 */
          content: '',

          /** 评价 */
          grade: '',

          /** 回复状态 */
          reply_status: ''
        },

        /** 是否显示回复（审核）框*/
        replyCommentShow: false,

        /** 表单项的marginLeft */
        formLabelWidth: '120px',

        /** 弹框 表单*/
        commentForm: {
          /** 评论id */
          comment_id: '',

          /** 是否有图 */
          have_image: '',

          /** 评论内容 */
          comment_content: '',

          /** 评论图片数组 */
          comment_imgs: [],

          /** 回复内容 */
          reply_content: ''
        },

        /** 检验工具 */
        replyRules: {
          reply_content: [
            { required: true, message: '回复内容不可为空', trigger: 'blur' },
            { max: 500, message: '回复内容最多500个字', trigger: 'blur' }
          ]
        },

        /** 表格最大高度 */
        tableMaxHeight: (document.body.clientHeight - 54 - 34 - 50 - 15)
      }
    },
    computed: {
      socreDescription() {
        return {
          'left': this.shopInfo.shop_description_credit
            ? parseFloat((this.shopInfo.shop_description_credit / 5).toFixed(2) * 100 - 7.5) + '%' : 0
        }
      },
      socreService() {
        return {
          'left': this.shopInfo.shop_service_credit
            ? parseFloat((this.shopInfo.shop_service_credit / 5).toFixed(2) * 100 - 7.5) + '%' : 0
        }
      },
      socreDelivery() {
        return {
          'left': this.shopInfo.shop_delivery_credit
            ? parseFloat((this.shopInfo.shop_delivery_credit / 5).toFixed(2) * 100 - 7.5) + '%' : 0
        }
      }
    },
    filters: {
      socre(val) {
        return val.toFixed(2)
      }
    },
    mounted() {
      this.GET_CommmentsList()
      window.onresize = this.countTableHeight
    },
    methods: {
      socre(val) {
        if (Number.isInteger(val)) {
          return {
            left: (val / 5).toFixed(2) + '%'
          }
        } else {
          return {
            left: 0
          }
        }
      },

      /** 计算高度 */
      countTableHeight() {
        this.tableHeight = (document.body.clientHeight - 54 - 35 - 50)
      },

      /** 切换面板 */
      handleScoreClick(tab) {
        this.activeName = tab.name
        if (this.activeName === 'commentlist') {
          this.GET_CommmentsList()
        }
      },

      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_CommmentsList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_CommmentsList()
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        this.params = {
          ...this.params,
          keyword: data
        }
        Object.keys(this.advancedForm).forEach(key => delete this.params[key])
        this.GET_CommmentsList()
      },

      /** 高级搜索事件触发 */
      advancedSearchEvent() {
        this.params = {
          ...this.params,
          ...this.advancedForm
        }
        delete this.params.keyword
        this.GET_CommmentsList()
      },

      GET_CommmentsList() {
        this.loading = true
        API_comment.getCommentList(this.params).then(response => {
          this.loading = false
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
          this.tableData = response.data
        })
      },

      /** 商家回复*/
      handleReplyComment(item) {
        this.replyCommentShow = true
        this.commentForm = {
          comment_id: item.comment_id,
          isPass: 1,
          have_image: item.have_image,
          comment_content: item.content,
          comment_imgs: item.images,
          reply_content: ''
        }
      },

      /** 保存评论回复*/
      saveCommentReply(FormName) {
        this.$refs[FormName].validate((valid) => {
          if (valid) {
            API_comment.replyComment(this.commentForm.comment_id, { reply: this.commentForm.reply_content }).then(() => {
              this.replyCommentShow = false
              this.GET_CommmentsList()
              this.$message.success('保存成功')
            })
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

  /*暂无数据时的样式*/
  /deep/ .el-table__empty-block {
    display: none;
  }

  .empty-block {
    position: relative;
    min-height: 60px;
    line-height: 60px;
    text-align: center;
    width: 139%;
    height: 100%;
    font-size: 14px;
    color: #606266;
  }

  /*表格信息*/
  .my-table-out{
    overflow-y: scroll;
    text-overflow: ellipsis;
    width: 100%;
    max-height: 800px;
  }
  .my-table {
    width: 100%;
    margin-bottom: 40px;
    background: #fff;
    border-collapse: collapse;
    font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif;
    font-size: 14px;
    font-bold: 700;
    .bg-order {
      background: #FAFAFA;
    }
    thead {
      width: 100%;
      th {
        border: 1px solid #ebeef5;
        border-collapse: collapse;
        color: #909399;
        padding: 10px 20px;
      }
    }
    tbody {
      width: 100%;
      margin-top: 10px;
      td {
        border: 1px solid #ebeef5;
        border-collapse: collapse;
        vertical-align: middle;
        text-align: center;
        padding: 10px 20px;
      }
      td:first-child {
        text-align: left;
      }
      /*商品名称*/
      a.shop-name {
        color: #0579c6;
        outline: none;
        font-weight: bold;
      }
      /*评论内容*/
      .comment-content {
        margin: 0;
        p {
          margin: 10px 0;
          i {
            font-weight: bold;
          }
        }
        p.comment-info {
          color: #999999;
        }
        p.reply-comment {
          color: #f42424;
        }
      }
    }
    /*图片*/
    .goods-image {
      width: 50px;
      height: 50px;
      margin: 3px;
    }
  }

  /*分页信息*/
  section>div {
    position: relative;
  }
  .el-pagination {
    text-align: right;
    width: 100%;
    height: 40px;
    background: #ffffff;
    position: absolute;
    bottom: 0;
    right: 0;
    border-top: 1px solid #e5e5e5;
    padding: 5px 20px;
  }
  /** 店铺评分 */
  .shop-score-container {
    background: #fff;
    border: 1px solid #ddd;
    padding: 10px;
  }
  .shop-score {
    width: 60%;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: space-around;
    align-items: center;
    margin: 50px 0;
    padding: 15px;
    div.score-title {

    }
    /** 评分图标标识 */
    div.score-pic {
      .score-pic-content {
        display: block;
        margin: 0 auto;
        padding: 0;
        width: 352px;
        height: 20px;
        dt {
          height: 20px;
          position: relative;
          background: url(./images/rate_column.gif) no-repeat 0px -88px;
        }
        em {
          position: absolute;
          background: url(./images/rate_column.gif) no-repeat 0 0;
          top: -26px;
          color: #fff;
          display: block;
          font-weight: 600;
          height: 30px;
          line-height: 16px;
          padding: 0 0 7px;
          text-align: center;
          width: 37px;
          font-size: 14px;
        }
      }
      dd {
        width: 65px;
        display: inline-block;
        margin-left: 0px;
        font-size: 14px;
      }
    }
  }
</style>

