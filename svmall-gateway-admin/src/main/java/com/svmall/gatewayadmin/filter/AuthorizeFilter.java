package com.svmall.gatewayadmin.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author zlf
 * @data 2023/2/6
 * @apiNote
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
        if(request.getURI().getPath().contains("/v2/api-docs")||request.getURI().getPath().contains("/login")){
            return chain.filter(exchange);
        }
        log.info("当前请求路径："+request.getURI().getPath());
        //3.获取token
        String token = request.getHeaders().getFirst("token");

        //4.判断token是否存在
        if(StringUtils.isBlank(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //5.判断token是否有效
        //try {
        //    Claims claimsBody = AppJwtUtil.getClaimsBody(token);
        //    //是否是过期
        //    int result = AppJwtUtil.verifyToken(claimsBody);
        //    if(result == 1 || result  == 2){
        //        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //        return response.setComplete();
        //    }
        //
        //
        //    //获取用户登录的id
        //    String userId = claimsBody.get("id").toString();
        //
        //    //对请求对象request进行增强
        //    ServerHttpRequest req = request.mutate().headers(httpHeaders -> {
        //        //httpHeaders 封装了所有的请求头
        //
        //        httpHeaders.set("userId", userId);
        //    }).build();
        //
        //    //设置增强的request到exchagne对象中
        //    exchange.mutate().request(req);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}

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