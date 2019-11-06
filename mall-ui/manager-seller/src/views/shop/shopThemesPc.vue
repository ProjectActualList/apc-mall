<template>
  <div class="bg-out">
    <el-button type="primary" @click="lookshop">查看店铺</el-button>
    <span class="themes-tip">选择或者切换默认模版时，可在【商品管理】-【标签管理】对应标签里添加标签商品。</span>
    <hr/>
    <!--当前模板-->
    <p style="padding: 0 50px;">当前模板:</p>
    <div class="tpl-current-theme">
      <div class="themes-img choosed-image" v-if="tpl_current.image_path">
        <img :src="tpl_current.image_path" alt="" class="shop-theme-image">
      </div>
      <span class="tpl-name">模板名称:{{tpl_current.name}}</span>
    </div>
    <!--模板列表-->
    <hr/>
    <div>
      <p style="padding: 0 50px;">模板列表:</p>
      <ul>
        <li v-for="item in tpl_list">
          <div class="tpl-theme">
            <div class="themes-img"
                 :class='{"choosed-image":item.id === tpl_choosed_id}'
                 @click="chooseTheme(item)">
              <img v-if="item.image_path" :src="item.image_path" alt="" class="shop-theme-image">
            </div>
            <span class="tpl-name">模板名称:{{item.name}}</span>
            <span class="tpl-preview" @click="previewImg(item.image_path)">预览</span>
          </div>
        </li>
      </ul>
      <hr/>
      <div style="width: 100%;text-align: center;">
        <el-button type="primary" @click="POST_SaveCurrrentTpl">保存修改</el-button>
      </div>
    </div>
    <div class="zoom-img" :class="{'zoom-img-show': zoom_img_show }">
      <div class="close-btn" @click="closeZoom">&times;</div>
      <div class="zoom-bg-img">
        <img :src="zoom_imgsrc" alt="">
      </div>
    </div>
  </div>
</template>

<script>
  import * as API_ShopTheme from '@/api/shopTheme'
  export default {
    name: 'shopThemesPc',
    data() {
      return {
        /** 当前模板*/
        tpl_current: {},

        /** 当前选中的模板的id*/
        tpl_choosed_id: '',

        /** 模板列表*/
        tpl_list: [],

        /** 隐藏区域显示*/
        zoom_img_show: true,

        /** 被放大的图片*/
        zoom_imgsrc: '',

        /** 店铺信息 */
        shopInfo: this.$store.getters.shopInfo
      }
    },
    mounted() {
      this.GET_ShopThemesPc()
    },
    methods: {
      /** 查看店铺 */
      lookshop() {
        window.open(`${this.MixinBuyerDomain}/shop/${this.shopInfo.shop_id}`)
      },

      /** 获取PC店铺主题列表*/
      GET_ShopThemesPc() {
        API_ShopTheme.getShopThemeList({ type: 'PC' }).then(response => {
          this.tpl_list = response
          this.tpl_list.forEach(elem => {
            if (elem.current_use === 1) {
              this.tpl_current = elem
            }
          })
        })
      },

      /** 选择模板主题*/
      chooseTheme(item) {
        this.tpl_choosed_id = item.id
      },

      /** 预览*/
      previewImg(imgsrc) {
        this.zoom_imgsrc = imgsrc
        this.zoom_img_show = !this.zoom_img_show
      },

      /** 关闭放大图*/
      closeZoom() {
        this.zoom_img_show = !this.zoom_img_show
      },
      /** 保存PC店铺主题*/
      POST_SaveCurrrentTpl() {
        if (!this.tpl_choosed_id) {
          this.$message.warning('请先选择一种模板！')
          return
        }
        this.$confirm(`若切换模板，可在【商品管理】-【标签管理】对应标签里添加标签商品，是否确认切换？`, '确认信息')
          .then(() => {
            const _params = {}
            API_ShopTheme.saveShopTheme(this.tpl_choosed_id, _params).then(() => {
              this.$message.success('切换成功')
              this.GET_ShopThemesPc()
            })
          }).catch(() => {})
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /** 模版主题 */
  .themes-tip {
    display: inline-block;
    padding: 10px;
    background-color: #fcf8e3;
    border: 1px solid #fbeed5;
    font-size: 14px;
    color: #c09853;
  }
  /*被选中的模板图片*/
  .choosed-image {
    border: 1px solid #5cb85c;
  }

  /*最外层*/
  .bg-out {
    background: #ffffff;
    padding: 15px;
  }

  /*图片*/
  .shop-theme-image {
    width: 100%;
    max-width: 200px;
    min-height: 400px;
    cursor: pointer;
  }

  /*预览*/
  .tpl-preview {
    cursor: pointer;
    border: 1px solid #5cb85c !important;
  }

  /*模板主题*/
  ul {
    width: 80%;
    margin: 0 auto;
    padding: 10px 25px;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: space-between;
    align-items: center;
    li {
      list-style: none;
    }
  }

  .tpl-current-theme {
    width: 80%;
    margin: 0 auto;
    padding: 10px 25px;
    display: flex;
    flex-direction: column;
    flex-wrap: nowrap;
    justify-content: space-between;
    align-items: flex-start;
    .themes-img {
      max-width: 200px;
      max-height: 250px;
      overflow: hidden;
    }
    span {
      text-align: center;
      display: inline-block;
      margin-top: 15px;
      width: 100%;
      max-width: 200px;
      background-color: #fcf8e3;
      border: 1px solid #f9dbcc;
      height: 28px;
      line-height: 28px;
      font-size: 12px;
    }
  }

  .tpl-theme {
    display: flex;
    flex-direction: column;
    flex-wrap: nowrap;
    justify-content: space-between;
    align-items: stretch;
    .themes-img {
      max-width: 200px;
      max-height: 250px;
      overflow: hidden;
    }
    span {
      text-align: center;
      display: inline-block;
      margin-top: 15px;
      width: 100%;
      max-width: 200px;
      background-color: #fcf8e3;
      border: 1px solid #f9dbcc;
      height: 28px;
      line-height: 28px;
      font-size: 12px;
    }
  }

  /*隐藏区域*/
  .zoom-img-show {
    display: none;
  }

  .zoom-img {
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 2000;
    overflow: hidden;
    background: #000000;
  }

  /*关闭按钮*/
  .close-btn {
    position: fixed;
    right: 5px;
    top: 5px;
    background: #ffffff;
    width: 20px;
    height: 20px;
    border: none;
    border-radius: 50%;
    text-align: center;
    cursor: pointer;
  }

  .zoom-bg-img {
    margin: 0 auto;
    padding: 25px 0;
    width: 25%;
    min-height: 80%;
    text-align: center;
    img {
      width: 100%;
      height: 800px;
    }
  }
</style>
