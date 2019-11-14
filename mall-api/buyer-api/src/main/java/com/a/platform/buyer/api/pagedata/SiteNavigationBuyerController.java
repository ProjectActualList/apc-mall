package com.a.platform.buyer.api.pagedata;

import com.a.platform.buyer.client.SiteNavigationClient;
import com.a.platform.buyer.model.SiteNavigationBO;
import com.a.platform.buyer.model.SiteNavigationDTO;
import com.alibaba.dubbo.config.annotation.Reference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


/**
 * 导航栏
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 14:06
 */
@RestController
@RequestMapping("/pages/site-navigations")
@Api(description = "导航栏相关API")
public class SiteNavigationBuyerController {

    @Reference
    private SiteNavigationClient siteNavigationClient;


    @ApiOperation(value = "查询导航栏列表", response = SiteNavigationDTO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "client_type", value = "客户端类型", required = true, dataType = "string", paramType = "query", allowableValues = "PC,MOBILE"),
    })
    @GetMapping
    public List<SiteNavigationBO> list(@ApiIgnore String clientType) {

        return this.siteNavigationClient.listByClientType(clientType);
    }
}