package com.enough.common.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * @program: learn-redis
 * @description: rest层统一返回结果
 * @author: lidong
 * @create: 2019/11/15
 */
public class ReturnResult<T> implements Serializable {

    private static final long serialVersionUID = -1393104860022774284L;

    private String msg;
    private Integer status;
    private boolean successed;
    private T data;
    private String newResource;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccessed() {
        return successed;
    }

    public void setSuccessed(boolean successed) {
        this.successed = successed;
    }

    public String getNewResource() {
        return newResource;
    }

    public void setNewResource(String newResource) {
        this.newResource = newResource;
    }

    @Override
    public String toString() {
        return "ReturnResult{" + "msg='" + msg + '\'' + ", status=" + status + ", successed=" + successed + ", data=" + data + ", newResource='" + newResource
                + '\'' + '}';
    }

    public enum Status {
        /**
         * 成功
         */
        SUCCESS,
        /**
         * 失败
         */
        FAULT
    }

    public static <T> ReturnResult.ResultBuilder <T> success(Class <T> clazz) {
        ReturnResult.ResultBuilder <T> builder = new ReturnResult.ResultBuilder <>();
        builder.successed(true);
        return builder;
    }

    public static <T> ReturnResult.ResultBuilder <T> failed(Class <T> clazz) {
        ReturnResult.ResultBuilder <T> builder = new ReturnResult.ResultBuilder <>();
        builder.successed(false);
        return builder;
    }

    public static <T> ReturnResult.ResultBuilder <T> structure(boolean isSuccess, Class <T> clazz) {
        ReturnResult.ResultBuilder <T> builder = new ReturnResult.ResultBuilder <>();
        builder.successed(isSuccess);
        return builder;
    }

    public static class ResultBuilder<T> {
        private String msg;
        private boolean successed;
        private T data;
        private String newResource;
        private Integer status;

        public ResultBuilder() {

        }

        public ReturnResult.ResultBuilder <T> msg(String msg) {
            this.msg = msg;
            return this;
        }

        public ReturnResult.ResultBuilder <T> successed(boolean successed) {
            this.successed = successed;
            return this;
        }

        public ReturnResult.ResultBuilder <T> data(T data) {
            this.data = data;
            return this;
        }

        public ReturnResult.ResultBuilder <T> newResource(String newResource) {
            this.newResource = newResource;
            return this;
        }

        public ReturnResult.ResultBuilder <T> status(Integer status) {
            this.status = status;
            return this;
        }

        public ReturnResult <T> build() {
            ReturnResult <T> returnResult = new ReturnResult <>();
            returnResult.setMsg(this.msg);
            returnResult.setStatus(this.status);
            returnResult.setData(this.data);
            returnResult.setSuccessed(this.successed);
            returnResult.setNewResource(this.newResource);
            return returnResult;
        }
    }

    public void test(String a, String b, String c) {
        System.out.println("a=" + a);
        System.out.println("b=" + b);
        System.out.println("c=" + c);
    }

    public static void main(String[] args) {
        ReturnResult <String> returnResult = CommonBuilder.of(ReturnResult <String>::new).with(ReturnResult::setSuccessed, true)
                .with(ReturnResult::setMsg, "测试通用Builder").with(ReturnResult::setStatus, 200).with(ReturnResult::setNewResource, "")
                .with(ReturnResult::setData, "newData").with(ReturnResult::test, "1", "2", "3").build();
        System.out.println("ReturnResult=" + JSON.toJSONString(returnResult));
    }

}
