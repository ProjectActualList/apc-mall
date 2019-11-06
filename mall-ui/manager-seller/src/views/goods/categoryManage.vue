<template>
  <div>
    <en-table-layout
      pagination
      ref="tableLayout"
      :tableData="tableData"
      :default-expand-all="true"
      :loading="loading">
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
          <el-button type="primary" @click="addGoodsCategory">新增商品分组</el-button>
        </div>
      </div>
      <template slot="table-columns">
        <el-table-column type="expand" width="0">
          <template slot-scope="scope">
            <en-table-layout
              v-if="scope.row.children && scope.row.children.length"
              :tableData="scope.row.children"
              :show-header="false"
              :toolbar="false"
              :pagination="false"
              :stripe="false"
              style="width: 100%">
              <template slot="table-columns">
                <el-table-column width="50">
                  <template slot-scope="scope">
                    <div class="expand">
                      <svg-icon iconClass="leftbotcorner" className="leftbotcorner-icon"/>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column prop="shop_cat_name"/>
                <el-table-column label="排序">
                  <template slot-scope="scope">{{ scope.row.sort || 0 }}</template>
                </el-table-column>
                <el-table-column>
                  <template slot-scope="scope">
                    <span class="showstatus showed" v-if="scope.row.disable === 1"><i class="el-icon-success"></i>已显示</span>
                    <span class="showstatus notshow" v-if="scope.row.disable === 0"><i class="el-icon-success"></i>未显示</span>
                  </template>
                </el-table-column>
                <!--操作-->
                <el-table-column>
                  <template slot-scope="scope">
                    <el-button
                      type="success"
                      @click="handleEditGoodsCategory(scope.$index, scope.row)">编辑
                    </el-button>
                    <el-button
                      type="danger"
                      @click="handleDeleteGoodsCategory(scope.row)">删除
                    </el-button>
                  </template>
                </el-table-column>
              </template>
            </en-table-layout>
          </template>
        </el-table-column>
        <el-table-column width="48">
          <template slot-scope="scope">
            <div @click="() => { handleToggleExpand(scope.row) }" class="expand">
              <i :class="['el-icon', scope.row.expanded ? 'el-icon-plus' : 'el-icon-minus']"></i>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="shop_cat_name" label="分组名称"/>
        <el-table-column prop="sort" label="排序"/>
        <el-table-column label="显示">
          <template slot-scope="scope">
            <span class="showstatus showed" v-if="scope.row.disable === 1"><i class="el-icon-success"></i>已显示</span>
            <span class="showstatus notshow" v-if="scope.row.disable === 0"><i class="el-icon-success"></i>未显示</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              type="success"
              @click="handleEditGoodsCategory(scope.$index, scope.row)">编辑
            </el-button>
            <el-button
              type="danger"
              @click="handleDeleteGoodsCategory(scope.row)">删除
            </el-button>
            <el-button
              type="primary"
              @click="handleAddSonCategory(scope.$index, scope.row)">新增下级
            </el-button>
          </template>
        </el-table-column>
      </template>
    </en-table-layout>
    <el-dialog :title="categorytitle" :visible.sync="goodsCategoryShow" width="25%">
      <el-form :model="goodsCatData" label-position="right" ref="goodsCatData" label-width="80px" :rules="rules" status-icon>
        <el-form-item label="分组名称" prop="category_name">
          <el-input v-model="goodsCatData.category_name" @change="() => { goodsCatData.category_name = goodsCatData.category_name.trim() }" maxlength="20" placeholder="限20字" style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="上级分组" >
          <el-select v-model="goodsCatData.category_parent" placeholder="请选择" style="width: 100%;">
            <el-option
              v-for="item in tableData"
              :key="item.shop_cat_id"
              :label="item.shop_cat_name"
              :value="item.shop_cat_id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="goodsCatData.sort" controls-position="right" :min="0" :max="99999"/>
        </el-form-item>
        <el-form-item label="显示状态">
          <el-radio-group v-model="goodsCatData.disable" style="width: 70%;">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="goodsCategoryShow = false">取 消</el-button>
        <el-button type="primary" @click="reserveCategoryGoods('goodsCatData')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import * as API_goodsCategory from '@/api/goodsCategory'
  import { RegExp } from '~/ui-utils'
  export default {
    name: 'categoryManage',
    data() {
      const checkSort = (rule, value, callback) => {
        if (!value && value !== 0) {
          return callback(new Error('排序不能为空'))
        }
        setTimeout(() => {
          if (!RegExp.integer.test(value) && value !== 0) {
            callback(new Error('请输入正整数'))
          } else {
            callback()
          }
        }, 500)
      }
      return {
        /** 列表loading状态 */
        loading: false,

        /** 列表数据 */
        tableData: null,

        /** 分组更新弹框标题*/
        categorytitle: '增加分组',

        /** 是否是编辑 1添加 2编辑 */
        is_edit: 1,

        /** 显示新增/编辑分组弹框*/
        goodsCategoryShow: false,

        /** 弹框数据*/
        goodsCatData: {
          category_name: '',
          category_parent: '',
          sort: 1,
          disable: 0
        },

        /** 商品分组*/
        categoryID: 0,

        /** 校验规则 */
        rules: {
          sort: [
            { validator: checkSort, trigger: 'blur' }
          ],
          category_name: [
            { required: true, message: '请填写分组名称', trigger: 'blur' }
          ]
        }
      }
    },
    mounted() {
      this.GET_GoodsCategoryList()
    },
    methods: {
      /** 添加展开属性 */
      add_expanded(data) {
        if (Array.isArray(data) && data.length > 0) {
          data.forEach(elem => {
            this.$set(elem, 'expanded', false)
            if (elem.children && elem.children.length > 0) {
              this.add_expanded(elem.children)
            }
          })
        }
      },

      /** 获取商品分组列表 */
      GET_GoodsCategoryList() {
        this.loading = true
        API_goodsCategory.getGoodsCategoryList().then(response => {
          this.loading = false
          this.tableData = response
          // 为分组数据增加展开状态
          this.add_expanded(this.tableData)
        })
      },

      /** 删除分组 */
      handleDeleteGoodsCategory(row) {
        this.$confirm('确认删除此分组, 是否继续?', '提示', { type: 'warning' }).then(() => {
          const _id = row.shop_cat_id
          API_goodsCategory.deleteGoodsCategory(_id).then(() => {
            this.GET_GoodsCategoryList()
            this.$message.success('删除分组成功！')
          })
        })
      },

      /** 新增商品分组 */
      addGoodsCategory() {
        this.is_edit = 1
        this.goodsCategoryShow = true
        this.categorytitle = '增加分组'
        this.goodsCatData = {
          category_name: '',
          category_parent: '',
          sort: '',
          disable: 1
        }
      },

      /** 编辑商品分组 */
      handleEditGoodsCategory($index, row) {
        this.is_edit = 2
        this.goodsCategoryShow = true
        this.categorytitle = '编辑分组'
        this.categoryID = row.shop_cat_id
        const _shop_cat_pid = row.shop_cat_pid === 0 ? '' : row.shop_cat_pid
        this.goodsCatData = {
          category_name: row.shop_cat_name,
          category_parent: _shop_cat_pid,
          sort: row.sort,
          disable: row.disable
        }
      },

      /** 新增商品下级分组 */
      handleAddSonCategory($index, row) {
        this.is_edit = 1
        this.goodsCategoryShow = true
        this.categorytitle = '增加子分组'
        this.goodsCatData = {
          category_name: '',
          category_parent: row.shop_cat_id,
          sort: '',
          disable: 1
        }
      },

      /** 保存商品分组 */
      reserveCategoryGoods(FormName) {
        this.$refs[FormName].validate((valid) => {
          if (valid) {
            let params = {}
            if (this.goodsCatData.category_parent) {
              params = {
                shop_cat_pid: this.goodsCatData.category_parent,
                shop_cat_name: this.goodsCatData.category_name,
                disable: this.goodsCatData.disable,
                sort: this.goodsCatData.sort
              }
            } else {
              params = {
                shop_cat_name: this.goodsCatData.category_name,
                disable: this.goodsCatData.disable,
                sort: this.goodsCatData.sort
              }
            }
            if (this.is_edit === 1) { // 添加
              API_goodsCategory.addGoodsCategory(params).then(() => {
                this.goodsCategoryShow = false
                this.$message.success('添加成功')
                this.GET_GoodsCategoryList()
              })
            } else { // 编辑
              API_goodsCategory.updateGoodsCategory(this.categoryID, params).then(() => {
                this.goodsCategoryShow = false
                this.$message.success('编辑成功')
                this.GET_GoodsCategoryList()
              })
            }
          }
        })
      },

      /** 展开/关闭 */
      handleToggleExpand(row) {
        if (row.children) {
          this.$refs['tableLayout'].$refs['table'].toggleRowExpansion(row)
          row.expanded = !row.expanded
          return
        }
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

  .goods-image {
    width: 50px;
    height: 50px;
  }

  /*扩展样式修正*/
  /deep/ .el-table__expanded-cell {
     padding: 0;
  }

  /* 控制状态是否已显示 */
  .showstatus {
    margin: 2px;
    padding: 2px;
    border-radius: 5px;
  }
  .showed {
    color: #5CB85C;
    border: 1px solid #5cb85c;
  }
  .notshow {
    color: #888;
    border: 1px solid #888;
  }


  /*展开样式控制*/
  .expand {
    width: 50px;
    i {
      cursor: pointer;
      padding: 2px;
      border: 1px solid #ddd;
    }
  }
  .expand_out {
    width: 48px;
    i {
      cursor: pointer;
      padding: 2px;
      border: 1px solid #ddd;
    }
  }
  .leftbotcorner-icon {
    margin-left: 20px;
  }

  /deep/ {
    .el-table__expanded-cell {
      border-bottom: none;
      padding: 0;
      .container {
        padding: 0;
      }
      .el-table::before {
        display: none;
      }
    }
    .el-table__expand-column {
      border-right: none;
    }
    .el-table__expand-icon {
      display: none;
    }
    .expand {
      cursor: pointer;
    }
  }
</style>
