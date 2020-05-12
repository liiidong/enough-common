package com.enough.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @program: learn-redis
 * @description: json工具类
 * @author: lidong
 * @create: 2020/01/08
 */
public final class JSONUtils {

    private static final SerializeConfig config = new SerializeConfig();
    private static final SerializerFeature[] features;

    public JSONUtils() {
    }

    static {
        config.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));
        config.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));
        config.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));
        features = new SerializerFeature[] {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullStringAsEmpty};
    }

    public static <T> T parseObject(String text, Class <T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    public static String toJSONString(Object javaObj) {
        return JSON.toJSONString(javaObj);
    }

    public static byte[] toJSONBytes(Object javaObj) {
        return JSON.toJSONBytes(javaObj);
    }

    public static <T> List <T> parseArray(String str, Class <T> clazz) {
        return JSON.parseArray(str, clazz);
    }

    public static List <Object> parseArray(String jsonStr, Type[] types) {
        return JSON.parseArray(jsonStr, types);
    }

    public static <T> T toJavaObject(JSON json, Class <T> clazz) {
        return JSON.toJavaObject(json, clazz);
    }

    public static Object parse(String jsonStr) {
        return JSON.parse(jsonStr);
    }

    public static String toJSONString(Object object, SerializeFilter filter, SerializerFeature... features) {
        return JSON.toJSONString(object, filter, features);
    }

    public static String toJSONStringWithDateFormat(Object object, String dateFormat) {
        return JSON.toJSONStringWithDateFormat(object, dateFormat, features);
    }

    public static JSONObject parseObject(String jsonStr) {
        return JSON.parseObject(jsonStr);
    }

    public static <T> T parseObject(String jsonStr, Class <T> clazz, Feature... features) {
        return JSON.parseObject(jsonStr, clazz, features);
    }

    public static <T> T parseObject(String jsonStr, Type clazz, Feature... features) {
        return JSON.parseObject(jsonStr, clazz, features);
    }

    public static JSONArray parseArray(String jsonStr) {
        return JSON.parseArray(jsonStr);
    }


}
