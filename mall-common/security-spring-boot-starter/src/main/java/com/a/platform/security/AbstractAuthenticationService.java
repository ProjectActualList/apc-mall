package com.a.platform.security;

import com.a.platform.common.util.DateUtil;
import com.a.platform.common.util.StringUtil;
import com.a.platform.common.util.TokenKeyGenerate;
import com.a.platform.redis.cache.Cache;
import com.a.platform.security.model.JWTConstant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;

/**
 * 公共安全配置
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 11:22
 */
public abstract class AbstractAuthenticationService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private Cache cache;

    @Value("${spring.cloud.config.profile:dev}")
    private String profile;

    /**
     * 获取token
     * 生产环境要由nonce和时间戳，签名来获取token
     * 开发环境可以直接传token
     *
     * @param req
     * @return
     */
    public String getToken(HttpServletRequest req) {

        String token = null;
        if ("dev".equals(profile)) {
            token = req.getHeader(JWTConstant.HEADER_STRING);
            if (StringUtil.notEmpty(token)) {
                token = token.replaceAll(JWTConstant.TOKEN_PREFIX, "").trim();
            }
        }
        //生产环境要防重放攻击，token由redis中获取
        if ("prod".equals(profile)) {
            //用户id
            String uid = req.getParameter("uid");
            //时间戳
            String timestamp = req.getParameter("timestamp");
            //随机数
            String nonce = req.getParameter("nonce");
            //签名
            String clientSign = req.getParameter("sign");
            //uuid
            String uuid = req.getHeader("uuid");
            //如果传递参数没有值，则为无权
            if (StringUtil.isEmpty(uid) || StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(nonce) || StringUtil.isEmpty(clientSign)) {
                return null;
            }

            //服务器当前时间。
            long currTime = DateUtil.getDateline();

            //将时间戳转换long类型 用于计算
            long clientTimes = Long.parseLong(timestamp);
            //当前时间大于时间戳60秒即为失效。
            if ((currTime - clientTimes) > JWTConstant.INVALID_TIME) {
                if (logger.isDebugEnabled()) {
                    logger.debug("服务器时间：" + currTime);
                    logger.debug("客户端时间：" + clientTimes);
                    logger.debug("验权时间戳超时，判为重放攻击 ");
                }
                return null;
            }

            String key = "nonce" + uid + "_" + nonce;
            String value = (String) this.cache.get(key);
            //如果redis中存在说明已经被使用过
            if (value != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug(" nonce[" + nonce + "] 被使用过,判为重放攻击  ");
                }
                return null;
            } else {
                //客户端时间戳秒数+失效秒数
                this.cache.put(key, "used", JWTConstant.INVALID_TIME);
            }
            //读取用户的id

            token = StringUtil.toString(cache.get(TokenKeyGenerate.generateBuyerAccessToken(uuid, Integer.parseInt(uid))));
            //验证签名 uid+ nonce + timestamp +token
            String serverSign = StringUtil.md5(uid + nonce + timestamp + token);

            if (!clientSign.equals(serverSign)) {

                if (logger.isDebugEnabled()) {
                    logger.debug("key is " + "ACCESS_TOKEN_" + uid + uuid);
                    logger.debug(" 服务器token: " + token);
                    logger.debug("服务器签名为：" + serverSign);
                    logger.debug("clientSign " + clientSign);
                    logger.debug(" 签名失败,判为重放攻击 ");
                }
                return null;
            }
        }
        return token;
    }

    protected String getUuid(HttpServletRequest req) {
        String uuid = req.getHeader("uuid");
        return uuid;
    }

    public static void main(String[] args) {
        //uid=16&nonce=3IIN0M&timestamp=1539253041&sign=d963ab81c9cef348d2733c7798aed506
        String uid = "16";
        String nonce = "3IIN0M";
        String timestamp = "1539253041";
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzZWxmT3BlcmF0ZWQiOjAsInN1YiI6IkNMRVJLIiwicm9sZSI6IlNVUEVSX1NFTExFUiIsImZvdW5kZXIiOm51bGwsInJvbGVzIjpbIkJVWUVSIiwiU0VMTEVSIiwiQ0xFUksiXSwic2VsbGVyTmFtZSI6Iumjn-WTgemFkuawtCIsImNsZXJrSWQiOjQsInV1aWQiOm51bGwsInVpZCI6MTYsInNlbGxlcklkIjoxNSwiY2xlcmtOYW1lIjoiZm9vZCIsImV4cCI6MTUzOTI1NDg0MCwidXNlcm5hbWUiOiJmb29kIn0.rHlV4mfV04YiON2TandK1geRXSVmKKRAn89iAWHKNR_wuaqZC4oz8qjKusgWudCi-9ul8OwZnqzmpvh_W5pPhA";
        String serverSign = StringUtil.md5(uid + nonce + timestamp + token);
        System.out.println(serverSign);
    }
}
