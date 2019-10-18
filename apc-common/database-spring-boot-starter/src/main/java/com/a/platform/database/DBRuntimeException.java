package com.a.platform.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 数据库操作运行期异常
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 17:35
 */
public class DBRuntimeException extends RuntimeException {

 
	private static final long serialVersionUID = -368646349014580765L;
	
	private static final Log loger = LogFactory
			.getLog(DBRuntimeException.class);

	
	public DBRuntimeException(String message){
		super(message);
	}
	public DBRuntimeException(Exception e,String sql) {
		
		super("数据库运行期异常");
		e.printStackTrace();
		if(loger.isErrorEnabled()){
			loger.error("数据库运行期异常，相关sql语句为"+ sql);
		}
	}
	
	
	public DBRuntimeException(String message,String sql) {
		
		super(message);
		if(loger.isErrorEnabled()){
			loger.error(message+"，相关sql语句为"+ sql);
		 
		}
		
	}
	
	
}
