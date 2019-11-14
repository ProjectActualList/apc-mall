package com.a.platform.system.validator;

import com.a.platform.system.annotation.ClientMobileType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * ClientMobileType 验证
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 14:17
 */
public class ClientMobileValidator implements ConstraintValidator<ClientMobileType, String> {

    private final String[] ALL_STATUS = {"PC", "MOBILE"};

    @Override
    public void initialize(ClientMobileType status) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(ALL_STATUS).contains(value);
    }
}
