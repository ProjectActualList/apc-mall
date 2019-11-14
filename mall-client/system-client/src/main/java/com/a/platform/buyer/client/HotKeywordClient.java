package com.a.platform.buyer.client;


import com.a.platform.buyer.model.HotKeywordBO;
import com.a.platform.buyer.model.HotKeywordDTO;
import com.a.platform.common.result.Page;

import java.util.List;


/**
 * 热门关键字
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 14:55
 */
public interface HotKeywordClient {

    /**
     * 查询热门关键字列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return Page
     */
    Page list(int page, int pageSize);

    /**
     * 添加热门关键字
     *
     * @param hotKeyword 热门关键字
     * @return HotKeyword 热门关键字
     */
    HotKeywordBO add(HotKeywordDTO hotKeyword);

    /**
     * 修改热门关键字
     *
     * @param hotKeyword 热门关键字
     * @param id         热门关键字主键
     * @return HotKeyword 热门关键字
     */
    HotKeywordBO edit(HotKeywordDTO hotKeyword, Integer id);

    /**
     * 删除热门关键字
     *
     * @param id 热门关键字主键
     */
    void delete(Integer id);

    /**
     * 获取热门关键字
     *
     * @param id 热门关键字主键
     * @return HotKeyword  热门关键字
     */
    HotKeywordBO getModel(Integer id);

    /**
     * 查询热门关键字
     *
     * @param num
     * @return
     */
    List<HotKeywordBO> listByNum(Integer num);
}