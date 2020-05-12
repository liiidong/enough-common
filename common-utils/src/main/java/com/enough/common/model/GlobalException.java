package com.enough.common.model;

/**
 * @program: common-utils
 * @description: 自定义全局异常
 * @author: lidong
 * @create: 2020/01/22
 */
public class GlobalException extends RuntimeException {

    private long code;
    private String msg;

    public GlobalException(long code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public GlobalException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
