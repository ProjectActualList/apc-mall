import Vue from 'vue'
import Router from 'vue-router'
/* Layout */
import Layout from '@/views/layout/Layout'

Vue.use(Router)

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
    noCache: true                if true ,the page will no be cached(default is false)
  }
**/
export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/404', component: () => import('@/views/errorPage/404'), hidden: true },
  { path: '/401', component: () => import('@/views/errorPage/401'), hidden: true },
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index'),
      name: 'dashboard',
      meta: { title: 'dashboard', icon: 'dashboard' }
    }]
  }
]

export default new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

// 分销
const disRouterMap = {
  path: '/distribution',
  component: Layout,
  redirect: '/distribution/extract-tpl',
  name: 'distribution',
  meta: { title: 'distribution', icon: 'development-manage' },
  children: [
    {
      path: '/distribution/extract-tpl',
      component: () => import('@/views/distribution/extractTpl/index'),
      redirect: '/distribution/extract-tpl/upgrade-logs',
      name: 'extractTpl',
      meta: { title: 'extractTpl' },
      children: [
        { path: 'per-accomplishment-tpl', component: () => import('@/views/distribution/extractTpl/perAccomplishmentTpl'), name: 'perAccomplishmentTpl', meta: { title: 'perAccomplishmentTpl' }},
        { path: 'upgrade-logs', component: () => import('@/views/distribution/extractTpl/upgradeLogs'), name: 'upgradeLogs', meta: { title: 'upgradeLogs' }}
      ]
    },
    {
      path: '/distribution/distributor',
      component: () => import('@/views/distribution/distributor/index'),
      redirect: '/distribution/distributor/distributor-list',
      name: 'distributor',
      meta: { title: 'distributor' },
      children: [
        { path: 'distributor-list', component: () => import('@/views/distribution/distributor/distributorList'), name: 'distributorList', meta: { title: 'distributorList' }},
        { path: 'distributor-statistics', component: () => import('@/views/distribution/distributor/distributorStatistics'), name: 'distributorStatistics', meta: { title: 'distributorStatistics' }, hidden: true }
      ]
    },
    {
      path: '/distribution/achievement',
      component: () => import('@/views/distribution/achievement/index'),
      redirect: '/distribution/achievement/achievement-list',
      name: 'achievement',
      meta: { title: 'achievement' },
      children: [
        { path: 'achievement-list', component: () => import('@/views/distribution/achievement/achievementList'), name: 'achievementList', meta: { title: 'achievementList' }},
        { path: 'bill-list', component: () => import('@/views/distribution/achievement/billList'), name: 'billList', meta: { title: 'billList' }, hidden: true },
        { path: 'bill-details', component: () => import('@/views/distribution/achievement/billDetails'), name: 'billDetails', meta: { title: 'billDetails' }, hidden: true }
      ]
    },
    {
      path: '/distribution/put-forward',
      component: () => import('@/views/distribution/putforward/index'),
      redirect: '/distribution/put-forward/put-forward-settings',
      name: 'putforwardManage',
      meta: { title: 'putforwardManage' },
      children: [
        { path: 'put-forward-settings', component: () => import('@/views/distribution/putforward/putforwardSettings'), name: 'putforwardSettings', meta: { title: 'putforwardSettings' }},
        { path: 'put-forward-apply', component: () => import('@/views/distribution/putforward/putforwardApply'), name: 'putforwardApply', meta: { title: 'putforwardApply' }}
      ]
    }
  ]
}

const asyncRouters = [
  // 商品管理
  {
    path: '/goods',
    component: Layout,
    redirect: '/goods/goods-list',
    name: 'goods',
    meta: {
      title: 'goods',
      icon: 'goods-manage'
    },
    children: [
      { path: 'goods-list', component: () => import('@/views/goods/goodsList'), name: 'goodsList', meta: { title: 'goodsList' }},
      { path: 'goods-audit', component: () => import('@/views/goods/goodsAudit'), name: 'goodsAudit', meta: { title: 'goodsAudit' }},
      {
        path: '/goods/goods-setting',
        component: () => import('@/views/goods/goods-setting/index'),
        redirect: '/goods/goods-setting/category-list',
        name: 'goodsSetting',
        meta: { title: 'goodsSetting' },
        children: [
          { path: 'category-list', component: () => import('@/views/goods/goods-setting/categoryList'), name: 'categoryList', meta: { title: 'categoryList' }},
          { path: 'category-params/:id', component: () => import('@/views/goods/goods-setting/categoryParams'), name: 'categoryParams', hidden: true, meta: { title: 'categoryParams', noCache: true }},
          { path: 'brand-list', component: () => import('@/views/goods/goods-setting/brandList'), name: 'brandList', meta: { title: 'brandList' }},
          { path: 'spec-list', component: () => import('@/views/goods/goods-setting/specList'), name: 'specList', meta: { title: 'specList' }}
        ]
      }
    ]
  },
  // 订单管理
  {
    path: '/order',
    component: Layout,
    name: 'order',
    redirect: '/order/order-list',
    meta: { title: 'order', icon: 'order-manage' },
    children: [
      { path: 'order-list', component: () => import('@/views/order/orderList'), name: 'orderList', meta: { title: 'orderList' }},
      { path: 'refund-list', component: () => import('@/views/order/refundList'), name: 'refundList', meta: { title: 'refundList' }},
      { path: 'collection-list', component: () => import('@/views/order/collectionList'), name: 'collectionList', meta: { title: 'collectionList' }},
      { path: 'receipt-history', component: () => import('@/views/order/receiptHistory'), name: 'receiptHistory', meta: { title: 'receiptHistory' }},
      { path: 'receipt-detail/:id', component: () => import('@/views/order/receiptDetail'), name: 'receiptDetail', hidden: true, meta: { title: 'receiptDetail' }},
      { path: 'detail/:sn', component: () => import('@/views/order/orderDetail'), name: 'orderDetail', hidden: true, meta: { title: 'orderDetail' }},
      { path: 'refund/:sn', component: () => import('@/views/order/refundDetail'), name: 'refundDetail', hidden: true, meta: { title: 'refundDetail' }}
    ]
  },
  // 会员管理
  {
    path: '/member',
    component: Layout,
    redirect: '/member/member-manage/member-list',
    name: 'member',
    meta: { title: 'member', icon: 'member-manage' },
    children: [
      {
        path: '/member/member-manage',
        component: () => import('@/views/member/member-manage/index'),
        redirect: '/member/member-manage/member-list',
        name: 'memberManage',
        meta: { title: 'memberManage' },
        children: [
          { path: 'member-list', component: () => import('@/views/member/member-manage/memberList'), name: 'memberList', meta: { title: 'memberList' }},
          { path: 'member-recycle', component: () => import('@/views/member/member-manage/memberRecycle'), name: 'memberRecycle', meta: { title: 'memberRecycle' }},
          { path: 'edit/:id', component: () => import('@/views/member/member-manage/memberEdit'), name: 'memberEdit', hidden: true, meta: { title: 'memberEdit' }}
        ]
      },
      {
        path: '/member/goods-comment',
        component: () => import('@/views/member/goods-comment/index'),
        redirect: '/member/goods-comment/goods-comment-list',
        name: 'goodsComment',
        meta: { title: 'goodsComment' },
        children: [
          { path: 'goods-comment-list', component: () => import('@/views/member/goods-comment/goodsCommentList'), name: 'mgoodsCommentList', meta: { title: 'goodsCommentList' }},
          { path: 'goods-ask-list', component: () => import('@/views/member/goods-comment/goodsAskList'), name: 'goodsAskList', meta: { title: 'goodsAskList' }}
        ]
      },
      {
        path: '/member/member-notification',
        component: () => import('@/views/member/member-notification/index'),
        redirect: '/member/member-notification/notification-history',
        name: 'memberNotification',
        meta: { title: 'memberNotification' },
        children: [
          { path: 'notification-history', component: () => import('@/views/member/member-notification/notificationHistory'), name: 'notificationHistory', meta: { title: 'notificationHistory' }}
        ]
      }
    ]
  },
  // 店铺管理
  {
    path: '/shop',
    component: Layout,
    redirect: '/shop/shop-manage/shop-list',
    name: 'shop',
    meta: { title: 'shop', icon: 'shop-manage' },
    children: [
      {
        path: '/shop/shop-manage',
        component: () => import('@/views/shop/shop-manage/index'),
        redirect: '/shop/shop-manage/shop-list',
        name: 'shopManage',
        meta: { title: 'shopManage' },
        children: [
          { path: 'shop-list', component: () => import('@/views/shop/shop-manage/shopList'), name: 'shopList', meta: { title: 'shopList' }},
          { path: 'shop-audit', component: () => import('@/views/shop/shop-manage/shopAudit'), name: 'shopAudit', meta: { title: 'shopAudit' }},
          { path: 'edit/:id', component: () => import('@/views/shop/shop-manage/shopEdit'), name: 'shopEdit', hidden: true, meta: { title: 'shopEdit' }}
        ]
      },
      {
        path: '/shop/shop-theme-manage',
        component: () => import('@/views/shop/shop-theme-manage/index'),
        redirect: '/shop/shop-theme-manage/pc',
        name: 'shopThemeManage',
        meta: { title: 'shopThemeManage' },
        children: [
          { path: 'pc', component: () => import('@/views/shop/shop-theme-manage/themeList'), name: 'themeList', meta: { title: 'themeList' }},
          { path: 'wap', component: () => import('@/views/shop/shop-theme-manage/themeListWap'), name: 'themeListWap', meta: { title: 'themeListWap' }}
        ]
      },
      {
        path: '/shop/settlement-manage',
        component: () => import('@/views/shop/settlement-manage/index'),
        redirect: '/shop/settlement-manage/settlement',
        name: 'settlementManage',
        meta: { title: 'settlementManage' },
        children: [
          { path: 'settlement', component: () => import('@/views/shop/settlement-manage/settlement'), name: 'settlement', meta: { title: 'settlement' }},
          { path: 'settlement/:sn', component: () => import('@/views/shop/settlement-manage/settlementList'), name: 'settlementList', hidden: true, meta: { title: 'settlementList' }},
          { path: 'detail/:id', component: () => import('@/views/shop/settlement-manage/settlementDetail'), name: 'settlementDetail', hidden: true, meta: { title: 'settlementDetail' }}
        ]
      }
    ]
  },
  // 促销管理
  {
    path: '/promotions',
    component: Layout,
    redirect: '/promotions/group-buy-manage/group-buy-list',
    name: 'promotions',
    meta: { title: 'promotions', icon: 'promotions-manage' },
    children: [
      {
        path: '/promotions/group-buy-manage',
        component: () => import('@/views/promotions/group-buy-manage/index'),
        redirect: '/promotions/group-buy-manage/group-buy-list',
        name: 'groupBuyManage',
        meta: { title: 'groupBuyManage' },
        children: [
          { path: 'group-buy-list', component: () => import('@/views/promotions/group-buy-manage/groupBuyList'), name: 'groupBuyList', meta: { title: 'groupBuyList' }},
          { path: 'group-buy-category', component: () => import('@/views/promotions/group-buy-manage/groupBuyCategory'), name: 'groupBuyCategory', meta: { title: 'groupBuyCategory' }},
          { path: 'group-buy-goods/:id', component: () => import('@/views/promotions/group-buy-manage/groupBuyGoods'), name: 'groupBuyGoods', hidden: true, meta: { title: 'groupBuyGoods' }},
          { path: 'group-buy-goods-info/:id', component: () => import('@/views/promotions/group-buy-manage/groupBuyGoodsInfo'), name: 'groupBuyGoodsInfo', hidden: true, meta: { title: 'groupBuyGoodsInfo' }}
        ]
      },
      {
        path: '/promotions/points-mall-manage',
        component: () => import('@/views/promotions/points-mall-manage/index'),
        redirect: '/promotions/points-mall-manage/points-classify',
        name: 'pointsMallManage',
        meta: { title: 'pointsMallManage' },
        children: [
          { path: 'points-classify', component: () => import('@/views/promotions/points-mall-manage/pointsClassify'), name: 'pointsClassify', meta: { title: 'pointsClassify' }},
          { path: 'points-goods', component: () => import('@/views/promotions/points-mall-manage/pointsGoods'), name: 'pointsGoods', meta: { title: 'pointsGoods' }}
        ]
      },
      {
        path: '/promotions/seckill-manage',
        component: () => import('@/views/promotions/seckill-manage/index'),
        redirect: '/promotions/seckill-manage/seckill-list',
        name: 'seckillManage',
        meta: { title: 'seckillManage' },
        children: [
          { path: 'seckill-list', component: () => import('@/views/promotions/seckill-manage/seckillList'), name: 'seckillList', meta: { title: 'seckillList' }},
          { path: 'seckill-list/audit/:id', component: () => import('@/views/promotions/seckill-manage/seckillAuditGoodsList'), name: 'seckillAuditGoodsList', hidden: true, meta: { title: 'seckillAuditGoodsList', noCache: true }},
          { path: 'seckill-list/pass/:id', component: () => import('@/views/promotions/seckill-manage/seckillPassGoodsList'), name: 'seckillPassGoodsList', hidden: true, meta: { title: 'seckillPassGoodsList', noCache: true }}
        ]
      },
      {
        path: '/promotions/assemble-manage',
        component: () => import('@/views/promotions/assemble-manage/index'),
        redirect: '/promotions/assemble-manage/assemble-list',
        name: 'assembleManage',
        meta: { title: 'assembleManage' },
        children: [
          { path: 'assemble-list', component: () => import('@/views/promotions/assemble-manage/assembleList'), name: 'assembleList', meta: { title: 'assembleList' }},
          { path: 'assemble-detail/:promotion_id', component: () => import('@/views/promotions/assemble-manage/assembleDetail'), name: 'assembleDetail', hidden: true, meta: { title: 'assembleDetail', noCache: true }}
        ]
      }
    ]
  },
  // 页面管理
  {
    path: '/page',
    component: Layout,
    redirect: '/page/pc-decoration/pc-theme',
    name: 'page',
    meta: { title: 'page', icon: 'page-manage' },
    children: [
      {
        path: '/page/pc-decoration',
        component: () => import('@/views/page/pc-decoration/index'),
        redirect: '/page/pc-decoration/floor',
        name: 'pcDecoration',
        meta: {
          title: 'pcDecoration'
        },
        children: [
          { path: 'floor', component: () => import('@/views/page/pc-decoration/pcFloorManage'), name: 'pcFloorManage', meta: { title: 'pcFloorManage' }},
          { path: 'focus', component: () => import('@/views/page/pc-decoration/pcFocusManage'), name: 'pcFocusManage', meta: { title: 'pcFocusManage' }}
        ]
      },
      {
        path: '/page/mobile-decoration',
        component: () => import('@/views/page/mobile-decoration/index'),
        redirect: '/page/mobile-decoration/floor',
        name: 'mobileDecoration',
        meta: {
          title: 'mobileDecoration'
        },
        children: [
          { path: 'floor', component: () => import('@/views/page/mobile-decoration/mobileFloorManage'), name: 'mobileFloorManage', meta: { title: 'mobileFloorManage' }},
          { path: 'focus', component: () => import('@/views/page/mobile-decoration/mobileFocusManage'), name: 'mobileFocusManage', meta: { title: 'mobileFocusManage' }}
        ]
      },
      {
        path: '/page/page-setting',
        component: () => import('@/views/page/page-setting/index'),
        redirect: '/page/page-setting/pc-site-menu',
        name: 'pageSetting',
        meta: { title: 'pageSetting' },
        children: [
          { path: 'pc-site-menu', component: () => import('@/views/page/page-setting/pcSiteMenu'), name: 'pcSiteMenu', meta: { title: 'pcSiteMenu' }},
          { path: 'mobile-site-menu', component: () => import('@/views/page/page-setting/mobileSiteMenu'), name: 'mobileSiteMenu', meta: { title: 'mobileSiteMenu' }},
          { path: 'hot-keyword', component: () => import('@/views/page/page-setting/hotKeyword'), name: 'hotKeyword', meta: { title: 'hotKeyword' }}
        ]
      }
    ]
  },
  // 统计
  {
    path: '/statistics',
    component: Layout,
    redirect: '/statistics/member-analysis/order-amount',
    name: 'statistics',
    meta: { title: 'statistics', icon: 'statistics-manage' },
    children: [
      {
        path: '/statistics/member-analysis',
        component: () => import('@/views/statistics/member-analysis/index'),
        redirect: '/statistics/member-analysis/order-amount',
        name: 'memberAnalysis',
        meta: { title: 'memberAnalysis' },
        children: [
          { path: 'order-amount', component: () => import('@/views/statistics/member-analysis/orderAmount'), name: 'orderAmount', meta: { title: 'orderAmount' }},
          { path: 'added-member', component: () => import('@/views/statistics/member-analysis/addedMember'), name: 'addedMember', meta: { title: 'addedMember' }}
        ]
      },
      {
        path: '/statistics/goods-statistics',
        component: () => import('@/views/statistics/goods-statistics/index'),
        redirect: '/statistics/goods-statistics/price-sales',
        name: 'goodsStatistics',
        meta: { title: 'goodsStatistics' },
        children: [
          { path: 'price-sales', component: () => import('@/views/statistics/goods-statistics/priceSales'), name: 'priceSales', meta: { title: 'priceSales' }},
          { path: 'hot-goods', component: () => import('@/views/statistics/goods-statistics/hotGoods'), name: 'hotGoods', meta: { title: 'hotGoods' }},
          { path: 'goods-sales-details', component: () => import('@/views/statistics/goods-statistics/goodsSalesDetails'), name: 'goodsSalesDetails', meta: { title: 'goodsSalesDetails' }},
          { path: 'goods-collect', component: () => import('@/views/statistics/goods-statistics/goodsCollect'), name: 'goodsCollect', meta: { title: 'goodsCollect' }}
        ]
      },
      {
        path: '/statistics/industry-analysis',
        component: () => import('@/views/statistics/industry-analysis/index'),
        redirect: '/statistics/industry-analysis/industry-scale',
        name: 'industryAnalysis',
        meta: { title: 'industryAnalysis' },
        children: [
          { path: 'industry-scale', component: () => import('@/views/statistics/industry-analysis/industryScale'), name: 'industryScale', meta: { title: 'industryScale' }},
          { path: 'generality-overview', component: () => import('@/views/statistics/industry-analysis/generalityOverview'), name: 'generalityOverview', meta: { title: 'generalityOverview' }}
        ]
      },
      {
        path: '/statistics/traffic-analysis',
        component: () => import('@/views/statistics/traffic-analysis/index'),
        redirect: '/statistics/traffic-analysis/shop',
        name: 'trafficAnalysis',
        meta: { title: 'trafficAnalysis' },
        children: [
          { path: 'shop', component: () => import('@/views/statistics/traffic-analysis/trafficAnalysisShop'), name: 'trafficAnalysisShop', meta: { title: 'trafficAnalysisShop' }},
          { path: 'goods', component: () => import('@/views/statistics/traffic-analysis/trafficAnalysisGoods'), name: 'trafficAnalysisGoods', meta: { title: 'trafficAnalysisGoods' }}
        ]
      },
      {
        path: '/statistics/other-statistics',
        component: () => import('@/views/statistics/other-statistics/index'),
        redirect: '/statistics/other-statistics/order',
        name: 'otherStatistics',
        meta: { title: 'otherStatistics' },
        children: [
          { path: 'order', component: () => import('@/views/statistics/other-statistics/orderStatistics'), name: 'orderStatistics', meta: { title: 'orderStatistics' }},
          { path: 'sales-revenue', component: () => import('@/views/statistics/other-statistics/salesRevenueStatistics'), name: 'salesRevenueStatistics', meta: { title: 'salesRevenueStatistics' }},
          { path: 'regionalAnalysis', component: () => import('@/views/statistics/other-statistics/regionalAnalysis'), name: 'regionalAnalysis', meta: { title: 'regionalAnalysis' }},
          { path: 'customerPriceDistribution', component: () => import('@/views/statistics/other-statistics/customerPriceDistribution'), name: 'customerPriceDistribution', meta: { title: 'customerPriceDistribution' }},
          { path: 'refund', component: () => import('@/views/statistics/other-statistics/refundStatistics'), name: 'refundStatistics', meta: { title: 'refundStatistics' }}
        ]
      }
    ]
  },
  // 设置
  {
    path: '/setting',
    component: Layout,
    redirect: '/setting/shop-settings/system',
    name: 'setting',
    meta: { title: 'setting', icon: 'setting-manage' },
    children: [
      {
        path: '/setting/shop-settings',
        component: () => import('@/views/setting/shop-settings/index'),
        redirect: '/setting/shop-settings/system',
        name: 'shopSettings',
        meta: { title: 'shopSettings', noCache: true },
        children: [
          { path: 'system', component: () => import('@/views/setting/shop-settings/systemSettings'), name: 'systemSettings', meta: { title: 'systemSettings' }},
          { path: 'smtp', component: () => import('@/views/setting/shop-settings/SMTPSettings'), name: 'smtpSettings', meta: { title: 'smtpSettings' }},
          { path: 'sms-gateway', component: () => import('@/views/setting/shop-settings/SMSGatewaySettings'), name: 'smsGatewaySettings', meta: { title: 'SMSGatewaySettings' }},
          { path: 'express-platform', component: () => import('@/views/setting/shop-settings/expressPlatformSettings'), name: 'expressPlatformSettings', meta: { title: 'expressPlatformSettings' }},
          { path: 'electronic-receipt', component: () => import('@/views/setting/shop-settings/electronicReceipt'), name: 'electronicrEceipt', meta: { title: 'electronicReceipt' }},
          { path: 'storage-solution', component: () => import('@/views/setting/shop-settings/storageSolution'), name: 'storageSolution', meta: { title: 'storageSolution' }},
          { path: 'static-page', component: () => import('@/views/setting/shop-settings/staticPage'), name: 'staticPage', meta: { title: 'staticPage' }},
          { path: 'goods-index', component: () => import('@/views/setting/shop-settings/goodsIndex'), name: 'goodsIndex', meta: { title: 'goodsIndex' }},
          { path: 'trust-login', component: () => import('@/views/setting/shop-settings/trustLogin'), name: 'trustLogin', meta: { title: 'trustLogin' }}
        ]
      },
      {
        path: '/setting/message-settings',
        component: () => import('@/views/setting/message-settings/index'),
        redirect: '/setting/message-settings/member',
        name: 'messageSettings',
        meta: { title: 'messageSettings' },
        children: [
          { path: 'shop', component: () => import('@/views/setting/message-settings/shopMessage'), name: 'shopMessageSettings', meta: { title: 'shopMessage' }},
          { path: 'member', component: () => import('@/views/setting/message-settings/memberMessage'), name: 'memberMessageSettings', meta: { title: 'memberMessage' }}
        ]
      },
      {
        path: '/setting/push-settings',
        component: () => import('@/views/setting/push-settings/index'),
        redirect: '/setting/push-settings/goods',
        name: 'pushSettings',
        meta: { title: 'pushSettings' },
        children: [
          { path: 'goods', component: () => import('@/views/setting/push-settings/goodsPush'), name: 'goodsPush', meta: { title: 'goodsPush' }},
          { path: 'app', component: () => import('@/views/setting/push-settings/appPush'), name: 'appPush', meta: { title: 'appPush' }}
        ]
      },
      {
        path: '/setting/payment-and-delivery',
        component: () => import('@/views/setting/payment-and-delivery/index'),
        redirect: '/setting/payment-and-delivery/payment',
        name: 'paymentAndDelivery',
        meta: { title: 'paymentAndDelivery' },
        children: [
          { path: 'payment', component: () => import('@/views/setting/payment-and-delivery/payment'), name: 'paymentSettings', meta: { title: 'payment' }},
          { path: 'express', component: () => import('@/views/setting/payment-and-delivery/express'), name: 'expressSettings', meta: { title: 'express' }},
          { path: 'regional-management', component: () => import('@/views/setting/payment-and-delivery/regionalManagement'), name: 'regionalManagementSettings', meta: { title: 'regionalManagement' }}
        ]
      },
      {
        path: '/setting/auth-settings',
        component: () => import('@/views/setting/auth-settings/index'),
        redirect: '/setting/auth-settings/administrator-manage',
        name: 'authSettings',
        meta: { title: 'authSettings' },
        children: [
          { path: 'administrator-manage', component: () => import('@/views/setting/auth-settings/administratorManage'), name: 'administratorManage', meta: { title: 'administratorManage' }},
          { path: 'role-manage', component: () => import('@/views/setting/auth-settings/roleManage'), name: 'roleManage', meta: { title: 'roleManage' }},
          { path: 'role-permission/:id(\\d+)', component: () => import('@/views/setting/auth-settings/rolePermission'), name: 'rolePermission', hidden: true, meta: { title: 'rolePermission', noCache: true }}
        ]
      }
    ]
  },
  // 开发
  {
    path: '/development',
    component: Layout,
    redirect: '/development/tool-manage/menu-manage',
    name: 'development',
    meta: { title: 'development', icon: 'development-manage' },
    children: [
      {
        path: '/development/tool-manage',
        component: () => import('@/views/development/tool-manage/index'),
        redirect: '/development/tool-manage/menu-manage',
        name: 'toolManage',
        meta: { title: 'toolManage' },
        children: [
          { path: 'menu-manage', component: () => import('@/views/development/tool-manage/menuManage'), name: 'menuManage', meta: { title: 'menuManage' }}
        ]
      },
      {
        path: '/development/article-manage',
        component: () => import('@/views/development/article-manage/index'),
        redirect: '/development/article-manage/article-list',
        name: 'articleManage',
        meta: { title: 'articleManage' },
        children: [
          { path: 'article-category', component: () => import('@/views/development/article-manage/articleCategory'), name: 'articleCategory', meta: { title: 'articleCategory' }},
          { path: 'article-list', component: () => import('@/views/development/article-manage/articleList'), name: 'articleList', meta: { title: 'articleList' }},
          { path: 'add', component: () => import('@/views/development/article-manage/addArticle'), name: 'addArticle', hidden: true, meta: { title: 'addArticle' }}
        ]
      }
    ]
  },

  { path: '*', redirect: '/404', hidden: true }
]

if (process.env.DISTRIBUTION) {
  asyncRouters.push(disRouterMap)
}

export const asyncRouterMap = asyncRouters
