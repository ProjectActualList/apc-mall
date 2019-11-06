<template>
  <div id="my-recommend">
    <nav-bar title="我推荐的人"/>
    <div class="recommend-container">
      <div class="my-refereer">我的推荐人：<span>{{ myRefereer || '没有推荐人' }}</span></div>
      <ul class="tree-wrap">
        <tree-list v-if="referee.length" :list="referee"></tree-list>
        <en-empty v-else>暂无数据</en-empty>
      </ul>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_distribution from '@/api/distribution'
  import treeList from './DisTree/tree-list.vue'

  export default {
    name: 'my-recommend',
    data() {
      return {
        /** 分页请求参数 */
        params: {
          page_no: 1,
          page_size: 10
        },

        /** 分页信息 */
        pageData: {
          page_no: 1,
          page_size: 10,
          data_total: 0
        },

        /** 我的推荐人 */
        myRefereer: '',

        /** 推荐的人列表 */
        referee: []
      }
    },
    mounted() {
      this.GET_myReferee()
      this.GET_referee()
    },
    components: {
      treeList
    },
    methods: {
      /** 我的推荐人 */
      GET_myReferee() {
        API_distribution.getMyRefereer().then(response => {
          this.myRefereer = response.message
        })
      },

      /** 获取推荐人 */
      GET_referee() {
        API_distribution.getRefereeList().then(response => {
          this.referee = response
        })
      },

      /** 当前页数发生改变 */
      handleCurrentPageChange(cur) {
        this.params.page_no = cur
        this.GET_referee()
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .recommend-container {
    padding-top: 46px;
    .my-refereer {
      padding-left: 25px;
      font-size: 14px;
      margin: 15px 0;
    }
  }
</style>
