package com.a.platform.database.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 字符串型mapper
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 17:39
 */
public class StringMapper implements RowMapper<String> {


    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        String str = rs.getString(1);
        return str;
    }

}
