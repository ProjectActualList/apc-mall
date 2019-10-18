package com.a.platform.common.interceptor;

import com.a.platform.common.context.SnakeToCamelArgumentResolver;
import com.a.platform.common.context.SnakeToCamelModelAttributeMethodProcessor;
import com.a.platform.common.context.XssStringJsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


/**
 * 加载自定义的 拦截器
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 18:35
 */
@Configuration
public class WebInterceptorConfigurer implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

        //参数蛇形转驼峰拦截
        argumentResolvers.add(new SnakeToCamelModelAttributeMethodProcessor(true));
        argumentResolvers.add(new SnakeToCamelArgumentResolver());

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApcMallRequestInterceptor()).addPathPatterns("/**");
    }

    /**
     * 自定义 ObjectMapper ，用于xss攻击过滤
     *
     * @param builder
     * @return
     */
    @Bean
    @Primary
    public ObjectMapper xssObjectMapper(Jackson2ObjectMapperBuilder builder) {
        //解析器
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        //注册xss解析器
        SimpleModule xssModule = new SimpleModule("XssStringJsonSerializer");
        xssModule.addSerializer(new XssStringJsonSerializer());
        objectMapper.registerModule(xssModule);
        //返回
        return objectMapper;
    }


}
