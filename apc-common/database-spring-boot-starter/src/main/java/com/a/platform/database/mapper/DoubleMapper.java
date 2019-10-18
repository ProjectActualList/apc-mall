package com.a.platform.database.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 浮点型mapper
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 17:36
 */
public class DoubleMapper implements RowMapper<Double> {

	
	@Override
    public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
		Double dobule = new Double(rs.getDouble(1));
		return dobule;
	}

}
