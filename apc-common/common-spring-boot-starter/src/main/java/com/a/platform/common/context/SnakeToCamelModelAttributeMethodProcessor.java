package com.a.platform.common.context;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javax.servlet.ServletRequest;


/**
 * 蛇形转驼峰参数转换器
 * 用于非基本类型
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 18:30
 */
public class SnakeToCamelModelAttributeMethodProcessor extends ServletModelAttributeMethodProcessor {


    /**
     * 构造函数
     *
     * @param annotationNotRequired 注解是否必须
     */
    public SnakeToCamelModelAttributeMethodProcessor(boolean annotationNotRequired) {
        super(annotationNotRequired);
    }

    /**
     * 绑定蛇形转驼峰Binder
     *
     * @param binder  spring 机制传琛来的binder
     * @param request spring机制的web request
     */
    @Override
    @InitBinder
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        SnakeToCamelRequestDataBinder camelBinder = new SnakeToCamelRequestDataBinder(binder.getTarget(), binder.getObjectName());
        camelBinder.bind(request.getNativeRequest(ServletRequest.class));
        super.bindRequestParameters(binder, request);
    }


}
