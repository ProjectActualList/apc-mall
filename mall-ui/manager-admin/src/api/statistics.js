/**
 * 统计相关API
 */

import request from '@/utils/request'

/** 会员分析 */

/**
 * 获取会员下单量
 * @param params
 */
export function getMemberOrderNum(params) {
  return request({
    url: 'admin/statistics/member/order/quantity',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取会员下单量表格数据
 * @param params
 */
export function getMemberOrderNumPage(params) {
  return request({
    url: 'admin/statistics/member/order/quantity/page',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取会员下单商品数
 * @param params
 */
export function getMemberGoodsNum(params) {
  return request({
    url: 'admin/statistics/member/order/goods/num',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取会员下单商品数表格数据
 * @param params
 */
export function getMemberGoodsNumPage(params) {
  return request({
    url: 'admin/statistics/member/order/goods/num/page',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取会员下单金额
 * @param params
 */
export function getMemberPrice(params) {
  return request({
    url: 'admin/statistics/member/order/money',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取会员下单金额表格数据
 * @param params
 */
export function getMemberPricePage(params) {
  return request({
    url: 'admin/statistics/member/order/money/page',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 新增会员统计
 * @param params
 */
export function addedMember(params) {
  return request({
    url: 'admin/statistics/member/increase/member',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 新增会员统计表格
 * @param params
 */
export function addedMemberPage(params) {
  return request({
    url: 'admin/statistics/member/increase/member/page',
    method: 'get',
    loading: false,
    params
  })
}

/** 商品统计 */

/**
 * 价格销量
 * @param params
 */
export function getPriceSales(params) {
  return request({
    url: 'admin/statistics/goods/price/sales',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取热卖商品下单金额
 * @param params
 */
export function getHotGoodsPrice(params) {
  return request({
    url: 'admin/statistics/goods/hot/money',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取热卖商品下单金额表格数据
 * @param params
 */
export function getHotGoodsPricePage(params) {
  return request({
    url: 'admin/statistics/goods/hot/money/page',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取热卖商品下单量
 * @param params
 */
export function getHotGoodsNum(params) {
  return request({
    url: 'admin/statistics/goods/hot/num',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取热卖商品下单量表格数据
 * @param params
 */
export function getHotGoodsNumPage(params) {
  return request({
    url: 'admin/statistics/goods/hot/num/page',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取收藏排行
 * @param params
 */
export function getGoodsCollectTop(params) {
  return request({
    url: 'admin/statistics/goods/collect',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取收藏排行表格数据
 * @param params
 */
export function getGoodsCollectTopPage(params) {
  return request({
    url: 'admin/statistics/goods/collect/page',
    method: 'get',
    loading: false,
    params
  })
}

/** 行业分析 */

/**
 * 获取行业规模下单量
 * @param params
 */
export function getIndustryScaleOrder(params) {
  return request({
    url: 'admin/statistics/industry/order/quantity',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取行业规模下单商品数量
 * @param params
 */
export function getIndustryScaleGoods(params) {
  return request({
    url: 'admin/statistics/industry/goods/num',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取行业规模下单金额
 * @param params
 */
export function getIndustryScalePrice(params) {
  return request({
    url: 'admin/statistics/industry/order/money',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取概括总览数据
 * @param params
 */
export function getGeneralityOverviewData(params) {
  return request({
    url: 'admin/statistics/industry/overview',
    method: 'get',
    loading: false,
    params
  })
}

/** 流量分析 */
/**
 * 获取店铺流量数据
 * @param params
 */
export function getTrafficAnalysisShop(params) {
  return request({
    url: 'admin/statistics/page_view/shop',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取商品流量数据
 * @param params
 */
export function getTrafficAnalysisGoods(params) {
  return request({
    url: 'admin/statistics/page_view/goods',
    method: 'get',
    loading: false,
    params
  })
}

/** 其它统计 */

/**
 * 订单统计 - 下单金额
 * @param params
 */
export function getOrderStatisticsPrice(params) {
  return request({
    url: 'admin/statistics/order/order/money',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 订单统计 - 下单量
 * @param params
 */
export function getOrderStatisticsOrder(params) {
  return request({
    url: 'admin/statistics/order/order/num',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 订单统计 - 下单量表格数据
 * @param params
 * @returns {Promise<any>}
 */
export function getOrderStatisticsPage(params) {
  return request({
    url: 'admin/statistics/order/order/page',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取销售收入统计表格数据
 * @param params
 */
export function getSalesRevenueStatisticsPage(params) {
  return request({
    url: 'admin/statistics/order/sales/money',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取销售收入总览
 * @param params
 */
export function getSalesRevenueStatisticsTotal(params) {
  return request({
    url: 'admin/statistics/order/sales/total',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取退单记录
 * @param params
 */
export function getSalesAftersaleStatistics(params) {
  return request({
    url: 'admin/statistics/order/aftersales/money',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取区域分析表格
 * @param params
 */
export function getRegionalAnalysisTable(params) {
  return request({
    url: 'admin/statistics/order/region/form',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取区域分析下单会员数
 * @param params
 */
export function getRegionalAnalysisMember(params) {
  return request({
    url: 'admin/statistics/order/region/member',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取区域分析下单金额
 * @param params
 */
export function getRegionalAnalysisPrice(params) {
  return request({
    url: 'admin/statistics/order/region/money',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取区域分析下单量
 * @param params
 */
export function getRegionalAnalysisNum(params) {
  return request({
    url: 'admin/statistics/order/region/num',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取客单价分布
 * @param params
 */
export function getOrderPriceDistribution(params) {
  return request({
    url: 'admin/statistics/order/unit/price',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取购买时段分布
 * @param params
 */
export function getBuyTimeDistribution(params) {
  return request({
    url: 'admin/statistics/order/unit/time',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取购买频次数据
 * @param params
 */
export function getBuyFrequency(params) {
  return request({
    url: 'admin/statistics/order/unit/num',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取退款统计
 * @param params
 */
export function getRefundStatistics(params) {
  return request({
    url: 'admin/statistics/order/return/money',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 商品销售明细
 * @param params
 */
export function getGoodsSaleDetail(params) {
  return request({
    url: 'admin/statistics/goods/sale/details',
    method: 'get',
    loading: false,
    params
  })
}
