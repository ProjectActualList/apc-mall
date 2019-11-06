/**
 * Created by Andste on 2018/7/15.
 * 促销相关API
 */

import request, { Method } from '@/utils/request'

/**
 * 获取商品促销活动列表
 * @param goods_id
 */
export function getGoodsPromotions(goods_id) {
  return request({
    url: `promotions/${goods_id}`,
    method: Method.GET
  })
}

/**
 * 获取团购分类
 */
export function getGroupBuyCategorys() {
  return request({
    url: 'promotions/group-buy/cats',
    method: Method.GET
  })
}

/**
 * 获取团购商品
 * @param params
 */
export function getGroupBuyGoods(params) {
  return request({
    url: `promotions/group-buy/goods`,
    method: Method.GET,
    loading: false,
    params
  })
}

/**
 * 获取团信息
 * @param gb_id
 */
export function getGroupBuyDetail(gb_id) {
  return request({
    url: `promotions/group-buy/active`,
    method: Method.GET
  })
}

/**
 * 获取店铺优惠券
 * @param seller_id
 */
export function getShopCoupons(seller_id) {
  return request({
    url: 'promotions/coupons',
    method: Method.GET,
    params: { seller_id }
  })
}

/**
 * 获取积分商城分类
 */
export function getPointsCategory() {
  return request({
    url: 'promotions/exchange/cats',
    method: Method.GET
  })
}

/**
 * 获取积分商城商品
 * @param params
 */
export function getPointsGoods(params) {
  return request({
    url: `promotions/exchange/goods`,
    method: Method.GET,
    loading: false,
    params
  })
}

/**
 * 获取限时抢购时间线
 */
export function getSeckillTimeLine() {
  return request({
    url: 'promotions/seckill/time-line',
    method: Method.GET
  })
}

/**
 * 获取限时抢购商品
 * @param params
 */
export function getSeckillTimeGoods(params) {
  return request({
    url: 'promotions/seckill/goods-list',
    method: Method.GET,
    loading: false,
    params
  })
}

/**
 * 获取全部优惠券
 * @param params
 */
export function getAllCoupons(params) {
  return request({
    url: 'promotions/coupons/all',
    method: Method.GET,
    loading: false,
    params
  })
}

/** 获取拼团列表 */
export function getAssembleList(params) {
  return request({
    url: 'pintuan/goods/skus',
    method: Method.GET,
    loading: false,
    params
  })
}

/**
 * 获取拼团商品sku的详细
 * @param sku_id
 */
export function getAssembleDetail(sku_id) {
  return request({
    url: `pintuan/goods/skus/${sku_id}`,
    method: Method.GET,
    loading: false
  })
}

/**
 * 获取此商品sku_id待成团的订单
 * @param goods_id
 */
export function getAssembleOrderList(goods_id, params) {
  return request({
    url: `pintuan/goods/${goods_id}/orders`,
    method: Method.GET,
    loading: false,
    params
  })
}

/***
 * 获取当前商品所有参与拼团的sku规格列表
 * @param goods_id
 */
export function getAssembleSkuList(goods_id) {
  return request({
    url: `pintuan/goods/${goods_id}/skus`,
    method: Method.GET,
    loading: false
  })
}
