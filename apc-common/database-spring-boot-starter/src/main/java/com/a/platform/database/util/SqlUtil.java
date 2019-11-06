package com.a.platform.database.util;

import com.a.platform.common.util.StringUtil;

import java.util.List;

/**
 * sql语句拼接工具类
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 16:57
 */
public class SqlUtil {
    /**
     * sql拼接
     *
     * @param list
     * @return
     */
    public static String sqlSplicing(List<String> list) {
        StringBuffer sql = new StringBuffer("");
        if (list.size() > 0) {
            sql.append(" where ");
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    sql.append(list.get(i) + " ");
                } else {
                    sql.append(list.get(i) + " and ");
                }
            }
        }
        return sql.toString();
    }

    /**
     * 拼接成sql的?形式，term中会将对应的值加上
     *
     * @param list
     * @param term
     * @return
     */
    public static String getInSql(Integer[] list, List<Object> term) {

        String[] temp = new String[list.length];
        for (int i = 0; i < list.length; i++) {
            temp[i] = "?";
            term.add(list[i]);
        }

        return StringUtil.arrayToString(temp, ",");

    }

    /****
     * 处理sql，将条件变成？可以自行传递参数进去
     * @param params
     * @return
     */
    public static String getInSql(Integer[] params) {
        String[] temp = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            temp[i] = "?";
        }

        return StringUtil.arrayToString(temp, ",");

    }


}
