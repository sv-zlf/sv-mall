package com.svmall.gatewayadmin.filter;

/**
 * @author zlf
 * @data 2023/2/8
 * @apiNote
 */


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;



@Slf4j
@Component
@Configuration
public class ResponseGlobalFilter implements GlobalFilter, Ordered {



    private static String CODE = "code";
    private static String MSG = "msg";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        //释放掉内存
                        DataBufferUtils.release(dataBuffer);
                        String lastStr = new String(content, Charset.forName("UTF-8"));

//                        JSONObject jsonObject = JSONUtil.parseObj(lastStr, false);
                        log.info("原始Response:{}", lastStr);
                        //在此处处理返回结果
//                        switch (jsonObject.getInt(CODE)) {
//                            case NO_SUCH_MESSAGE:
//                                lastStr = JSONUtil.toJsonStr(getGlobalResponse(jsonObject));
//                                log.info("国际化处理后Response:{}", lastStr);
//                                return bufferFactory.wrap(lastStr.getBytes());
//                        }
                        //不处理异常则用json转换前的数据返回
                        return bufferFactory.wrap(content);
                    }));
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -2;
    }

//    private GlobalResponse getGlobalResponse(JSONObject jsonObject) {
//        GlobalResponse globalResponse = new GlobalResponse();
//        String msg = jsonObject.getStr(MSG);
//        String[] split = msg.split("'");
//        String s = split[1];
//        globalResponse.setMsg(StringUtils.isEmpty(localeMessageSourceService.getMessage(s)) ? msg : localeMessageSourceService.getMessage(s));
//        globalResponse.setCode(jsonObject.getInt(CODE));
//        return globalResponse;
//    }
}

