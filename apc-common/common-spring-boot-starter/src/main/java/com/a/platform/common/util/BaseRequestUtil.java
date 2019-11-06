package com.a.platform.common.util;

import com.a.platform.common.context.ThreadContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * httpRequest常用方法工具
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 14:57
 */
public abstract class BaseRequestUtil {


    private BaseRequestUtil() {
    }

    /**
     * 获取完整回调域名
     * 思路：如果域名包含协议则直接返回，反之拼接协议和域名返回
     *
     * @param scheme 协议
     * @param domain 域名
     * @return 完整域名
     */
    public static String getDomain(String scheme, String domain) {
        if (domain == null) {
            domain = "domain";
        }
        if (domain.indexOf("http") != -1) {
            return domain;
        }
        return scheme + domain;
    }


    private final static Set<String> PublicSuffixSet = new HashSet<String>(
            Arrays.asList(new String(
                    "com|org|net|gov|edu|co|tv|mobi|info|asia|xxx|onion|cn|com.cn|edu.cn|gov.cn|net.cn|org.cn|jp|kr|tw|com.hk|hk|com.hk|org.hk|se|com.se|org.se")
                    .split("\\|")));

    /**
     * 获取url的顶级域名
     *
     * @param host
     * @return
     */
    public static String getDomainName(String host) {
        if (host.endsWith(".")) {
            host = host.substring(0, host.length() - 1);
        }
        int index = 0;
        String candidate = host;
        for (; index >= 0; ) {
            index = candidate.indexOf('.');
            String subCandidate = candidate.substring(index + 1);
            if (PublicSuffixSet.contains(subCandidate)) {
                return candidate;
            }
            candidate = subCandidate;
        }
        return candidate;
    }

    /**
     * 检测是不是手机访问ba
     *
     * @return true为是false为不是手机访问
     */
    public static boolean isMobile() {

        HttpServletRequest request = ThreadContextHolder.getHttpRequest();
        if (request == null) {
            return false;
        }
        String userAgent = request.getHeader("user-agent");
        if (StringUtil.isEmpty(userAgent)) {
            return false;
        }

        userAgent = userAgent.toLowerCase();

        return userAgent.contains("android") || userAgent.contains("iphone");

    }

}
