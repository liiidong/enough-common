package com.enough.common.rest.exceptions;

import com.enough.common.model.ReturnResult;
import com.enough.common.rest.HttpServletRequestAware;
import com.enough.common.rest.utils.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;

public class JaxrsHttpExceptionMapper implements HttpServletRequestAware, ExceptionMapper<JaxrsHttpException> {
    private HttpServletRequest request;

    @Override
    @Context
    public void setHttpServletRequest(@Context HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Response toResponse(JaxrsHttpException ex) {
        String errMessage = ex.getMessage() == null ? "HttpException" : ex.getMessage();
        int status = ex.getErrorStatus() == null ? 500 : ex.getErrorStatus().getStatusCode();
        ReturnResult <String> result = ReturnResult.failed(String.class)
                .status(status)
                .msg(errMessage)
                .data(errMessage).build();
        ResponseBuilder var2 = Response.status(status)
                .header("Content-Type", HttpUtil.getAcceptMediaType(this.request))
                .entity(result);
        return var2.build();
    }
}
