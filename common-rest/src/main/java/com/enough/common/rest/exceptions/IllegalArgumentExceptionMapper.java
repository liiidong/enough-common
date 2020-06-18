package com.enough.common.rest.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import com.enough.common.model.ReturnResult;
import com.enough.common.rest.HttpServletRequestAware;
import com.enough.common.rest.utils.HttpUtil;

public class IllegalArgumentExceptionMapper implements HttpServletRequestAware, ExceptionMapper<IllegalArgumentException> {

    private HttpServletRequest request;
    
    @Override
    @Context
    public void setHttpServletRequest(@Context HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Response toResponse(IllegalArgumentException e) {
        ResponseBuilder responseBuilder = Response.status(Status.BAD_REQUEST);
        String errMessage = e.getMessage() == null ? "IllegalArgumentException" : e.getMessage();
        ReturnResult <String> result = ReturnResult.failed(String.class)
                .status(Status.BAD_REQUEST.getStatusCode())
                .msg(errMessage)
                .data(errMessage)
                .build();
        return responseBuilder.header("Content-Type", HttpUtil.getAcceptMediaType(this.request))
            .entity(result).build();
    }
}
