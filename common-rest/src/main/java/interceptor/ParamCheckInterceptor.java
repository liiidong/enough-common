package interceptor;

import com.enough.common.annotation.ParamNotNull;
import com.alibaba.fastjson.JSON;
import com.enough.common.model.ReturnResult;
import com.enough.common.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wrapper.PostParamRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: ims-business
 * @description: 投资计划操作过滤器
 * @author: lidong
 * @create: 2020/04/08
 */
@Slf4j
public class ParamCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return ((HandlerMethod) handler).getMethodParameters().length > 0;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        try {
            for (MethodParameter methodParameter : ((HandlerMethod) handler).getMethodParameters()) {
                if(methodParameter.getParameterAnnotation(ParamNotNull.class) != null){
                    boolean afterInit = methodParameter.getParameterAnnotation(ParamNotNull.class).afterInit();
                    //检查参数时候为null
                    Class clazz = handler.getClass();
                    Object arg;
                    PostParamRequestWrapper requestWrapper = new PostParamRequestWrapper(request);
                    String param = requestWrapper.getBody();
                    if (StringUtils.isNotBlank(param)) {
                        try {
                            arg = JSON.parseObject(param, clazz);
                            if(arg == null && afterInit){
                            }
                        } catch (Exception e) {
                            log.error("参数转换错误：" + e.getMessage());
                            throw e;
                        }
                    }
                }
            }
        } catch (Exception e) {
            ReturnResult result = new ReturnResult();
            result.setMsg(e.getMessage());
            result.setStatus(500);
            returnJson(response, result);
        }
    }

    private void returnJson(HttpServletResponse response, ReturnResult result) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSONUtils.toJSONString(result));
        } catch (IOException e) {
            log.error("拦截器输出流异常" + e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
