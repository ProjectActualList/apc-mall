<template>
  <div id="my-points">
    <nav-bar title="我的积分"></nav-bar>
    <van-tabs v-model="tabActive" :line-width="100">
      <van-tab title="我的积分"/>
      <van-tab title="积分明细"/>
    </van-tabs>
    <div class="points-container">
      <van-cell-group v-if="tabActive === 0" :border="false">
        <van-cell title="消费积分" :value="points.consum_point || 0"/>
        <van-cell title="等级积分" :value="points.grade_point || 0"/>
      </van-cell-group>
      <template v-else>
        <empty-member v-if="finished && !pointsList.length">暂无收藏的店铺</empty-member>
        <van-list
          v-else
          v-model="loading"
          :finished="finished"
          @load="onLoad"
        >
          <van-cell-group :border="false">
            <van-cell
              v-for="(point, index) in pointsList"
              :key="index"
              :title="f.unixToDate(point.time)"
              :label="point.reason"
              :value="getPointValue(point)"
            />
          </van-cell-group>
        </van-list>
      </template>
    </div>
  </div>
</template>

<script>
  import * as API_Members from '@/api/members'
  import { Foundation } from '@/ui-utils'
  export default {
    name: 'my-points',
    head() {
      return {
        title: `我的积分${this.site.site_name}`
      }
    },
    data() {
      return {
        f: Foundation,
        tabActive: this.$route.query.type === 'detail' ? 1 : 0,
        points: {
          consum_point: '获取中...',
          grade_point: '获取中...'
        },
        params: {
          page_no: 1,
          page_size: 10
        },
        pointsList: [],
        // 积分明细加载中
        loading: false,
        // 积分明细是否加载完成
        finished: false
      }
    },
    mounted() {
      this.tabActive === 0
        ? this.GET_Points()
        : this.GET_PointsData()
    },
    watch: {
      tabActive(newVal) {
        if (newVal === 0 && this.points.consum_point === '获取中...') {
          this.GET_Points()
        }
        if (newVal === 1 && !this.pointsList.length) {
          this.GET_PointsData()
        }
      }
    },
    methods: {
      /** 加载积分明细 */
      onLoad() {
        this.params.page_no += 1
        this.GET_PointsData()
      },
      /** 获取积分明细值 */
      getPointValue(point) {
        const g_p = point.grade_point_type === 0 ? `-${point.grade_point}` : `+${point.grade_point}`
        const c_p = point.consum_point_type === 0 ? `-${point.consum_point}` : `+${point.consum_point}`
        return `消费：${point.grade_point === 0 ? 0 : g_p}  等级：${point.consum_point === 0 ? 0 : c_p}`
      },
      /** 获取积分明细 */
      GET_PointsData() {
        this.loading = true
        API_Members.getPointsData(this.params).then(response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            this.pointsList.push(...data)
          }
        })
      },
      /** 获取当前会员的积分 */
      GET_Points() {
        API_Members.getPoints().then(response => {
          this.points = response
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .points-container {
    /deep/ .cell { font-size: 12px }
    /deep/ .el-pagination {
      text-align: right;
      margin-top: 15px;
    }
  }
  .points-my /deep/ .el-alert--warning {
    h2 {
      font-size: 18px;
      margin-bottom: 10px;
    }
    p {
      margin-bottom: 5px;
    }
  }
  .points-my /deep/ .el-alert--info {
    margin-top: 15px;
    h2 {
      font-size: 16px;
      margin-bottom: 10px;
    }
    p {
      margin-bottom: 5px;
    }
  }
</style>
