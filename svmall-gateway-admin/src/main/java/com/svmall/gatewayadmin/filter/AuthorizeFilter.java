package com.svmall.gatewayadmin.filter;

import com.svmall.gatewayadmin.exception.ErrorException;
import com.svmall.gatewayadmin.vo.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author zlf
 * @data 2023/2/6
 * @apiNote 用户权限过滤器
 */
@Component
@Slf4j
public class AuthorizeFilter implements Ordered, GlobalFilter {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取request和response对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //2.放行登录与swagger测试
        if(request.getURI().getPath().contains("/v2/api-docs")||request.getURI().getPath().contains("/login")||request.getURI().getPath().contains("/hello")||request.getURI().getPath().contains("auth")){
            return chain.filter(exchange);
        }
        log.info("当前请求路径："+request.getURI().getPath());

        //3.获取token和key
        String token = request.getHeaders().getFirst("token");
        //4.判断token是否存在
        if(StringUtils.isBlank(token)){
            throw new ErrorException(ResultCode.TOKEN_INVALID, "token不存在");
        }

        //5.获取缓存中是否存在token
        //Object value=redisUtil.get(token);
        //if (value.equals("")){
          //  throw new ErrorException(ResultCode.TOKEN_INVALID, "token已失效");
      //  }
        //6.放行
        return chain.filter(exchange);
    }

    /**
     * 优先级设置  值越小  优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}