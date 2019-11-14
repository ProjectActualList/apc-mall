package com.a.platform.database.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 数据库结果集过滤器
 * 可对RowMapper的结果某或多列进行过滤
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 17:34
 */
public interface IRowMapperColumnFilter {


    /**
     * 对结果集的行进行过滤
     *
     * @param colValues 结果集一行的map
     * @param rs        结果集
     * @throws SQLException 可能的sql异常
     */
    public void filter(Map colValues, ResultSet rs) throws SQLException;


}
