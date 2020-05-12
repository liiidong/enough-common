package com.enough.common.rest.filters;

import javax.servlet.*;
import java.io.IOException;

public class ExceptionWrapperFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
       // 是否已经放有异常栈, 避免循环
    }

    @Override
    public void destroy() {

    }
}
