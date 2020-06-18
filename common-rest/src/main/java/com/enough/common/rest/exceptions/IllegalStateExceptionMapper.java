package com.enough.common.rest.exceptions;

import com.enough.common.model.ReturnResult;
import com.enough.common.rest.HttpServletRequestAware;
import com.enough.common.rest.utils.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class IllegalStateExceptionMapper implements HttpServletRequestAware, ExceptionMapper<IllegalStateException> {
    private HttpServletRequest request;

    @Override
    @Context
    public void setHttpServletRequest(@Context HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Response toResponse(IllegalStateException e) {
        ResponseBuilder responseBuilder = Response.serverError();
        String errMessage = e.getMessage() == null ? "IllegalStateException" : e.getMessage();
        ReturnResult <String> result = ReturnResult.failed(String.class)
                .status(Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .msg(errMessage)
                .data(errMessage).build();
        return responseBuilder.header("Content-Type", HttpUtil.getAcceptMediaType(this.request))
                .entity(result).build();
    }
}