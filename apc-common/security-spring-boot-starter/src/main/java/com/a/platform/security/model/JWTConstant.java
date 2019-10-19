package com.a.platform.security.model;

/**
 * JWT常量
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/19 16:42
 */
public class JWTConstant {

    /**
     * 10 days
     */
    public static final String SECRET = "ThisIsASecret";

    public static final String TOKEN_PREFIX = "Bearer";

    public static final String HEADER_STRING = "Authorization";

    /**
     * 时间戳失效时间，单位：秒
     */
    public static final int INVALID_TIME = 60;
}
