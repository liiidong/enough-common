package com.enough.common.rest.exceptions;

public class ShiroException extends RuntimeException  {
    private static final long serialVersionUID = 1L;
    private static final int HTTPSTATUS = 401;

    public static int getHTTPSTATUS() {
        return HTTPSTATUS;
    }
}


