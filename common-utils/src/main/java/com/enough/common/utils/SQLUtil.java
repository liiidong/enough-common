package com.enough.common.utils;

/**
 * @author lidong
 * @apiNote sql工具类
 * @program datacube-server
 * @since 2020/09/14
 */
public class SQLUtil {

    public static final String TO_FLOAT_64 = "toFloat64";

    public static String as(String str, String alias) {
        return str.concat(" AS \"".concat(alias).concat("\""));
    }

    public static String round(String str) {
        return "round(".concat(str).concat(",3)");
    }

    public static String roundAs(String str, String alias) {
        return as("round(".concat(str).concat(",3)"), alias);
    }

    public static String sum(String str) {

        return "SUM(".concat(str).concat(")");
    }

    public static String avg(String str) {
        return "avg(".concat(str).concat(")");
    }

    public static String any(String str) {
        return "any(".concat(str).concat(") AS \"" + str + "\"");
    }

    public static String toFloat64(String str) {
        return TO_FLOAT_64.concat("(").concat(str).concat(")");
    }

    public static String concat(String... str) {
        String finalStr = String.join(",", str);
        return "concat(" + finalStr + ")";
    }

    public static String upper(String name) {
        return "upper(" + name + ")";
    }
    public static String lower(String name) {
        return "lower(" + name + ")";
    }

    public static String upperLike(String col,String val) {
        return "upper(`" + col + "`) LIKE " + "upper('%" + val + "%')" ;
    }

    public static String lowerLike(String col,String val) {
        return "lower(`" + col + "`) LIKE " + "lower('%" + val + "%')" ;
    }
}
