package com.a.platform.database;

/**
 * sql文件执行器
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 17:36
 */
public interface ISqlFileExecutor {

    /**
     * 批量执行sql语句
     */
    public void execute(String sql);
}
