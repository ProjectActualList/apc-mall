package com.a.platform.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识为数据库的字段
 *
 * @author weixing.yang
 * @version 1.1.0
 * @date 2019/10/18 17:30
 */
@Target(ElementType.FIELD) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface Column {

	String name() default "";

	boolean allowNullUpdate() default  false;


}
