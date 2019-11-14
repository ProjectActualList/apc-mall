package com.a.platform.buyer.api.pagedata;

import com.a.platform.buyer.client.HotKeywordClient;
import com.a.platform.buyer.model.HotKeywordBO;
import com.a.platform.buyer.model.HotKeywordDTO;
import com.alibaba.dubbo.config.annotation.Reference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 热门关键字
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 14:59
 */
@RestController
@RequestMapping("/pages/hot-keywords")
@Api(description = "热门关键字相关API")
public class HotKeywordBuyerController {

    @Reference(validation = "true", version = "${dubbo.provider.HotKeywordClient.version}")
    private HotKeywordClient hotKeywordClient;


    @ApiOperation(value = "查询热门关键字列表", response = HotKeywordDTO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "num", value = "查询的数量", required = true, dataType = "int", paramType = "query"),
    })
    @GetMapping
    public List<HotKeywordBO> list(Integer num) {

        return this.hotKeywordClient.listByNum(num);
    }


}