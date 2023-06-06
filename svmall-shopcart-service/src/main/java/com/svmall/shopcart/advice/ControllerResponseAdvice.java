package com.svmall.shopcart.advice;


import com.alibaba.fastjson.JSON;
import com.svmall.common.vo.ResultVo;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author zlf
 * @data 2023/6/1
 * @description
 */
@RestControllerAdvice(basePackages = {"com.svmall"})
public class ControllerResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // response是ResultVo类型，或者注释了NotControllerResponseAdvice都不进行包装
        return !methodParameter.getParameterType().isAssignableFrom(ResultVo.class);
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        // String类型不能直接包装
        if (returnType.getGenericParameterType().equals(String.class)) {
            return JSON.toJSONString(new ResultVo(data));
        }
        // 否则直接包装成ResultVo返回
        return new ResultVo(data);
    }
}