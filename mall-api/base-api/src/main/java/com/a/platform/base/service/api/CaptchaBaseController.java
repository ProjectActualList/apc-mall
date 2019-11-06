package com.a.platform.base.service.api;

import com.a.platform.base.service.service.CaptchaService;
import com.alibaba.dubbo.config.annotation.Reference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 验证码生成
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 15:06
 */
@RestController
@RequestMapping("/captchas")
@Api(description = "验证码api")
public class CaptchaBaseController {

    @Reference
    private CaptchaService captchaService;

    @GetMapping(value = "/{uuid}/{scene}")
    @ApiOperation(value = "生成验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "uuid客户端的唯一标识", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "scene", value = "业务类型", required = true, dataType = "String", paramType = "path")
    })
    public String getCode(@PathVariable("uuid") String uuid, @PathVariable("scene") String scene) {

        //直接调取业务类，由业务类输出流到浏览器
        this.captchaService.writeCode(uuid, scene);

        return null;
    }
}
