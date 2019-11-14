package com.a.platform.database.meta;

import java.util.Arrays;

/**
 * 元数据
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 17:35
 */
public class DataMeta {

    /**
     * sql 语句
     */
    private String sql;

    /**
     * sql 语句中需要的参数
     */
    private Object[] paramter;


    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getParamter() {
        return paramter;
    }

    public void setParamter(Object[] paramter) {
        this.paramter = paramter;
    }

    @Override
    public String toString() {
        return "DataMeta{" +
                "sql='" + sql + '\'' +
                ", paramter=" + Arrays.toString(paramter) +
                '}';
    }
}
