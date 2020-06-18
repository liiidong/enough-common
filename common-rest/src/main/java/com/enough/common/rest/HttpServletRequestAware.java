package com.enough.common.rest;

import javax.servlet.http.HttpServletRequest;

public interface HttpServletRequestAware {
    void setHttpServletRequest(HttpServletRequest httpServletRequest);
}
