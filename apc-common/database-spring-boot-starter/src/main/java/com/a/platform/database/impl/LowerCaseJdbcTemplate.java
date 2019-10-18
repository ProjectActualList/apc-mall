package com.a.platform.database.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * 覆写jdbctemlate ，使用LowerCaseColumnMapRowMapper
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 17:34
 */
public class LowerCaseJdbcTemplate extends JdbcTemplate {
    @Override
    protected RowMapper getColumnMapRowMapper() {

        return new MySqlColumnMapRowMapper();

    }

}
