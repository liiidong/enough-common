package com.enough.common.rest.exceptions;

import com.enough.common.model.ReturnResult;
import com.enough.common.rest.HttpServletRequestAware;
import com.enough.common.rest.utils.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class WebApplicationExceptionMapper implements HttpServletRequestAware, ExceptionMapper<WebApplicationException> {
    
    private HttpServletRequest request;
    
    @Override
    public void setHttpServletRequest(HttpServletRequest request) {
        this.request = request;
    }
    
    @Override
    public Response toResponse(WebApplicationException ex) {
        Response response = ex.getResponse();
        String errMessage = ex.getMessage() == null ? "webApplicaitonException" : ex.getMessage();
        ReturnResult <String> result = ReturnResult.failed(String.class)
                .status(response.getStatus())
                .msg(errMessage)
                .data(errMessage).build();
        return Response.status(response.getStatus())
                .header("Content-Type", HttpUtil.getAcceptMediaType(this.request))
                .entity(result)
                .build();
    }
}
