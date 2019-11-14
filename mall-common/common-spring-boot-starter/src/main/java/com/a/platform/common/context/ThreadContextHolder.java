package com.a.platform.common.context;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 用ThreadLocal来存储Session,以便实现Session any where
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 17:46
 */
public class ThreadContextHolder {

    private static ThreadLocal<HttpServletRequest> requestThreadLocalHolder = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> responseThreadLocalHolder = new ThreadLocal<HttpServletResponse>();


    public static void setHttpRequest(HttpServletRequest request) {

        requestThreadLocalHolder.set(request);
    }

    public static void setHttpResponse(HttpServletResponse response) {
        responseThreadLocalHolder.set(response);
    }


    public static void remove() {
        requestThreadLocalHolder.remove();
        responseThreadLocalHolder.remove();
    }


    public static HttpServletResponse getHttpResponse() {

        return responseThreadLocalHolder.get();
    }

    public static HttpServletRequest getHttpRequest() {
        return requestThreadLocalHolder.get();
    }


}
