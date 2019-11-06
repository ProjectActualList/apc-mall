<template>
  <div id="user-agreement" class="item-container">
    <h2 class="title" style="text-align: center">入驻协议</h2>
    <div class="content-box" v-html="agreement"></div>
    <div class="agreed">
      <el-checkbox v-model="agreed">我已阅读并同意以上协议</el-checkbox>
    </div>
    <div class="next-btn">
      <el-button size="small" @click="handleNextStep">下一步</el-button>
    </div>
  </div>
</template>

<script>
  import * as API_Shop from '@/api/shop'
  import * as API_Article from '@/api/article'
  import mixin from './checkStatusMixin'
  export default {
    name: "user-agreement",
    mixins: [mixin],
    middleware: 'auth-seller',
    async asyncData() {
      try {
        const agreement = await API_Article.getArticleByPosition('COOPERATION_AGREEMENT')
        return { agreement: agreement.content }
      } catch (e) {
        return { agreement: '入驻协议获取失败...' }
      }
    },
    data() {
      return { agreed: false }
    },
    methods: {
      handleNextStep() {
        if (this.agreed) {
          API_Shop.initApplyShop().then(response => {
            this.$router.push({ name: 'shop-apply-basic-info' })
          })
        } else {
          this.$message.error('请阅读并同意店铺入驻协议！')
        }
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .content-box {
    border: 1px solid #d7d7d7;
    padding: 10px;
    line-height: 20px;
    max-height: 300px;
    overflow-y: scroll;
  }
  .agreed {
    text-align: center;
    padding: 20px 0;
  }
  .next-btn {
    text-align: center;
    padding: 10px 0;
  }
</style>
