package com.enough.common.remote;

import com.enough.common.model.ReturnResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @program: gaf-commons-modules
 * @description:
 * @author: lidong
 * @create: 2019/08/01
 */
public class RemoteCommonService {

    private static Logger logger = LoggerFactory.getLogger(RemoteCommonService.class);

    private RestTemplate restTemplate;

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T, K> ReturnResult <K> remoteCreate(String url, T t, List <String> cookieList, Class <K> clazz) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if(CollectionUtils.isNotEmpty(cookieList)) {
                headers.put("Cookie", cookieList);
            }
            HttpEntity <T> requestEntity = new HttpEntity <>(t, headers);
            //  执行HTTP请求
            return restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference <ReturnResult <K>>() {
            }).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ReturnResult.failed(clazz).msg("创建失败").build();
    }

    public <T, K> ReturnResult <K> createError(String url, T t, List <String> cookieList, Class <K> clazz) {
        return ReturnResult.failed(clazz).msg("创建失败").build();
    }

    @HystrixCommand(fallbackMethod = "getError")
    public <T> T remoteGet(String url, MultiValueMap <String, String> params, List <String> cookieList, Class <T> clazz) {
        T serviceSetting = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            //  提交方式，表单提交
            headers.setContentType(MediaType.APPLICATION_JSON);
            if(CollectionUtils.isNotEmpty(cookieList)) {
                headers.put("Cookie", cookieList);
            }
            //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
            HttpEntity <MultiValueMap <String, String>> requestEntity = new HttpEntity <>(params, headers);
            //  执行HTTP请求
            ResponseEntity <T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, clazz);
            serviceSetting = responseEntity.getBody();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return serviceSetting;
    }

    public <T> T getError(String url, MultiValueMap <String, String> params, List <String> cookieList, Class <T> clazz) {
        ReturnResult.failed(clazz).msg("服务信息获取失败").build();
        return null;
    }

    @HystrixCommand(fallbackMethod = "removeError")
    public ReturnResult <String> remoteRemove(String url, MultiValueMap <String, String> params, List <String> cookieList) {
        try {
            HttpHeaders headers = new HttpHeaders();
            if(CollectionUtils.isNotEmpty(cookieList)) {
                headers.put("Cookie", cookieList);
            }
            //  提交方式，表单提交
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
            HttpEntity <MultiValueMap <String, String>> requestEntity = new HttpEntity <>(params, headers);
            //  执行HTTP请求
            return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference <ReturnResult <String>>() {
            }).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ReturnResult.failed(String.class).msg("删除失败").build();
    }

    public ReturnResult <String> removeError(String url, MultiValueMap <String, String> params, List <String> cookieList) {
        return ReturnResult.failed(String.class).msg("删除失败").build();
    }

    public <T, K> ReturnResult <K> remotePut(String url, T t, List <String> cookieList, Class <K> clazz) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if(CollectionUtils.isNotEmpty(cookieList)) {
                headers.put("Cookie", cookieList);
            }
            HttpEntity <T> requestEntity = new HttpEntity <>(t, headers);
            //  执行HTTP请求
            return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference <ReturnResult <K>>() {
            }).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ReturnResult.failed(clazz).msg("更新失败").build();
    }
}
