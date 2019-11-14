package com.a.platform.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 资源找不到异常
 *
 * @author Kingapex
 * @version v1.0
 * @since v7.0.0
 * 2017年8月15日 下午1:02:50
 */
public class ResourceNotFoundException extends ServiceException {


    private static final long serialVersionUID = -6945068834935110333L;


    public ResourceNotFoundException(String message) {

        super(SystemErrorCode.RESOURCE_NOT_FOUND, message);
        this.statusCode = HttpStatus.NOT_FOUND;

    }
}
