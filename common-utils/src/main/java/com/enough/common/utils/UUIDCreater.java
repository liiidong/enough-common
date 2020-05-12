package com.enough.common.utils;

import java.util.UUID;

/**
 * @program: common-utils
 * @description: UUID生成器
 * @author: liiidong
 * @create: 2020/03/08
 */
public class UUIDCreater {

    private static volatile UUIDCreater uuidCreater;

    private UUIDCreater() {
    }

    /**
     * 单例，双重锁校验，提高性能
     *
     * @return
     */
    public static UUIDCreater getInstance() {
        if (uuidCreater == null) {
            synchronized (UUIDCreater.class) {
                if (uuidCreater == null) {
                    uuidCreater = new UUIDCreater();
                }
            }
        }
        return uuidCreater;
    }

    public String getID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
