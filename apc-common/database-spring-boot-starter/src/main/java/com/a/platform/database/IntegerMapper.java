package com.a.platform.database;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 整型mapper
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 17:36
 */
public class IntegerMapper implements RowMapper<Integer> {

    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer v = rs.getInt(1);
        return v;
    }

}
