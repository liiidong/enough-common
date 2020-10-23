package com.enough.common.utils;

import com.enough.common.model.GlobalException;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author lidong
 * @apiNote 断言工具类，简单封装下类似spring的Assert
 * @program datacube-server
 * @since 2020/09/22
 */
public class AssertUtil {
    /**
     * expression is not true,throw exception
     * @param expression
     * @param message
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new GlobalException(message);
        }
    }

    /**
     * obj is not null，throw exception
     * @param object
     * @param message
     */
    public static void isNull(@Nullable Object object, String message) {
        if (object != null) {
            throw new GlobalException(message);
        }
    }

    /**
     * obj is null，throw exception
     * @param object
     * @param message
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new GlobalException(message);
        }
    }

    public static void notEmpty(@Nullable Collection <?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new GlobalException(message);
        }
    }

    public static void notBlank(@Nullable String str, String message) {
        if (!StringUtils.hasLength(str)) {
            throw new GlobalException(message);
        }
    }

    public static <T> T requireNonNull(T obj, String msg) {
        if (obj == null) {
            throw new GlobalException(msg);
        }
        return obj;
    }

    public static <T> T requireNonNull(T obj) {
        if (obj == null) {
            throw new GlobalException("参数不能为空!");
        }
        return obj;
    }

    /**
     * 判断map是否包含另一个map
     * value类型只能是基本类型
     *
     * @param baseMap
     * @param refMap
     * @return
     */
    public static boolean contains(Map <String, Object> baseMap, Map <String, Object> refMap) {
        if (!baseMap.keySet().containsAll(refMap.keySet())) {
            return false;
        }
        for (String s : refMap.keySet()) {
            if (!String.valueOf(baseMap.get(s)).equals(String.valueOf(refMap.get(s)))) {
                return false;
            }
        }
        return true;
    }

    public static String toString(Object object) {
        return object == null ? "" : String.valueOf(object);
    }
}
