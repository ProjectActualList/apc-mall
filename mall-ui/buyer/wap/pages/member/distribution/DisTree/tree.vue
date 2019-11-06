<template>
  <li class="node list-item" data-type="0">
    <template v-if="root !==0 && nodes !== 1">
      <span class="line"></span>
    </template>
    <div class="contanier flex">
      <div class="small-panel flex">
        <span class="label">[推荐人数]</span>
        <span class="txt">{{model.downline}}</span>
      </div>
      <div class="body-panel flex">
        <div class="title">
          <i v-if="HasChild" class="arrow" :class="isOpenOrClose" @click="openExpand(model)"></i>
          <i class="t-icon m-dep"></i>
          {{model.name}}
        </div>
        <div class="time" @click="handlelookDetails(model)">详情</div>
      </div>
      <div class="bottom-panel flex">
        <div class="left-wrap flex">
          <span class="label">[返利]</span>|<span class="txt color-red">{{model.rebate_total | unitPrice('¥') }}</span>
        </div>
      </div>
    </div>
    <template v-if="HasChild && this.model.isExpand">
      <ul class="box">
        <tree-item v-for="(m, i) in model.item" :key="String('child_node'+i)" :num='i' :root="1" @openExpand="openExpand" @delAction="delAction" :nodes.sync="model.item.length" :model.sync="m"></tree-item>
      </ul>
    </template>
  </li>
</template>

<script>
export default {
  name: 'treeItem',
  props: ['model', 'num', 'nodes', 'root'],
  data() {
    return {
      treeDataSource: []
    }
  },
  watch: {
    model: {
      handler() {
        // console.log('监听子组件：', this.model)
      },
      deep: true
    }
  },
  computed: {
    HasChild() {
      return this.model.item.length > 0
    },
    isOpenOrClose() {
      return this.model.isExpand ? 'open' : 'close'
    }
  },
  methods: {
    openExpand(m) {
      this.$emit('openExpand', m)
    },
    //删除
    delAction(m) {
      this.$emit('delAction', m)
    },
    handlelookDetails(m) {
      this.$router.push({path: '/member/distribution/my-performance', query: {member_id: m.id}})
    }
  }
}
</script>

<style type="text/scss" lang="scss">
  .color-red{
    color: #f42424;
  }
  .flex {
  display: flex;
  justify-content: flex-start;
}
  .contanier {
  padding-left: 26px;
  padding-right: 10px;
  flex-direction: column;
  .small-panel {
    margin-top: 5px;
  }
  .small-panel,
  .bottom-panel {
    flex-direction: row;
    height: 20px;
    font-size: 8px;
    color: #333;
  }
  .body-panel {
    height: 24px;
    flex-direction: row;
    justify-content: space-between;
    .title {
      position: relative;
      .arrow {
        position: absolute;
        width: 0;
        height: 0;
        left: -13px;
        border-style: solid;
        z-index: 1;
        &.close {
          border-width: 6px 0 6px 6px;
          border-color: transparent transparent transparent #aaa;
        }
        &.open {
          top: 4px;
          border-width: 6px 6px 0;
          border-color: #aaa transparent transparent;
        }
      }
    }
    .time {
      font-size: 14px;
      color: #ff853f;
    }
    .t-icon {
      display: inline-block;
      width: 18px;
      height: 18px;
      background-size: 100% 100% !important;
      vertical-align: middle;
      margin: -3px 2px 0 0;
      &.m-dep {
        background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAEa0lEQVRYR8WXbYhUZRTH//9xRGYyw+xDm4YLCRViQWJB9EYipVIEplaIHwLrQzTSuPd5ZnbZvEkv9z53WmFLaEtKiCIy+lCESUKkfojQrKAPkoJURiaWW+7MtrvznHiGOzFuOy/XXeh8unDP+Z/fPed5OZf4n41J8g8NDc0eHh5+SETWArgFwHUkL4jICZIHAexVSn2dRLNjAGPMZhEJSHa1SCAisjuTyfTkcrk/OwHpCCCKou0i4tcFRWQYgPvSYySvBHATgGUAZjsfETkFYL3W+kg7iLYAYRgWSb4YC4+S7Pc872WS0iheKpWusta+BmBd7HtuYmJiaV9f35lWEC0BSqXSCmvtV7HAaWvtfYVC4ftWgkEQbEmlUq/HPgeUUqsuGcAY4xbWnSJiZ82adWtPT8/RdiV1740xLwDodc8kH/Q87+NmcU0rsHPnzq7x8fFf4nK+r7Xe2Ely5+P7fjqbzZ4AsFhEPtRa19oylTUFMMY8CuBdF2StXVMoFPZ1CuD8wjB06yQvImWl1NzJa6au1QqgB0DkHNPp9IJ8Pv97EgBjzBMAhuL4Rfl8/nTSCrgeul6iXC7P9n1/IglAFEWbROTtOGaJUupkUoB/WyAiN2itjycBCMOwn+QOANXu7u45GzZsqCYCKJVKy621tYNERJTWutaOTs0Y47bvCgDfKaVuTrwIRYRRFP3gznsROVetVm/s7e092wlAEAR3pVKpL2LfnFLqlcQALsAY8xSAV+MqHK5UKqt83x9tBTE4ODivUqkcJbkEwPlyubzQ9/3yJQHEt99nAO6OIY6TfKzZjRcEwTKSe0le73oPYKVSql6JZOdA3Tv+osMk3WXj1oMl+QmAIySPAfjLWjuP5D0i8jTJtEsuIlu01m+1a1nby8gJRFF0mbV2D8mH2wmKyFmS65RSh9r5uvcdAQwMDGSq1epma+0AyWwrYRG5AGBPOp0227Zt+6kdRFuAIAjWknxjikHkPIBTbgIB0E1y/qRk5621jxQKhf2tIFoCGGOeA/BsXUBEvgXgttR+rfXPjcJhGC4SkdUkt5Jc2rBetiil3ky8CxqvVBFxg8hWz/NcJS4aRCYL+76fymazT4pISPLy+H2fUqo21Ey2KSsQRdEDIvJR7PxrtVpdUywW3Yrv2MIwvIak01geB7lK7G4LEATBFSRPklwAoALgdqXUNx1nbnB0M8XY2Jg7lLpExG3XxcVi8Y9Grf9UwBgzAOCZuIePd7KXW8E1VlNEXtJa1yalul0EMDg4OGd0dPQ3APNE5KDWunYCTtfCMDxE8g53p2QymYW5XO7vKQGMMfcDqE0+IrJJa/3OdJO7+EmzwWql1KdTAoRhuMON3S6/tXZ+oVBw8/+0bdeuXXNHRkbcj4qreEkp5TWrwHsANorIGa311dPO3CBgjPkRwLUi8oHWen2zChwgudL99Sil6ttnRjiMMV8CuE1EPtda39sMoDZGkfQ9z3On4IxZGIY+ye0A+pVSz08JMGPZEgj9Ax+gBD/Qa+wWAAAAAElFTkSuQmCC);
      }
    }
  }
}
</style>
