package com.enough.common.rest.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;

import com.enough.common.rest.exceptions.ScNotModifiedException;
import com.supermap.gaf.rest.HttpServletRequestAware;
import com.supermap.gaf.rest.utils.HttpUtil;

public class ScNotModifiedExceptionMapper implements HttpServletRequestAware,  ExceptionMapper<ScNotModifiedException> {
    private HttpServletRequest request;

    @Override
    public void setHttpServletRequest(HttpServletRequest request) {
        this.request = request;
    }
    
    public Response toResponse(ScNotModifiedException e) {
        ResponseBuilder var2 = Response.ok();
        var2.header("Content-Type", HttpUtil.getAcceptMediaType(this.request));
        var2.status(e.getHttpStatus());
        return var2.build();
    }
}
