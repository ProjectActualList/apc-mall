package com.a.platform.common.model;

import java.lang.annotation.*;

/**
 * 地区注解
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 16:34
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface RegionFormat {

}
