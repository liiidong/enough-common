package com.enough.common.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.InputStreamSource;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong
 * @apiNote 日志aop，环切到controller的public方法
 * @program datacube-server
 * @since 2020/07/28
 */
@Slf4j
@Aspect
public class LogAspect {

    /**
     * 定义切点，为Controller所有公有方法
     * execution(正则)
     * public 方法修饰符，可不写或*代替
     * (..)不限方法参数
     * </p>
     */
    @Pointcut(value = "execution(public * com.enough.*.controller.*.*(..))")
    public void controllerMethods() {
    }

    /**
     * 被拦截的方法，须显式的抛出异常，且不能做任何处理，
     * 这样AOP才能捕获到方法中的异常，进而进行回滚。
     *
     * @param point
     * @return
     */
    @Around("controllerMethods()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        //方法名
        String methodStr = point.getSignature().getDeclaringTypeName().concat(".").concat(point.getSignature().getName());
        String argsStr = getArgsJsonStr(point);
        log.info(methodStr.concat(",入参：").concat(argsStr));
        long startDate = System.currentTimeMillis();
        Object result = point.proceed();
        long endDate = System.currentTimeMillis();
        log.info("请求耗时：".concat(String.valueOf(endDate - startDate).concat("ms")));
        log.info(methodStr.concat(",返回结果：").concat(result == null ? "无" : JSON.toJSONString(result)));
        return result;
    }

    /**
     * 获取方法
     *
     * @param joinPoint ProceedingJoinPoint
     * @return 方法
     */
    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint.getTarget().getClass().getDeclaredMethod(joinPoint.getSignature().getName(), method.getParameterTypes());
            } catch (SecurityException | NoSuchMethodException e) {
                log.error("" + e);
            }
        }
        return method;
    }

    /**
     * 获取参数
     * 字符串表述
     *
     * @param point
     * @return
     */
    private String getArgsJsonStr(ProceedingJoinPoint point) {
        if (point.getArgs() != null && point.getArgs().length > 0) {
            List <String> argList = new ArrayList <>();
            for (Object arg : point.getArgs()) {
                if (arg == null || arg instanceof ServletRequest || arg instanceof ServletResponse || arg instanceof InputStreamSource) {
                    continue;
                }
                // 判断是基本类型或者String
                if (arg.getClass().isPrimitive() || arg instanceof String) {
                    argList.add(arg.toString());
                } else {
                    // JOSN序列化
                    argList.add(JSON.toJSONString(arg));
                }
            }
            return CollectionUtils.isEmpty(argList) ? StringUtils.EMPTY : String.join(",", argList);
        }
        return StringUtils.EMPTY;
    }
}
