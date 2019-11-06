<template>
  <div id="comments" style="background-color: #f7f7f7">
    <nav-bar title="订单评论"/>
    <div class="comment-container">
      <div class="cm-item" v-for="(cm, index) in commentsForm.comments" :key="index">
        <div class="cm-nav">
          <img :src="skulist[index].goods_image" :alt="skulist[index].name" class="cm-img"/>
          <div class="cm-grade">
            <span class="item-flower praise" :class="[cm.grade === 'good' && 'active']">
              <input type="radio" v-model="cm.grade" value="good">
              <i></i>
              <span>好评</span>
            </span>
            <span class="item-flower average" :class="[cm.grade === 'neutral' && 'active']">
              <input type="radio" v-model="cm.grade" value="neutral">
              <i></i>
              <span>中评</span>
            </span>
            <span class="item-flower bad-review" :class="[cm.grade === 'bad' && 'active']">
              <input type="radio" v-model="cm.grade" value="bad">
              <i></i>
              <span>差评</span>
            </span>
          </div>
        </div>
        <div class="cm-con">
          <van-field
            v-model="cm.content"
            type="textarea"
            :placeholder="cm.grade === 'good' ? '宝贝满足您的期待吗？说说它的优点和美中不足的地方吧' : '可以告诉我们您遇到了什么问题吗？'"
            :error-message="errorMsgs[index]"
            rows="4"
          />
        </div>
        <div class="cm-photo">
          <div class="cm-photos">
            <a href="javascript:;" v-for="(img, index) in cm.images" :key="index" class="img-item">
              <van-icon name="clear" @click="cm.images.splice(index, 1)"/>
              <img :src="img">
            </a>
          </div>
          <van-uploader v-if="cm.images.length < 5" :after-read="(file) => { onFileRead(cm, file) }" accept="image/png, image/jpeg, image/jpg">
            <van-icon name="photograph" />
          </van-uploader>
        </div>
      </div>
      <div class="shop-grade">
        <div class="shop-grade-nav">
          <van-icon name="shop"/>店铺评分
        </div>
        <div class="rating-star">
          <div class="logistics-star">
            <div class="text">
              <span>描述相符</span>
            </div>
            <div class="star">
              <van-rate v-model="commentsForm.description_score" color="#f42424"/>
            </div>
            <div class="grade-text">
              <span>{{ countGradeText(commentsForm.description_score) }}</span>
            </div>
          </div>
          <div class="logistics-star">
            <div class="text">
              <span>服务态度</span>
            </div>
            <div class="star">
              <van-rate v-model="commentsForm.service_score" color="#f42424"/>
            </div>
            <div class="grade-text">
              <span>{{ countGradeText(commentsForm.service_score) }}</span>
            </div>
          </div>
          <div class="logistics-star">
            <div class="text">
              <span>物流服务</span>
            </div>
            <div class="star">
              <van-rate v-model="commentsForm.delivery_score" color="#f42424"/>
            </div>
            <div class="grade-text">
              <span>{{ countGradeText(commentsForm.delivery_score) }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <van-button type="danger" :disabled="cantComments" size="large" @click="submitForm" class="cm-btn">发表评论</van-button>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_Order from '@/api/order'
  import * as API_Members from '@/api/members'
  import request, { Method } from '@/utils/request'
  import '../../static/lrz/lrz.all.bundle'
  export default {
    name: 'comments',
    head() {
      return {
        title: `评价商品-${this.site.site_name}`
      }
    },
    data() {
      return {
        order_sn: this.$route.query.order_sn,
        skulist: '',
        commentsForm: {},
        // 不能评论【已评论过】
        cantComments: false,
        // 错误信息
        errorMsgs: []
      }
    },
    mounted() {
      API_Order.getOrderDetail(this.order_sn).then(response => {
        if (!response.order_operate_allowable_vo.allow_comment) {
          this.cantComments = true
          return false
        }
        this.skulist = response.order_sku_list
        this.commentsForm = {
          order_sn: this.order_sn,
          delivery_score: 5,
          description_score: 5,
          service_score: 5,
          comments: response.order_sku_list.map((item, index) => {
            this.errorMsgs[index] = ''
            return {
              sku_id: item.sku_id,
              content: '',
              grade: 'good',
              images: []
            }
          })
        }
      })
    },
    methods: {
      countGradeText(val) {
        switch (val) {
          case 5: return '非常好'
          case 4: return '好'
          case 3: return '一般'
          case 2: return '差'
          case 1: return '非常差'
          default: return '非常好'
        }
      },
      /** 图片上传 */
      onFileRead(cm, file) {
        lrz(file.file).then(async rst => {
          const res = await request({
            url: this.MixinUploadApi,
            method: Method.POST,
            headers: { 'Content-Type': 'multipart/form-data' },
            data: rst.formData
          })
          res && cm.images.push(res.url)
        })
      },
      submitForm() {
        let err_flg = false
        const params = JSON.parse(JSON.stringify(this.commentsForm))
        params.comments.forEach((item, index) => {
          if (item.grade !== 'goods' && !item.content) {
            err_flg = true
            this.$set(this.errorMsgs, index, '请填写您的评价！')
          }
        })
        if (err_flg) {
          this.$message.error('表单填写有误，请核对！')
          return false
        }
        API_Members.commentsOrder(params).then(() => {
          this.$message.success('发表成功！')
          this.$router.back()
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .comment-container {
    padding-top: 46px;
    padding-bottom: 50px;
  }
  .cm-btn {
    position: fixed;
    bottom: 0;
    border-radius: 0;
  }
  .cm-item {
    width: 100%;
    background: white;
    margin-bottom: 15px;
  }
  .cm-nav {
    display: flex;
    height: 50px;
    line-height: 50px;
    border-bottom: 1px solid #e4e2e2;
  }
  .cm-img {
    height: 40px;
    width: 40px;
    margin: 5px 5px 5px 10px;
  }
  .cm-grade {
    display: flex;
    width: calc(100% - 85px);
    margin-left: 30px;
    .item-flower {
      display: flex;
      align-items: center;
      width: 80px;
      height: 50px;
      position: relative;
      input {
        position: absolute;
        width: 100%;
        height: 100%;
        opacity: 0;
      }
      i {
        display: inline-block;
        width: 23px;
        height: 25px;
        margin-right: 5px;
      }
      &.praise i {
        background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABcAAAAZCAMAAADt/kXiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6ODVFRjFCQzFEMzUxMTFFNzg0QTdBRjgzMTlCMDlDRTEiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6ODVFRjFCQzJEMzUxMTFFNzg0QTdBRjgzMTlCMDlDRTEiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo4NUVGMUJCRkQzNTExMUU3ODRBN0FGODMxOUIwOUNFMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo4NUVGMUJDMEQzNTExMUU3ODRBN0FGODMxOUIwOUNFMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PnT5ZF8AAAKmUExURf7+/v7+///+/u3t7f/8//7//////v/++/z+//3+///7///8/v/9/u7u7v/9//79/P39/+no6O/v7/79//7//vz+/v/7/f/8/f/9/P78/v78//79/vz9//3+/f79++3s7f39/fz9/Pv+/v3////+/Ozs5//9/fP08vv8/PP18/z++f38/vPy8vz8/u3v7P/++fz9/v/5//38//3+++fo6P76/v76/+nn6P/v6vr//uzm5fv9/Pr6+/Ty8tfPzNHQz8vKyf3/+uzr6+Da1/79+vv+9+DY1vj+9dLQzNXMytjQztva1ufj4/37/uvs7ejl5f39/v//+vn///3+/MrIx+Hb1Pf///z//f3+/uro59rZ2Ojp5v39/PHx8+Tj2NPMyfz///v9+fv9+/3//Pz99+be3ejl3/z99eHd2P/9+Pv//+bj3+nm5eTj39HMyujt6Ozq68zJyczKydbV1N7Y1f37/Pn+/dnQzvH19v/+/efi2tTRz/7++P/+9PT19vr+/fv9/czLyfr99+rn5+zu7e7s7P3++MzLy83LytXRztTQz9TQzurt6v37/dDOzOzt7fz9/f78/fv//vz//v39+uXg4dvT0MvJyNjV1czJyNPPzvf09/z+/efl3fDz8Oro5djQz+bo6PXy88vHxt7a2evp6OLb2fj++/77/urr6/D2983Kyv7/9ODg3c7Ny+rn5vr//Ojs7cvJyf/6/fz++P7+/Ovr5t/Y1efi4ebk4/79/fv9/tDKyO/p6f779/T9+9XRzevx8srJyP/9+/72/+fs7Pn//NbV1dXQzvr++Pz8/8rIyMrJyebo5f/9+v75/urk4+/q4/T09+3r6u3r5fz+/NLPzvv+/NrZ2fX09v///P/7/P/8++rq6P77///+/8nIyP///xMunIUAAAHfSURBVHjaZNBjk2NBFAbgc5FcbpKdOBnbxtoza9u27Z01B+uZtW3bto1z8k+2c3erNlV5v3Sfp7u6ul5As1+0y5wtvjy1UbrEWfBvAL/rop1LuHGkosOWJAkdutlkuEnu5rPortTJ44kGJSEy5Xk0A9ozcjzuCw0nEkvPrsmw86JVlQF4zSXNjYnpdPqFINRQ/0PV51rPzMv1gSl5zyTaxu4KgUDgJlsHU23JOA6w/iuaIWy9sizowuwmZ74eoMJ8DbDXKaoTCEldon3FKiRktQzze/luQDgc5o+ABxSvhnmhk/1f+UUfQv1Jj1Ulfh10+/K7FOotqLPikQAtXWSKiPjvE9Yccy5oDjj8i7o35KAd3VbiF74GdFifTyHqa2Bm5jR6B0rc2ET2zo7sptOJ6gX9IFHVtRWNrRyA2X2LlbKkbCrd/0x0ohkbRvq1aMC4BsGGaXUbovZjjO06KXcA4Nns7cb0kqifsaFdBXeGAaod5+y+vJb+5enblLS09W9+sx4yZn2KfT+KzhPNbzvvY+V12cYVeQG56JxLRx/uTzk+8NuiKPi5+HFvsYhnvamuVt1ted7Ezc+WOjZotiEmLXJj6UnAyKwfBdZi74MRo4cqoi+qzyZPrDN95R8BBgAU5TEbFgozOwAAAABJRU5ErkJggg==") no-repeat center;
        background-size: 100%;
      }
      &.praise.active i {
        background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABcAAAAZCAIAAABVQiKHAAAE2ElEQVQ4EWVVS4scVRQ+596qrn5Mp+eZzsuJYcJE8QmJMeblQnASQhCNIIIQQnDjIn/DjUE32QhRQQMiqCQqgi4kRGaRMEkMxMSYh3nMaOKMM93TU11V9+V3etSN1c3tC3XPd75zvu/c5uADcQgeCz7kmRRRCNhgCezxKqjIa1KBIg5ETP9/EIIHsf88AgEsCo4cs4+si61T5PAaZwKgAI4E/wX04iKJCRoHY9dLgwMuCBHlVJF3pn4Jabe8a6MuDSjhwkEBZJkQiPXAOQhJJABzVoaCFiZ4q0jlRfjxm8XPjod0yT+YaOw/TPVVhMqInZwwCrFeu+VSpYIeRwllJFMMtkyLrdn0g6Or29ciLmZ+6KRjT67cul+DN0cm4IiLPVAQJN1DRXjQW5PdupGdei8x983obh4e5Knv181NUSRNG5n/uXvyWJZaO78Y374Y3b+Wv/hGvO0AJdWghLtUZBR7T52bF9LTXwzobOnSxURpbZZMmWJQBY4NfPOi+/BtYyy7Wa1brcmhylMvhEpFQQMKyAUBOORZ6e6vIfurnoSkm+vCorxuTNqTNiGqkcrm49z7zHE5c2Tqc9OqM+cG10QoijmCyLELXBjVnkVUKGwkalcpUknoUIi90iq1KvaOFkxN2CmL3v9hiwVLpJGNfCTdkZ2ySeS9JVEJve9VC57wJHLBJChKUyTlKc5RQ0XFVZxixGMRxcGnlBQjjQylKbGcwQpoC3QoDwBxnXIiCL7MFR/36fKAtF7ixe4S4JPErR93QzUkVN6VqKupg5fwXlAmT0AabWYu4BbvS6lds5mqw+iaUMEKspApKF1bv4027Mgp1jbSzuvea4joKCk58UmGsUjIljk3xI8/C4FQg0wLw/mwixRFpeGx6pY9OVeC1QxKQYbHo7vUh1owl5SoXFOKhIOjfZt3UBTLULHFuCxX5LyYmapbX862vEK59d4VJTIJ4IvYt4GhHSeOKm3Kaxv5yHul/iZaIY0lq7x4U7qjg3f4aa5rvvTm9MzV6N6VIYuxStFSB8KSTqdRvTMwOPzqkeiJ50kjA7RAcyNIhA14CWxBhbJG6Whheoo/PdF36YzLbiu1BA/50Ahxv1k1aiYONp7bx6U6tNIa8UDCfKKzwHOYgiK7fq7720+8coiaG1bUNqSzs63znySTp6Jutrj7cGXTrsGH1pqkyO/e0jO/u75m39Pbud4vyrCDgOzYQ92ZS5cvfH28prujq8dp257GzonagbdaD6ZrrYX+g0dCNzfnzs6d++7OhclFiL3pmZ3jj9VW1B2J9aQvPb/EzV0Tmx9eVU5vN05/Nffl0faVM7VD76R6ZeJaIVd/fvsxf/5+vdF8dO+++fFH9IpmeaiOrmQE9aVBIumyXzUm1BW+vZjdOHvz5InqyPYRezleuO4PvXv+o2NjG8fW7H1d1QZ8uSI3jOTH4EAUXEtA8YJiVe+mk04p5ebpztVAzfaNyZprx7tfC/fv8drVVKp6X8XYeNFFBgzjIbcbUCARQDCDAbMIHxpmTLAOhrS2XuM8Sl9evLOsvOSDgxRiey35tyJxKUYGWYLG4AeNrfdiIrAMuCSgI6A8wHAloxEu4Hrq/SXkWiYAtxT+KeTaxB5H5bg8ARyRTswIn8sKewBHrIEtZgh7cSyy/A3sjKrUNJzDUgAAAABJRU5ErkJggg==") no-repeat center;
        background-size: 100%;
      }
      &.average i, &.bad-review i {
        background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABcAAAAZCAIAAABVQiKHAAADsklEQVQ4EY1UaXPaSBDVSAJdIMQtGduQSpzdlP//L/HmS2LMoQLMbXEJgdDs6xHglCupSoMoZqa73+vXPWI84dJfWurIfuOtSjj79UC4cs5PsOTEJMZkpiiKLMsXtw8BlFS9nF0QmPT2Fsxn8zDcx/EJ4aqqqJmMZVpOsWAYBgNkSgoRjHOJY4fxE+3RQmIJT4aDUa/nZ9RspVLJ5XIgFayWQRBE+4OiqMVSsdHw8rZ1xmSI5QkoIwuCZSbjYDabdzqdmxvPdT1wxKZgTw77/WE6nQZvQRzzarVcq1ey2QzK5DxhjKnES1SVcN7v+3bBdj0XMoCdxLlMdJnCFdPUm/d3Ud1dLpfdTh+StVp3woVoCV0E4jpYH49H13WhJsoFgkT8zka1S5KuZw1Dj09HWWakgSw+En4RQFsS5AQnTcvCnySEcqQZKCaCK9ykxeKt3e6UyqV6vYYyLxAXOOIP4xLamxaIBeiQCQw6TKT1egOBPM8DGOikeYAmpkDw1w0dak3GE6BTPjypJUkKCy6VStkyTb/vHw4xCSYGDTDvpecsC9M1nc5WqzVyJKiDKVQZGbLSx7SMLw+fkLbX7cXoVorH04pESjWjaLoR7qLnn+3dNqRhpS7hISHD3X6+WM6ms+1up2V1gG02G0oMBIYeCTlS/ZyCvQERzn/8eL5v3mNJuQTdIFh1uz60OJ1iDHQuZzEZTIFP5eMeUT4MFngXi47vD+o1NzpGnZcOVKjWKqZpwFvTtITHd7fNgp1XVAWF6qKblyznNhJ5Tc86jj2bjx8fH+em0W6/9Hr9VqtZLleiKDY0s1wqGqZOkGnjSAqag3NFxE2WFEnxPPe/798nk1nj1rPy5mQ8G79Op5MFZ4lt59FHgF17D5lFfzjVgtLW63W43+PKGro+eh2PhuN/v3217Rz8ouiw3YTb7QbV4Xrvwm20j0zLQk+peTBQQBZ8fd8HedxacKnX3ef2C1T959vXTIY6BQPUYr4YDIbojqKqt42b1qcmkhIFCJu+GeJTvBU2Gr3itVAslV5HI8dxms27TDaLsRsOR8PBAKo5xaIOqTVN13VMY0rnXFEqMfiHYYj3i41XiGU9PT1Va9WHh8/Hw6nb7RacfL1WFw0SnRG1pDzPXGgBE2XGxxhjAlssF6vVqtFoKEoGAIoCYOEharz6U1yqbhpPB6mHcP7NkraEz9VBbIjBxNY1PXZ/9cD/D6cfHN6zIMU18vrnT3lF2IcfMXXXyPTwPR49vPjjTv7Z/gehGPTuG3E/sQAAAABJRU5ErkJggg==") no-repeat center;
        background-size: 100%;
      }
      &.average.active i, &.bad-review.active i {
        background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABcAAAAZCAMAAADt/kXiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NTIwOTU2MkVEMzUyMTFFN0I1N0Y5RjRCODhDM0Y4NzYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NTIwOTU2MkZEMzUyMTFFN0I1N0Y5RjRCODhDM0Y4NzYiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo1MjA5NTYyQ0QzNTIxMUU3QjU3RjlGNEI4OEMzRjg3NiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo1MjA5NTYyREQzNTIxMUU3QjU3RjlGNEI4OEMzRjg3NiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Po9jmKQAAADtUExURf2zk/79/v78/v5iHf5cE//+/vuMW/38/fyohPt5QPuVaP///vtxM/xfGf69oP69of5QAfyfdv1XDPuLW/79/ftwM//+/f6QX/1kIP6zk/yog//9/vthG/p5P/toJv5dFP///fyphP5jHf1sK/x9Rf1WC/toJfxoJvufdv6PX/twMvqLWv5RA/b0+P6zlPyfd////Pv6/fz7/vtsLP1YDft0OPxaEPqCTfyohfuCTv2LWP/+/P1dFv38//39+v2HUv5nJPxoJ/2zkvufd/p5QP6hd/7+/f+9oP/++/xrK/78//+9of/9///+//5PAJKHuPcAAAFGSURBVHjaVFJnYwIxCCXxll71qnVr6+ree+/dQvj/P6ckN6x8yHEP8nhAwCwYUeaAcwh70Xa9D4jGz3B7IC5xakHyRUT+5xiIFAHPrSx34vEjUEhYZX4uiU1fbKQCpukDaZs+9VzyqDSTs0ahAqOF/L6U03gt5kORJXqQeVbA+47H4TXmLS/D5d5aTf2SlpgVmfN4XeY3lO4ENxHz+SgP3DBHekC2LynA3RRe5gZzKzQuH1OksHftt2EgQlf+9xsE0Lz7Fj1Kv4o4i/3IgFALQ/tY5iNEoghWXXqiRHxIPhgsN64iOy+oVDk4Ohte7DidPZsmFa6R8KNqf55IaSCcpAxcT043mIe7651QKacT9qR95skBX9523C7J1pVIBLjJ/YcTZVdMJu0rXTfKotyu8/1S7ucfU+QvYsV7cKR+HMdz/E+AAQDIpIk/MoAlqgAAAABJRU5ErkJggg==") no-repeat center;
        background-size: 100%;
      }
    }
  }
  .cm-con {
    textarea {
      resize: none;
      height: 100px;
      width: 100%;
      padding: 6px;
      background-color: #fff;
      border: none;
      box-sizing: border-box;
      overflow: hidden;
      font-size: 13px;
    }
  }
  .shop-grade {
    background-color: #fff;
    .shop-grade-nav {
      display: flex;
      align-items: center;
      height: 45px;
      line-height: 45px;
      position: relative;
      font-size: 14px;
      .van-icon {
        font-size: 24px;
        margin: 0 5px;
      }
    }
  }
  .rating-star {
    .logistics-star {
      height: 35px;
      line-height: 35px;
      clear: both;
      .text {
        float: left;
        font-size: 12px;
        margin-left: 6%;
        margin-right: 10px;
        color: #999;
        line-height: 30px;
      }
      .star {
        float: left;
      }
      .grade-text {
        float: left;
        font-size: 12px;
        color: #999;
        margin-left: 10px;
        line-height: 30px;
      }
    }
  }
  .cm-photo {
    padding: 5px 10px;
    border-top: 1px solid #e4e2e2;
    /deep/ .van-icon {
      font-size: 28px;
    }
  }
  .cm-photos {
    display: flex;
    align-items: center;
    .img-item {
      position: relative;
      /deep/ .van-icon {
        position: absolute;
        top: 0;
        right: 0;
        color: red;
        font-size: 20px;
        background-color: #fff;
        border-radius: 100%;
        overflow: hidden;
      }
    }
    img {
      width: 50px;
      height: 50px;
      margin-right: 5px;
    }
  }
</style>
