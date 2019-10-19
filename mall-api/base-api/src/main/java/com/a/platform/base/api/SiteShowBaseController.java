package com.a.platform.base.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 站点信息首页展示控制器
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/19 17:09
 */
@RestController
@RequestMapping("/site-show")
@Api(description = "站点展示")
public class SiteShowBaseController {

    @Autowired
    private SettingManager settingManager;

    @GetMapping
    @ApiOperation(value = "获取站点设置")
    public SiteSetting getSiteSetting() {

        String siteJson = settingManager.get(SettingGroup.SITE);

        return JsonUtil.jsonToObject(siteJson,SiteSetting.class);
    }
}
