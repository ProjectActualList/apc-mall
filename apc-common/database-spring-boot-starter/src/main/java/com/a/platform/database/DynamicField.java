package com.a.platform.database;

import com.a.platform.database.annotation.NotDbField;

import java.util.HashMap;
import java.util.Map;


/**
 * 动态字段
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 17:36
 */
public class DynamicField {

    private Map<String, Object> fields;

    public DynamicField() {
        fields = new HashMap<String, Object>();
    }

    public void addField(String name, Object value) {
        fields.put(name, value);
    }

    @NotDbField
    public Map<String, Object> getFields() {
        return fields;
    }
}
