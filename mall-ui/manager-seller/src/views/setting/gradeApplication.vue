<template>
  <div class="bg-settings">
    <h4>店铺等级</h4>
    <p>当前店铺等级为 {{ shopGradeData.shop_grade }}级，存储空间容量为{{ shopGradeData.current_space_capacity }}M</p>
    <p>提升店铺等级可增加空间容量为 {{ shopGradeData.next_space_capacity }} M</p>
    <div v-if="shopGradeData.is_promote">申请已提交请耐心等待</div>
    <el-button v-else type="success" @click="handlePromoteGrade">提升等级</el-button>
  </div>
</template>

<script>
  import * as API_Shop from '@/api/shop'
  export default {
    name: 'gradeApplication',
    data() {
      return {
        /** 店铺等级信息*/
        shopGradeData: {}
      }
    },
    mounted() {
      this.GET_ShopGradeData()
    },
    methods: {
      /** 获取店铺等级信息 */
      // GET_ShopGradeData() {
      //   API_Shop.getShopGradeData().then(response => {
      //     this.shopGradeData = response
      //   }).catch(error => {
      //     console.log(error)
      //   })
      // },

      /** 提升等级*/
      handlePromoteGrade() {
        // API_Shop.getShopGradeData(this.shopGradeData.shop_id, this.shopGradeData).then(response => {
        //   this.$message.success('申请成功，请耐心等待！')
        //   this.GET_ShopGradeData()
        // }).catch(error => {
        //   this.$message.success('申请失败，请稍后重试！')
        //   console.log(error)
        // })
      }
    }
  }
</script>

<style lang="scss" type="text/scss" scoped>
  .bg-settings {
    background: #fff;
    border: 1px solid #FAFAFA;
    margin: 15px;
    padding: 10px;
    p {
      color: #449d44;
      font-size: 14px;
    }
  }
</style>
