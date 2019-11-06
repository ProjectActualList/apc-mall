<template>
  <div id="apply-menu" class="menu-container">
    <div class="title">
      <h2>商家入驻申请</h2>
    </div>
    <div class="content">
      <dl :class="[step === 1 && 'current']">
        <dt>
          <i></i>入驻协议
        </dt>
      </dl>
      <dl :class="[step === 2 && 'current']">
        <dt>
          <i></i>基本信息
        </dt>
      </dl>
      <dl :class="[step === 3 && 'current']">
        <dt><i></i>认证信息</dt>
        <dd>
          <ul>
            <li>
              <i></i><a>营业执照信息</a>
            </li>
            <li>
              <i></i><a>组织机构代码证</a>
            </li>
            <li>
              <i></i><a>一般纳税人证明</a>
            </li>
          </ul>
        </dd>
      </dl>
      <dl :class="[step === 4 && 'current']">
        <dt>
          <i></i>财务资质信息
        </dt>
        <dd>
          <ul>
            <li>
              <i></i>
              <a>开户行银行许可证</a>
            </li>
            <li>
              <i></i>
              <a>税务登记证</a>
            </li>
          </ul>
        </dd>
      </dl>
      <dl :class="[step === 5 && 'current']">
        <dt>
          <i class="hideer">  </i>
          店铺信息
        </dt>
      </dl>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'apply-menu',
    props: ['step']
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .menu-container {
    place-self: flex-start;
    width: 208px;
    border: solid 1px #D7D7D7;
    .title {
      background-color: #F5F5F5;
      height: 36px;
      border-top: solid 1px #D7D7D7;
      margin-top: -1px;
      h2 {
        font: 600 14px/20px "microsoft yahei";
        color: #333333;
        height: 20px;
        padding: 8px 10px;
      }
    }
    .content {
      width: 190px;
      margin: 0 auto;
      .current dt i { background-position: -280px 0 }
      .current dd { display: block }
      dt {
        line-height: 20px;
        font-weight: 600;
        color: #555;
        height: 20px;
        padding: 10px 0 9px 0;
        border-bottom: solid 1px #F5F5F5;
        i {
          background: url(~assets/images/icons-apply-shop.png) no-repeat -300px 0;
          vertical-align: middle;
          display: inline-block;
          width: 11px;
          height: 11px;
          margin-right: 10px;
          margin-left: 6px;
        }
      }
      dd { display: none }
      ul {
        width: 100%;
        padding: 6px 0;
        li {
          padding: 6px 0;
          a { color: #666 }
          i {
            background: url(~assets/images/icons-apply-shop.png) no-repeat -320px 0;
            vertical-align: middle;
            display: inline-block;
            width: 3px;
            height: 5px;
            margin-right: 10px;
            margin-left: 14px;
          }
        }
      }
    }
  }
</style>
