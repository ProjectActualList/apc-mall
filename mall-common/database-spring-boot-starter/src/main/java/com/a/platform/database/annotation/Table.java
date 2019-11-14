package com.a.platform.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识为数据库的表名
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 17:32
 */
@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface Table {

	/**
	 * 数据库的表名
	 * @return
	 */
	String name();
}
