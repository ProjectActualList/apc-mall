package com.a.platform.common.core;

/**
 * 图片验证码业务类型
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 15:50
 */
public enum SceneType {

    //验证码登录
    LOGIN("验证码登录"),

    //手机注册
    REGISTER("手机注册"),

    //找回密码
    FIND_PASSWORD("找回密码"),

    //绑定手机
    BIND_MOBILE("绑定手机"),

    //修改密码
    MODIFY_PASSWORD("修改密码"),

    //添加店员
    ADD_CLERK("添加店员"),

    //手机验证
    VALIDATE_MOBILE("验证手机");

    private String description;

    SceneType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}
