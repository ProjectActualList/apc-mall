package com.a.platform.base.api;

import com.a.platform.base.client.SettingClient;
import com.a.platform.base.model.SettingGroup;
import com.a.platform.base.model.SiteSettingBO;
import com.a.platform.common.util.JsonUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 站点信息首页展示
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 14:04
 */
@RestController
@RequestMapping("/site-show")
@Api(description = "站点展示")
public class SiteShowBaseController {

    @Reference(validation = "true", version = "${dubbo.consumer.SettingClient.version:1.0}")
    private SettingClient settingClient;

    @GetMapping
    @ApiOperation(value = "获取站点设置")
    public SiteSettingBO getSiteSetting() {

        String siteJson = settingClient.get(SettingGroup.SITE);

        return JsonUtil.jsonToObject(siteJson,SiteSettingBO.class);
    }
}
