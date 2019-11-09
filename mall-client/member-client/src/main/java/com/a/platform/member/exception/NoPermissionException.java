package com.a.platform.member.exception;

import com.a.platform.common.exception.ServiceException;
import com.a.platform.common.exception.SystemErrorCode;
import org.springframework.http.HttpStatus;

/**
 * 无权限异常，比如试图更新一个别人的账号的密码
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/9 18:44
 */
public class NoPermissionException extends ServiceException {

    private static final long serialVersionUID = 8207742972948289957L;

    public NoPermissionException(String message) {
        super(SystemErrorCode.NO_PERMISSION, message);
        this.statusCode = HttpStatus.UNAUTHORIZED;
    }


}
