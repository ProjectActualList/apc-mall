package com.a.platform.database.meta;

/**
 * 列数据
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 17:35
 */
public class ColumnMeta {

    /**
     * 字段名
     */
    private Object[] names;

    /**
     * 字段值
     */
    private Object[] values;

    public Object[] getNames() {
        return names;
    }

    public void setNames(Object[] names) {
        this.names = names;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }
}
