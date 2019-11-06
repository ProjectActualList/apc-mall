<template>
  <div id="apply-shop">
    <apply-header/>
    <nuxt-child v-if="$route.name === 'shop-apply' || $route.name === 'shop-apply-success'"/>
    <div v-else class="apply-container w">
      <apply-menu :step="step"/>
      <div class="right-content">
        <apply-steps :step="step"/>
        <nuxt-child/>
      </div>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import { Checkbox, CheckboxGroup, DatePicker, Form, FormItem, Input, InputNumber, Upload } from 'element-ui'
  Vue.use(Checkbox).use(CheckboxGroup).use(DatePicker).use(Form).use(FormItem).use(Input).use(InputNumber).use(Upload)
  import applyHeader from './apply/-apply-header'
  import applyMenu from './apply/-apply-menu'
  import applySteps from './apply/-apply-steps'
  import { RegionPicker } from '@/components'
  Vue.component(RegionPicker.name, RegionPicker)

  export default {
    name: 'apply',
    middleware: 'auth-user',
    head() {
      return {
        title: `申请开店-${this.site.site_name}`
      }
    },
    components: { applyHeader, applyMenu, applySteps },
    computed: {
      step() {
        const { name } = this.$route
        switch (name) {
          case 'shop-apply-user-agreement':
            return 1
          case 'shop-apply-basic-info':
            return 2
          case 'shop-apply-auth-info':
            return 3
          case 'shop-apply-financial-qualification':
            return 4
          case 'shop-apply-shop-info':
            return 5
          default:
            return 1
        }
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /deep/ .item-container {
    padding: 20px;
    .title {
      text-align: left;
      font-size: 22px;
      padding-left: 35px;
      padding-bottom: 20px;
    }
    .content {
      display: flex;
      justify-content: center;
    }
  }
  /deep/ .next-btns {
    display: flex;
    align-items: center;
    justify-content: center;
    padding-top: 20px;
    border-top: 1px dashed #d7d7d7;
  }
  .apply-container {
    display: flex;
    justify-content: space-between;
    width: 1000px;
    padding-top: 30px;
    margin-bottom: 50px;
  }
  .right-content {
    width: 1000px - 210px -15px - 2px;
    min-height: 300px;
    border: 1px solid #D7D7D7;
  }
</style>
