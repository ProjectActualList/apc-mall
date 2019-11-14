package com.a.platform.database.util;

import com.a.platform.common.util.StringUtil;
import com.a.platform.database.meta.ColumnMeta;
import com.a.platform.database.annotation.Column;
import com.a.platform.database.annotation.Id;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 反射机制工具类
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 17:48
 */
public class ReflectionUtil {


    /**
     * 将po对象中有属性和值转换成map
     *
     * @param po
     * @return
     */
    @SuppressWarnings("AlibabaCollectionInitShouldAssignCapacity")
    public static Map po2Map(Object po) {
        Map poMap = new HashedMap();
        ColumnMeta columnMeta = getColumnMeta(po);

        Object[] columnName = columnMeta.getNames();
        Object[] columnValue = columnMeta.getValues();

        for (int i = 0; i < columnValue.length; i++) {
            poMap.put(columnName[i], columnValue[i]);
        }

        return poMap;
    }


    /**
     * 遍历实体类的属性和数据类型以及属性值
     *
     * @param model
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static ColumnMeta getColumnMeta(Object model) {

        ColumnMeta columnMeta = new ColumnMeta();

        try {
            // 获取实体类的所有属性，返回Field数组
            Field[] fields = model.getClass().getDeclaredFields();
            //属性名
            List columnName = new ArrayList();
            //属性值
            List columnValue = new ArrayList();

            /**
             * 遍历所有的属性，过滤掉满足以下条件的属性。
             * 1、属性值为null的数据
             * 2、没有添加自定义标签 @id 和 @Column 的属性
             */
            for (Field field : fields) {

                if (field.isAnnotationPresent(Column.class)) {

                    String dbName = field.getName();
                    Column column = field.getAnnotation(Column.class);
                    if (!StringUtil.isEmpty(column.name())) {
                        dbName = column.name();
                    }
                    ReflectionUtils.makeAccessible(field);
                    Object value = ReflectionUtils.getField(field, model);
                    if (value == null && !column.allowNullUpdate()) {
                        continue;
                    }

                    columnName.add(dbName);
                    columnValue.add(value);
                }
            }

            columnMeta.setNames(columnName.toArray());
            columnMeta.setValues(columnValue.toArray());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return columnMeta;
    }

    /**
     * 根据Class读取主键的字段名
     *
     * @param clazz
     * @return
     */
    public static String getPrimaryKey(Class clazz) {
        String columnId = "";
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //主键字段名
            if (field.isAnnotationPresent(Id.class)) {
                Id id = field.getAnnotation(Id.class);
                String columnIdName = id.name();

                //是否自定义的属性名
                if (StringUtil.isEmpty(columnIdName)) {
                    columnId = field.getName();
                } else {
                    columnId = columnIdName;
                }
                break;
            }
        }
        return columnId;
    }


    private static String getFieldName(String methodName) {

        methodName = methodName.substring(3);
        methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
        return methodName;
    }
}
