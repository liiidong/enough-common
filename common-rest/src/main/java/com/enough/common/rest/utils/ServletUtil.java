package com.enough.common.rest.utils;

import com.alibaba.fastjson.JSON;
import com.enough.common.model.ReturnResult;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: gaf-commons-modules
 * @description:
 * @author: lidong
 * @create: 2019/07/15
 */
public class ServletUtil {

    /**
     * 获取cookies
     *
     * @param request
     * @return
     */
    public static List <String> getCookieList(HttpServletRequest request) {
        List <String> cookieList = new ArrayList <>();
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return cookieList;
        }
        for (Cookie cookie : cookies) {
            cookieList.add(cookie.getName() + "=" + cookie.getValue());
        }
        return cookieList;
    }

    /**
     * 重写响应体
     *
     * @param response
     * @param returnResult
     * @throws IOException
     */
    public static void reWriterResponse(HttpServletResponse response, ReturnResult <?> returnResult) throws IOException {
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(returnResult.getStatus());
        response.getWriter().println(JSON.toJSONString(returnResult));
    }
}
