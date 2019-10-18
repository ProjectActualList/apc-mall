package com.a.platform.database.impl;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * map mapper
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 17:34
 */
public class FilterColumnMapRowMapper extends ColumnMapRowMapper {
    private IRowMapperColumnFilter filter;

    public FilterColumnMapRowMapper(IRowMapperColumnFilter filter) {
        this.filter = filter;
    }

    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Map mapOfColValues = createColumnMap(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
            key = key.toLowerCase();
            Object obj = getColumnValue(rs, i);
            mapOfColValues.put(key, obj);
            //对此行结果集进行过滤
            this.filter.filter(mapOfColValues, rs);
        }
        return mapOfColValues;
    }
}
