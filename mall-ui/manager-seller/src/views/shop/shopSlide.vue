<template>
  <div class="shopBanner-container">
    <!--提示-->
    <div class="shopBanner-tips">
      <p>1. 最多上传5张幻灯片。</p>
      <p>2. 支持jpg、jpeg、gif、png格式上传，建议图片宽度1920px、高度在400px到500px之间、大小4.00M以内的图片。
        提交2~5张图片可以进行幻灯片播放，一张图片没有幻灯片播放效果。</p>
      <p>3. 操作完成以后，按“保存”按钮，可以在当前页面进行幻灯片展示。</p>
      <p>4. 跳转链接必须是 <b class="color-weight">http:// 或 https://</b></p>
    </div>
    <!--轮播-->
    <el-carousel indicator-position="outside" height="500px">
      <el-carousel-item v-for="(item,index) in tableData" :key="index">
        <img :src="item.img" alt="">
      </el-carousel-item>
    </el-carousel>
    <!--上传 更新 删除 编辑幻灯片-->
    <div class="shopBanner-operaSlide">
      <ul>
        <li v-for="(item,index) in tableData">
          <div class="slide-item"
            @mouseover="overSlideShow(item.silde_id)"
            @mouseout="outSlideHide(item.silde_id)">
            <div class="del-btn-pos">
              <el-button class="del-btn"
                v-show="currentShopBannerId === item.silde_id"
                type="danger"
                plain
                size="mini"
                @click="Del_Slide(item, index)">删除</el-button>
            </div>
            <div class="img-show">
              <img :src="item.img" alt="" v-show="!(activeIndex === index && isShowProgress)">
              <el-progress
                type="circle"
                class="progress-circle"
                v-show="activeIndex === index && isShowProgress"
                :percentage="currentPercent"
                :width="100"/>
            </div>
            <label>跳转URL...</label>
            <el-input
              class="slide-input-link"
              clearable
              size="mini"
              maxlength="255"
              auto-complete="off"
              v-model="item.silde_url"></el-input>
            <el-upload
              class="upload-demo"
              :action="`${MixinUploadApi}?scene=shop`"
              :key="index"
              list-type="picture"
              :limit="1"
              :show-file-list="false"
              :on-progress="upLoading"
              :on-success="uploadSuccess"
              :file-list="fileList">
              <el-button type="primary" @click="uploadSlide(item, index)">
                上传
                <i class="el-icon-upload el-icon--right"></i>
              </el-button>
            </el-upload>
          </div>
        </li>
        <li v-if="tableData.length < 5 ">
          <div class="slide-item" >
            <div class="img-show">
              <img src="http://javashop-statics.oss-cn-beijing.aliyuncs.com/demo/AAC7404DD7144969AC913857B575C976.png"/>
            </div>
            <el-button type="primary" size="mini" plain @click="POST_AddSlide">+新增</el-button>
          </div>
        </li>
      </ul>
      <div class="save-btn">
        <el-button type="primary" size="medium" @click="POST_SaveSlide">保存</el-button>
      </div>
    </div>
  </div>
</template>

<script>
  import * as API_ShopSlide from '@/api/shopSlide'
  import { RegExp } from '~/ui-utils'
  export default {
    name: 'shopBanner',
    data() {
      return {
        /** 列表loading状态 */
        loading: false,

        /** 列表参数 */
        params: {},

        /** 列表数据*/
        tableData: [],

        /** 上传时的幻灯片*/
        uploadShopBanner: {},

        /** 当前幻灯片id*/
        currentShopBannerId: '',

        /** 存储上传的图片列表 数量限制为1*/
        fileList: [],

        /** 当前百分比 */
        currentPercent: 0,

        /** 是否显示进度条 */
        isShowProgress: false,

        /** 当前正在操作的幻灯片 */
        activeIndex: 0
      }
    },
    mounted() {
      this.GET_ShopSlideList()
    },
    methods: {
      /** 获取幻灯片列表*/
      GET_ShopSlideList() {
        this.loading = true
        API_ShopSlide.getShopSlideList(this.params).then(response => {
          this.loading = false
          this.tableData = response
        })
      },

      /** 鼠标移入*/
      overSlideShow(id) {
        this.currentShopBannerId = id
      },

      /** 鼠标移出*/
      outSlideHide() {
        this.currentShopBannerId = ''
      },

      /** 上传幻灯片*/
      uploadSlide(item, index) {
        this.uploadShopBanner = item
        this.activeIndex = index
      },

      /** 文件正在上传时的钩子 */
      upLoading(event, file, fileList) {
        this.timer = setInterval(() => {
          if (this.currentPercent < 100) {
            this.currentPercent += 5
          } else {
            clearInterval(this.timer)
            this.timer = null
            setTimeout(() => {
              this.isShowProgress = false
            }, 500)
          }
        }, 30)
        this.isShowProgress = true
      },

      /** 上传成功钩子*/
      uploadSuccess(response) {
        this.uploadShopBanner.img = response.url
        this.uploadShopBanner.silde_url = ''
        this.fileList = []
        this.isShowProgress = false
        this.$message.success('上传成功')
      },

      /** 新增幻灯片*/
      POST_AddSlide() {
        this.tableData.push({
          silde_url: '',
          img: 'http://javashop-statics.oss-cn-beijing.aliyuncs.com/demo/AAC7404DD7144969AC913857B575C976.png'
        })
      },

      /** 删除幻灯片*/
      Del_Slide(item, index) {
        this.$confirm('确定要删除此幻灯片么？', '确认信息')
          .then(() => {
            if (item.silde_id) {
              API_ShopSlide.delShopSlide(item.silde_id, {}).then(() => {
                this.$message.success('删除成功')
                this.GET_ShopSlideList()
              })
            } else {
              this.tableData.splice(index, 1)
            }
          })
      },

      /** 保存幻灯片*/
      POST_SaveSlide() {
        let _result = false
        const _params = this.tableData.map(key => {
          if (!RegExp.URL.test(key.silde_url)) {
            _result = true
          }
          return {
            silde_id: key.silde_id || 0,
            silde_url: key.silde_url,
            img: key.img
          }
        })
        if (_result) {
          this.$message.error('存在URL错误，请输入正确的URL')
          return
        }
        API_ShopSlide.saveShopSlide(_params).then(() => {
          this.$message.success('保存成功')
          this.GET_ShopSlideList()
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .shopBanner-container {
    margin: 20px;
    padding: 20px;
    background: #ffffff;
    border: 1px solid #dddddd;
  }

  /deep/ div.img-show {
    position: relative;
    /*圆形进度条*/
    .progress-circle {
      position: absolute;
      width: 80px;
      height: 80px;
      left: 30px;
      top: 0px;
      z-index: 5000;
      & > div.el-progress__text {
        color: #fff;
      }
    }
  }

  /*提示*/
  .shopBanner-tips {
    border: 1px solid #fbeed5;
    background: #fcf8e3;
    color: #c09853;
    margin: 10px auto;
    padding: 8px 35px 8px 14px;
    text-shadow: 0 1px 0 rgba(255, 255, 255, 0.5);
    text-align: left;
    p {
      margin: 4px 0;
    }
    .color-weight {
      color: red;
    }
  }

  /*轮播*/
  .el-carousel__item img {
    opacity: 0.75;
    width: 100%;
    height: 100%;
    margin: 0;
    object-fit: cover;
  }

  /*操作幻灯片*/
  .shopBanner-operaSlide {
    padding-top: 40px;
    border-top: 1px solid #eee;
    ul {
      display: flex;
      flex-wrap: nowrap;
      flex-direction: row;
      justify-content: space-around;
      align-items: center;
      li {
        list-style: none;
        div.slide-item {
          display: flex;
          flex-wrap: nowrap;
          flex-direction: column;
          justify-content: space-around;
          align-items: center;
          border: 1px solid #f5f5f5;
          padding: 10px;
          .del-btn-pos {
            margin: 5px auto;
            width: 160px;
            height: 20px;
            text-align: right;
            .del-btn {
              margin: 5px 0 0 0 ;
              padding: 0;
              cursor:pointer;
              width: 40px;
              height: 20px;
              background: white;
              border-radius: 4px;
              font-size: 12px;
              text-align: center;
              line-height: 20px;
              font-weight: 900;
              color: rgba(153, 153, 153, 0.64);
              border: 1px solid #ccc;
            }
            .del-btn:hover{
              color: red;
            }
          }
          div.img-show {
            background-color: #fff;
            border: 1px solid #f5f5f5;
            display: table-cell;
            height: 100px;
            line-height: 0;
            margin: 5px auto;
            overflow: hidden;
            text-align: center;
            vertical-align: middle;
            width: 160px;
            img {
              width: 100%;
              height: 100%;
              object-fit: cover;
            }
          }
          label {
            color: #777;
            font-size: 12px;
            height: 24px;
            line-height: 24px;
          }
          .slide-input-link {
            margin: 5px;
          }
        }
      }
    }
    /*保存幻灯片*/
    .save-btn {
      margin-top: 10px;
      width: 100%;
      text-align: center;
    }
  }
</style>
