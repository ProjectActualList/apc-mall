package com.a.platform.system.annotation;

import com.a.platform.system.validator.ClientMobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


/**
 * 客户端两端类型验证
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 14:16
 */
@Constraint(validatedBy = {ClientMobileValidator.class})
@Documented
@Target( {ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientMobileType {

    String message() default "不正确的客户端类型";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
