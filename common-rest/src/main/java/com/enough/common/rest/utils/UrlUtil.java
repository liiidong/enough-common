package com.enough.common.rest.utils;

import com.enough.common.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Slf4j
public class UrlUtil {

    public static <T> List<T> parseParamValueArray(String parameterName, Map<String, String> urlParameters, Class<T> type, List<T> defaultValue) {
        String str = urlParameters.get(parameterName);
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        try {
            return JSONUtils.parseArray(str, type);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return defaultValue;
    }
    
    public static <T> T parseParamValue(String parameterName, Map<String, String> urlParameters, Class<T> type, T defaultValue) {
        String str = urlParameters.get(parameterName);
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        try {
            return (T) MapUtils.getObject(urlParameters,parameterName);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return defaultValue;
    }
}
