package com.enough.common.utils;

public final class ClassLoaderUtil {
    public static ClassLoader get(Class<?> clz) {
        return clz.getClassLoader();
    }
    
    public static ClassLoader contextLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
