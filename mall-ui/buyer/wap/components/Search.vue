<template>
  <van-popup
    v-model="show"
    position="bottom"
    close-on-click-overlay
    @click-overlay="$emit('close')"
    style="width: 100%;height: 100%;background-color: #f3f3f3;"
  >
    <div class="control-search">
      <div class="nav-control-search">
        <div class="inner">
          <i class="close-control-search" @click="$emit('close')"></i>
          <div class="input-control-search">
            <form class="search-form" action="/" @submit.prevent="handleOnSubmit">
              <div class="input-box">
                <span class="search-type show-search-type-box" @click.stop="showSearchType = !showSearchType">{{ searchType }}</span>
                <i class="arrow-down show-search-type-box" @click.stop="showSearchType = !showSearchType"></i>
                <transition name="fade">
                  <div v-if="showSearchType" class="search-type-box">
                    <ul>
                      <li class="item" @click="searchType = '商品'"><i class="icon-search-type type-goods"></i>商品</li>
                      <li class="item" @click="searchType = '店铺'"><i class="icon-search-type type-store"></i>店铺</li>
                    </ul>
                  </div>
                </transition>
                <input type="search" v-model="keyword" class="search-input" placeholder="搜索商品/店铺" autofocus="autofocus">
                <i class="search-icon"></i>
                <input type="submit" class="search-submit">
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
    <div v-if="searchType === '商品' && autoCompleteStr &&  autoCompleteData.length" class="auto-complete">
      <ul>
        <li v-for="(word, index) in autoCompleteData" :key="index" @click="handleOnSubmit(word.words)">
          <span>{{ word.words }}</span>
          <span>约{{ word.goods_num }}个商品</span>
        </li>
      </ul>
    </div>
    <template v-else>
      <div class="hot-keyword-search">
        <div class="title-hot-keyword">
          <span>大家都在搜</span>
        </div>
        <div class="content-hot-keyword">
          <ul class="key-words">
            <li
              v-for="keyword in hot_keywords"
              :key="keyword.id"
              class="item"
              @click="handleOnSubmit(keyword.hot_name)"
            >{{ keyword.hot_name }}</li>
          </ul>
        </div>
      </div>
      <div class="history-search">
        <template v-if="searchHistory && searchHistory.length">
          <ul>
            <li v-for="(his, index) in searchHistory" :key="his" class="his-item">
              <span @click="handleOnSubmit(his)">{{ his }}</span>
              <i @click="handleDeleteHistory(index)" class="icon-close"></i>
            </li>
          </ul>
          <van-button size="small" class="clear-his-btn" @click="handleClearHistory">清空搜索历史</van-button>
        </template>
        <div v-else style="width: 100%;text-align: center;line-height: 100px">暂无搜索记录...</div>
      </div>
    </template>
  </van-popup>
</template>

<script>
  import * as API_Home from '@/api/home'
  import * as API_Goods from '@/api/goods'
  import Storage from '@/utils/storage'

  export default {
    name: 'Search',
    props: ['show'],
    data() {
      return {
        keyword: this.$route.query.keyword || '',
        hot_keywords: [],
        // 显示搜索类型
        showSearchType: false,
        // 搜索类型
        searchType: '商品',
        // 搜索历史
        searchHistory: [],
        // 自动补全数据
        autoCompleteData: '',
        // 自动补全字符串
        autoCompleteStr: ''
      }
    },
    watch: {
      keyword: 'handleSearchChange'
    },
    mounted() {
      /** 获取热门关键词 */
      API_Home.getHotKeywords().then(response => this.hot_keywords = response)
      this.$nextTick(() => {
        document.querySelector('body').addEventListener('click', this.handleCloseSearcyType);
      })
      // 初始化搜索历史
      const searchHistory = Storage.getItem('search_history')
      if(searchHistory) this.searchHistory = JSON.parse(searchHistory)
    },
    methods: {
      /** 隐藏搜索类型盒子 */
      handleCloseSearcyType() {
        this.showSearchType = false
      },
      /** 提交表单之拦截 */
      handleOnSubmit(_keyword) {
        let { searchType, keyword } = this
        if (typeof _keyword === 'string') keyword = _keyword
        if (searchType === '商品') {
          const searchHistory = JSON.parse(JSON.stringify(this.searchHistory))
          const _index = searchHistory.indexOf(keyword)
          if (_index !== -1) {
            searchHistory.splice(_index, 1)
          } else if (searchHistory.length > 9) {
            searchHistory.pop()
          }
          if (keyword.trim()) searchHistory.splice(0, 0, keyword)
          this.$set(this, 'searchHistory', searchHistory)
          Storage.setItem('search_history', JSON.stringify(searchHistory))
          window.location.href = keyword ? `/goods?keyword=${keyword}` : '/goods'
        } else {
          window.location.href = `/shop?keyword=${keyword}`
        }
      },
      /** 删除搜索历史 */
      handleDeleteHistory(index) {
        this.searchHistory.splice(index, 1)
        Storage.setItem('search_history', JSON.stringify(this.searchHistory))
      },
      /** 清空搜索历史 */
      handleClearHistory() {
        this.$confirm('确定要清空搜索历史吗？', () => {
          this.$set(this, 'searchHistory', [])
          Storage.removeItem('search_history')
        })
      },
      /** 搜索关键字发生改变 */
      handleSearchChange() {
        let _str = this.keyword
        if (this.searchType !== '商品') return
        if (!_str) {
          this.autoCompleteData = []
          return
        }
        if (_str === this.autoCompleteStr) return
        this.autoCompleteStr = _str
        _str = _str.trim()
        API_Goods.getKeywordNum(_str).then(response => {
          this.autoCompleteData = response
        })
      }
    },
    destroyed () {
      document.removeEventListener('click', this.handleCloseSearcyType)
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .control-search {
    width: 100%;
  }
  .nav-control-search {
    width: 100%;
    height: 45px;
    background-color: #f3f3f3;
  }
  .control-search .nav-control-search:after {
    content: '';
    position: absolute;
    top: 45px;
    width: 100%;
    height: 1px;
    background-color: #ccc;
  }
  .control-search .nav-control-search .inner {
    display: flex;
    align-items: center;
    width: 100%;
    height: 100%;
  }
  .control-search .close-control-search {
    display: block;
    width: 10.66vw;
    height: 100%;
    background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAACLElEQVR4Xu3a4U3EMAwFYHsDNkBsABMAE5DbADZgA8QksEG9AYzACGwATBCU01Uqp95dndrPVE3/nISipO+LU1GrTCu/eOX5qQG0Cli5QDsCKy+A9hBsR6AdgZULtCOgLYCU0hkRvRBR+X0WkXftHJbjU0qXRPS0m7Pcz4dmfnUFbDYbIaK7fpGc84OIvGoWtRq7C//GzGUzKOf8KSIXmvlrAMqOXw8XiUDYD7+7n5+u67YYUy81QEopMXO3vwAS4UD4UgHqalQDlOAppXtmLs+BP1fNDUzdqX6cZfgyZxVAFIJ1+FkAaASP8LMBUAhe4U0AvBE8w5sBeCF4hzcFsEZAhDcHsEJAhXcBmIuADO8GUIuADu8KoEWICO8OMBUhKjwE4BQCEX0SUde/0vb/8yPeK2AAxxDGXoZQ4aEAUxGQ4eEApxDQ4f8jwJW2pzd2hDR/q+4HaBbpxx5qpAwefN9EdItEgAGcCh+FAAE41kLbnsO99lrOGVYJ7gBT+odjY1AIrgBTwh97PiAQ3AA04SMRXABqwkchmAPMCR+BYApgER6NYAZgGR6JYALgER6FMBvAMzwCYRYAIrw3QjUAMrwnQhVARHgvBDVAZHgPhBqAr6gG5rAHceAFCvKNUOninqO7t2MNmBEEyDdCN8y8/Sos5/woIuWrsbBriFDTU1QfgbCkTgs3ACfYxUzbKmAxW+V0o60CnGAXM22rgMVsldONtgpwgl3MtKuvgF+ypE1fyEWN7wAAAABJRU5ErkJggg==') no-repeat center;
    background-size: 60%;
  }
  .control-search .input-control-search {
    position: relative;
    width: 89.34vw;
    height: 100%;
  }
  .control-search .input-box {
    display: flex;
    position: relative;
    width: 98%;
    height: 66%;
    border: 1px solid #bfbfbf;
    border-radius: 3px;
    margin-top: 1.8%;
    background-color: #fff;
  }
  .control-search .search-type {
    position: relative;
    width: 10%;
    min-width: 30px;
    background-color: #f3f3f3;
    text-align: right;
    font-size: 14px;
    line-height: 33px;
    overflow: hidden;
  }
  .control-search .arrow-down {
    position: relative;
    display: block;
    width: 3%;
    height: 33px;
    background: #f3f3f3 url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAB+UlEQVR4Xu2ZW1HEQBBF7yoAB4ACwAESQAHgoFEAKOA6ABSABBzAKmBxAA6oqQpVWTaPSTKPTqbnM1uppM/c7jNbWaHwtSq8fhgAS0DhBKwFCg+ADUFrAWuBwglYCxQeALOAtYC1QOEErAUKD4BZYKcFROQKwAOA/YWl4xvADcmnel1NAD4BHC6s+L9yNiSP+gBsABwsFMAXya3NbUrAOYCXhQK4IPnamQD3o4jcAbhdGIR7kq6urdV6DhARNywuFwLhmaQb7jur8yAkIh8AjmcOYU3ypK2GPgBOhW8zhrAGcEbSKbBx9R6FRcTRcxD2ZpaEn6p4l+LW1QugGooOwvvMAJyS7Cze1eMFoILghsjjTCBc/z/xjW6B+o0z0WOj7oIAqJKgWY+tugsGoIKgUY+dugsNQJsee3UXFEDNDBr06KW74AAU6dFLd1EAKNCjt+6iAagg5Pj3OEh3UQFk0ONg3UUHkFCPo3SXCkBsPY7WXRIAkfU4SXfJAETU4yTdJQUQQY+TdZccQEA9BtFdFgAB9BhMd9kATNBjUN3lBjBUj8F1lxXAQD1G0V12AAP0GEV3KgB46DGa7tQA6NBjVN2pAtCgx+i6UwegNhPg8wGjrYCp170/jEx9kNb7DYDWnUn1XpaAVKS1PscSoHVnUr2XJSAVaa3P+QXewLdBt8in9wAAAABJRU5ErkJggg==') no-repeat center;
    background-size: 8px;
    border-right: 1px solid #bfbfbf;
    padding: 0 2px;
  }
  .search-input {
    border: none;
    outline: none;
    background-color: #fff;
    width: 87%;
    padding-left: 2%;
    height: 33px;
    font-size: 16px;
    tap-highlight-color:rgba(0,0,0,0);
    -webkit-tap-highlight-color:rgba(0,0,0,0);
  }
  .search-icon, .search-submit {
    position: absolute;
    right: 0;
    top: 0;
    width: 11%;
    height: 100%;
  }
  .search-icon {
    background: #f3f3f3 url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAEuElEQVR4Xu2agZEUNxBFeyIwRGCIwBABJgJDBJgI3BuBIYJtR2CIAIiAIwKTgY8IfBmM67k0LtEnjaQZ7e7c7qnqautu5zTS0+9udUuDXHgbLnz+cg/gXgEHJKCqj4ZheDaO4xMRmX4euFfeiMhXfoZhuB7H8YuZ8ftRWncTUFUm+kpEXojIo4WzAMo7EXl/aBjdAKjqzyLyu4jw2bNdDcPwbr/fv+/Z6dTXagBhxfcHmLifL2axM7OrniBWAVBVJq6FAX0KNs7Ab7yk8RPBVFAO5sPnDzN9moi8NTPMZHVbBCAM+kMYcGoQX7BhM8OOm5uq4j9+FZFfMv+MGl738A/NAILkP4uI9+aMldV+02NgdBZAvwlO1bNAAc/XvqsJwMzkv7Five1zmnFwsEj/p4QiUMIipdFXNYCZybPqTL6LTebsRVVRHBAIsb49XaqEKgBBin8lZP+HmZWcYLMPmPsHVcU3/OmeWWwOtQCYPB46bqukt4ZKBgKOEZ/QpMQiAFXFCbHBidvRV94Dy0BoHtcsgCD9v93LP5kZYerkTVVxft4noILqzVIJAOEu3tri7Z+0yuxQpIJjZLJxdPhqZk9r35kFEEIPAOL20sw+1nZ+jOcy46z2T3MA/OqTpvZOdLowSpjCtZk9ruk8CSBj+022VfPyXs9kxlu1N8gB8J5/s6s/QUyogFoCe4bZlgOA54+LGdU2VXrhob4PCRQJ2tTIPB+W3ncLQEZOD7fi+ecmpKpsguJUumgGKQB+q7l5+UdmQISKU2gKKOQP2ZYC4O2f4gN/23xTVfISijRTK/qBFAA2Fs+iTjYX+3MrkdgTFNVbA2Cz4c+DSPkvM5vd7aYA+Ajw2MyuN6//MEBVHeOxLgHQ1MHWwNwD6KAA5P5jtLJ3xgRCdvjPWhPwUeAuOUGStTiD7RIFLi4MXvxG6C5vhb35FpO4s0mGUg5QRIoOPJcOU2KO62xFkqfeDyTS4W9mVryfUFsQuTKz56eeZCEV9iW8qhL5uZTEfPiDVVH+PDRXFPUOZbMqUNXFBdxzLYtXb95KByNeBZSckFbT+duhfEfw/Jxbxs6uuPuLx1MCQMf+aGwzpqCqFEH9MV2xDlgNgAczh6Ncf3l9qJWt6TczrubyXfF0OEDwpsCfT7Y3yByHNUl/glwLgNsZpMn+9tZJlKCqVHp/c0ppkn4TgKACLkigBA+BUjRqOJpjPOoFiZhyuCeUgoA6qMEf7eQ4czeg+ZZIlQlUQuAx4OCIqi8oFLa3RCGOuJLq6gGhGUAwB3wCqx2fH8Rz+e9+7ziO3CZpNo3dbvdqHEfC2xTisg5XVf1pEOP4aGYva6LJIgBTx5lQ5N+LGgCCPG/2+z23SP9vu90OiA+iK/Wp6zfZg87MLRH6r3LQqwAENSBT7urk1FCzEKVnZlPbNRBWA4jUQEZGOa03CBSjpYuQSyF0AxCBQBEcUiLluLxeWuX4ey5jYdsc7lafSgUIqf1K1od0B+AiBjBQxvTJ114hk0/ARzB4HFj1pD3VmVCdhHBQAC1L3vPZFghnCWBm53ormpwtgAABPxTfG8KpfDfnswYQIHDOQfJEDnMrXT57ACXfcg+gROjcv794BfwLf9NFXzgATGIAAAAASUVORK5CYII=') no-repeat center;
    background-size: 50%;
    display: block;
    z-index: 102;
    border-left: 1px solid #bfbfbf;
  }
  .search-submit {
    z-index: 103;
    opacity: 0;
    outline: none;
    border: 0;
  }
  .search-type-box {
    position: absolute;
    left: 0;
    top: 33px;
    z-index: 101;
    padding: 10px;
    -webkit-transition: all .3s ease-out;
    transition: all .3s ease-out;
  }
  .search-type-box ul {
    background: #484d50;
    border-radius: 6px;
    width: 140px;
    overflow: hidden;
    color: #c7c8c9;
    &:after {
      position: absolute;
      left: 20px;
      top: 5px;
      content: '';
      display: inline-block;
      width: 10px;
      height: 10px;
      background: #484d50;
      -webkit-transform: rotate(45deg);
    }
  }
  .search-type-box li {
    border-top: 1px solid #707476;
    margin-top: -1px;
    height: 35px;
    line-height: 35px;
  }

  .icon-search-type {
    float: left;
    position: relative;
    width: 40px;
    height: 35px;
    &.type-goods {
      background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAFNklEQVR4Xu1bTXrbNhCdoVtTXtU9QZUTVFrUJLWJc4KoJ4hzA+UEkU8Q9QSxT1DlBFE3AqkuFJ0gygli79J8MacfqB+LIEEAJCjRn82dPong4GEw782DiPDIL3zk84cnAJ4yYE8IMMba4DjPibCDEHdWj8VzArhBoE+rjzg5Qvxwdna2+ryHq/YtEIbhSwIY8MnqzicBBWEEcXwdBMFS974yv6sNAMb+PQe8ew+A7TKBbe4hwNGJ+/Nlt9u9qTKO7N5aAJiGs3cINLAXMC2Bjl4HwR8Te2OuN6HtAVkY8lW/kIy7AIIxgJNMhE9oPp+ffvv2owNAbQA6B4Q+APySez/h6yA4u7IZs9UMYGH4MXevE1wDxEPd/czY7IIgHiLib5nJWgbBGgCMRUNAeCsEvAByBmVTVzImADkvyo4pAmoFgNls1rmLaS5OvuUen1ctXjwbAGmU3ha0bLlut+rYCfPa2E/TMBwj4Mtt5Sb6ctJyOzYC5GOuGCX+uBsrAf3V8/3KhbYyAInAQedzCkiLKboZV9wOXCv0fO/XqgtYGYBpGA4Q8N1OIIvA99ZKr2p46funLFymCqOFglgZAMaiCSA834ZqISgZbNksoA893+e0WfqqAQB7FVqcVaYWEPwTBJ62xM5DqToAYUS7Awe+V3lM2XImoum/71+3xdZCHagcrLAvbwPfOy2djxo3sjDiPUGiFInoSy/wK/UalQHgaUlwd4WIp0BO35ZAkdeBrS7ggqjy8yoDoLFojf7JEwCq5VkJHXxLgH0EqHV/q2LR/X7tMo2B6FLVgBVmwFqHv9d9cCN/p9AlUgAkDU4j56gK6sjBrsxnlALAWHQFCK9Ugz+I7wmug8DLNWnkAITh55SfV6PEtQ1itj+hZeD7z4yUINujwrMNAB9PN/6CDIi4N//7fZNTn8a3DUCOfyDtUItqwN66PPsAJGpxy14E8q6xCIC0x0dwGQTeMC9Yzhg/YuJ+YNKZIdC45bpv7DlCrE3ocM9h0/lNkOI3Mo7PeIkFsRcAkEYRJK2njC65GDlxj59VBYELMUJnLoqwROxQ3M0DQfQoCOHPnueNzYpgxofLr6RSK5x3axZ8O9Fv3J2ELLWZwGCldIBuJRWrbQplC4bFNIy+yiW4bFH0PYpCKZz14LJMUDcAu/2/mMJ5foDIACrPQNELpP2+vL1UlKJQUHx0K79i/IzCy/QviixUAaBkgnUR5Gd94nnews7BSGK7c00ijn/bco/bYpE1YYAVYxVcIprSopNUahwBQQcRbwhocuK6w6oMsAltdYD6fUT88DQJGiet1vEgb3xTl1oBQPpEhgA+9Xyvq5u+h/jdNIw4Zd6fSygOaZSOkK6mPsRk855pGq8SAJEJijj10CCIokzFAMoakGgB4eSnSFUdGoBpFPWR4O/7Bk59cKLMANOqekgQysSqAYB+Z3XIyfNnZzSDhomjAcDDYQJTBtCqAbo9waFXv2ycygxIUks4l28iE5RhAP0MeABMUIYBTABQ9gSH3gJlGMAAgOYzQRkGMACg+UxQhgG0AShbYfe5LUx7gE1sWizQdCYoywBmGdBgJijLAKYACP8Fpkng+y/2meayZ2WcaQMrTnsL5Pn/BDD+ycHLfb7isgvC5kAGIfmL/fYyEWraAOS1xk1Y/UwMhla8IQBSg7IpWNwCxR3V32J2gzUCgN+43gr8rY37k+NmTH9x5OCF6XY0BmAz1+StDoz7uHobLP8Vl/qBueUONJIzLvsqTWkA6p/bfp7wBMB+cG7uUx59BvwPZzESbjOhxhQAAAAASUVORK5CYII=') no-repeat center;
      background-size: 50%;
    }
    &.type-store {
      background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAADcElEQVR4Xu1bwXXaQBCd8QF0C+7AqcDhkEjLJU4FsTtIKojTgTswqSDpwHEFIRck5UKoIHRg54Y5ZPKEAQtJaHbYxVoey3VHo79/Zv6spAHhwH944PsHT4DPgANnwJfAgSeAF0G2BOL41wdCukaAjsvZQgD3SPhZqTffJDhZAoZJeuf65pcbzkjoReGxNQKy6APSV4nDxm0JP0qyoDYD4iT5AYBnjW9KBIAGKore6V6ykYA4jk8Aj/6sOaJ/L5VSE13nz2FninMjAcMk6SPgp9UmCH4qFTqZDXGcDgDh7ZMW0JdeFF3qBKCGgIL4CWtL5+a2bIpaJRHDSgIqxO+vikKn22CcpPcA8OIpY/XEsJqAgvgR6KeUrahK/ZRKFvTEsESAqahIgduy3xZ3iQCp+A3T9BwI5mcFpKMLpV4P6ja1S/ttxLCCAJn45U+KOuKzS/ttxLBcAklK+QgG7dZxt9vNBKb0q0q7Ju1Ho1Fn+jC7ywNVUVh72Ksi4DcAnK56KsJFLwy/VxGQpTMS3OTXqEH7iu41VlH4qq4keQ0AmgTtdreYBY9sP4wA8GT9Bu7Y63QvvS4ANAHEq6DVup1Opx1EPCWAfnnzq7yZIMAlEY3n4rhD+yAI7qez2Xsguirh0Ti6V9ZHuafaalbP50cn+vPgbIIUJ+maFtRBJ6BbJOzkz+NN2/ei6FyH7lqF1MmEPNOu2RsTkDnIWh0hXuLje4FldxgT0ACJ+sXHY9fsORLYV2Kcg31f9wTsewRN8fsM4BiUPr1x/myvm+JjM0D69GZ7g5w/U3wsAXHh6ZB7uuIA2143xecJ4CJiyjDn33TdFJ/PAC4Cpgxz/k3XTfH5DOAiIGV42ZdNP6k/fu/n3zJL8RX3az0DbM4T6Lxldo6AIiAuw7h17tzhCTA8qFkvAdOISK+X2u9cA0wBSa+X2nsCCgz4EuBUWJpiUvvi/aXXS+19CfgSWGfAa4DXAIYBqchI7fdOBOsmQLLNPCcBVRMq3LPDNl2g8JWYBkG7fbFxbMbwbK5L4GJA46Ywy8xOhIgJGCZJ9mH0mtMKJ9a3mGZlu8AirbVnBRokQhz9DKsWAYvpq2z+bzU81eBGq249Dtqts01lWYdVi4Clg8UUVjaF7QoRYyDsS/4gIdYAxyJtHY4oA6zf3QGHngAHgtAohP+ouQ9uJwLCIwAAAABJRU5ErkJggg==') no-repeat center;
      background-size: 50%;
    }
  }
  .title-hot-keyword {
    width: 100%;
    line-height: 25px;
    background-color: #eee;
    text-align: left;
    span {
      line-height: 25px;
      font-size: 12px;
      color: #999;
      margin-left: 6%;
    }
  }
  .content-hot-keyword {
    background-color: #fff;
    padding-top: 10px;
  }
  .key-words {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
  }
  .key-words .item {
    width: 30%;
    border: 1px solid #e0e0e0;
    text-align: center;
    line-height: 30px;
    font-size: 12px;
    color: #848689;
    border-radius: 15px;
    margin-bottom: 10px;
    margin-left: 2%;
    overflow: hidden;
  }
  .fade-enter-active, .fade-leave-active {
    transition: all .3s ease;
  }
  .fade-enter, .fade-leave-to {
    transform: translateY(10px);
    opacity: 0;
  }
  .history-search {
    width: 100%;
    margin-top: 10px;
    background-color: #fff;
    overflow: hidden;
    .his-item {
      padding: 10px;
      overflow: hidden;
    }
    span {
      float: left;
      display: inline-block;
      text-align: left;
      width: calc(100% - 50px);
    }
    .icon-close {
      position: relative;
      top: 2px;
      display: inline-block;
      float: right;
      width: 50px;
      height: 14px;
      background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAABUElEQVRYR+2WsWrDMBCG74ylsVm7FvoGHUoDGTJ36SN4kR9KWvwOnTp1KLQJ9BEKXkvpE0jGVxQwBBPbuivYi73m7v+/nH6djbDwgwv7wwqwTmBwAmVZvgDAxnv/VFXVtySsRVFca62fiejHOfd4SWMQwBjziYh3RPQVQthxIaK5UuoNEW8B4MNau2UBnAtwITi9oyHkCHX/jtszeQs4gpzaDngSIBamCKfUsDLQLx4zkJpHj6QJjJ1v/K1LOzesbID+cbRtWyMiIOKNxFwE0EHkef4ejSMAEdUhhC13V/wLQCl1AiCiE0DTNPMA9AJXR4Asy+Y5gktpny2Ei17DlHueUiNaRBxhTm3SKpYIcnsGNyFX6Hy8nN6xL6IDANxLN1wP4uCce2BlwBhzRMQr7/1esuG6jam1fgWAX2vtjgUg+QaU9LDehhKDqZ4VYJ3AH5vVmDCe89qdAAAAAElFTkSuQmCC") no-repeat right;
      background-size: 14px;
    }
    .clear-his-btn {
      display: block;
      margin: 10px auto;
    }
  }
  .auto-complete {
    height: calc(100% - 50px);
    background-color: #FFFFFF;
    padding: 0 10px;
    li {
      display: flex;
      justify-content: space-between;
      line-height: 30px;
      color: #333;
    }
  }
</style>
