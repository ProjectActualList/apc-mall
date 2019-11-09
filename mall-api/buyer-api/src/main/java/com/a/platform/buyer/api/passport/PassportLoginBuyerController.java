package com.a.platform.buyer.api.passport;

import com.a.platform.base.service.client.CaptchaClient;
import com.a.platform.common.core.SceneType;
import com.a.platform.common.exception.ServiceException;
import com.a.platform.member.client.MemberClient;
import com.a.platform.member.client.PassportClient;
import com.a.platform.member.exception.MemberErrorCode;
import com.a.platform.member.model.dto.MemberFaceBO;
import com.alibaba.dubbo.config.annotation.Reference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotEmpty;

/**
 * 会员登录注册API
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 15:43
 */
@RestController
@RequestMapping("/passport")
@Api(description = "会员登录API")
@Validated
public class PassportLoginBuyerController {

    @Reference
    private PassportClient passportClient;

    @Reference
    private CaptchaClient captchaClient;

    @Reference
    private MemberClient memberClient;


    @PostMapping(value = "/login/smscode/{mobile}")
    @ApiOperation(value = "发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "uuid客户端的唯一标识", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "captcha", value = "图片验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String", paramType = "path"),
    })
    public String sendSmsCode(@NotEmpty(message = "uuid不能为空") String uuid, @NotEmpty(message = "图片验证码不能为空") String captcha, @PathVariable("mobile") String mobile) {
        boolean isPass = captchaClient.valid(uuid, captcha, SceneType.LOGIN.name());
        if (!isPass) {
            throw new ServiceException(MemberErrorCode.E107.code(), "图片验证码不正确！");
        }
        passportClient.sendLoginSmsCode(mobile);
        //清清除图片验证码信息
        captchaClient.deleteCode(uuid, captcha, SceneType.LOGIN.name());
        return null;
    }

    @GetMapping("/login")
    @ApiOperation(value = "用户名（手机号）/密码登录API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "captcha", value = "验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "uuid", value = "客户端唯一标识", required = true, dataType = "String", paramType = "query"),
    })
    public MemberFaceBO login(@NotEmpty(message = "用户名不能为空") String username, @NotEmpty(message = "密码不能为空") String password, @NotEmpty(message = "图片验证码不能为空") String captcha, @NotEmpty(message = "uuid不能为空") String uuid) {
        //验证图片验证码是否正确
        boolean isPass = captchaClient.valid(uuid, captcha, SceneType.LOGIN.name());
        if (!isPass) {
            throw new ServiceException(MemberErrorCode.E107.code(), "图片验证码错误！");
        }
        //校验账号信息是否正确
        return memberClient.login(username, password, 1);
    }

    @GetMapping("/login/{mobile}")
    @ApiOperation(value = "手机号码登录API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "sms_code", value = "手机验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "uuid", value = "客户端唯一标识", required = true, dataType = "String", paramType = "query")
    })
    public MemberFaceBO mobileLogin(@PathVariable String mobile, @ApiIgnore @NotEmpty(message = "短信验证码不能为空") String smsCode) {
        boolean isPass = true;//smsClient.valid(SceneType.LOGIN.name(), mobile, smsCode);
        if (!isPass) {
            throw new ServiceException(MemberErrorCode.E107.code(), "短信验证码错误！");
        }
        return memberClient.mobileLogin(mobile, 1);

    }
}
