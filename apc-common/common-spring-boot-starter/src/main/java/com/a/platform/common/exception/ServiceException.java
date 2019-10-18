package com.a.platform.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 服务异常类,各业务异常需要继承此异常
 * 业务类如有不能处理异常也要抛出此异常
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 18:13
 */
public class ServiceException extends RuntimeException {

    protected HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

    private String code;
    /**
     * 要返回前端的数据
     */
    private Object data;


    public ServiceException(String code, String message) {
        super(message);
        this.code = code;

    }

    public ServiceException(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getCode() {
        return code;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
