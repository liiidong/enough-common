package com.enough.common.rest.exceptions;

import com.enough.common.rest.filters.EnoughRequestWrapperFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(ResourceConfig.class)
// 使用Jersey时再加载
@ConditionalOnProperty(name="enough.jersey.exception.enable", havingValue="true", matchIfMissing = true)
public class ExceptionMapperConfig {
    
    @Bean
    public ExceptionMapperConfig configExceptionMapper(@Autowired ResourceConfig resouceConfig) {
        resouceConfig.register(com.enough.common.rest.exceptions.IllegalArgumentExceptionMapper.class);
        resouceConfig.register(com.enough.common.rest.exceptions.IllegalStateExceptionMapper.class);
        resouceConfig.register(com.enough.common.rest.exceptions.ScNotModifiedExceptionMapper.class);
        resouceConfig.register(com.enough.common.rest.exceptions.JaxrsHttpExceptionMapper.class);
        resouceConfig.register(com.enough.common.rest.exceptions.WebApplicationExceptionMapper.class);
        return new ExceptionMapperConfig();
    }
    
    @Bean
    public EnoughRequestWrapperFilter requestFilter() {
        return new EnoughRequestWrapperFilter();
    }
}
