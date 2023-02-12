package com.svmall.gatewayadmin.filter;




import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svmall.gatewayadmin.exception.ApiException;
import com.svmall.gatewayadmin.vo.ResultCode;
import com.svmall.gatewayadmin.vo.ResultVo;
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


/**
 * @author zlf
 * @data 2023/2/8
 * @apiNote 统一响应过滤器
 */
@Slf4j
@Component
@Configuration
public class ResponseGlobalFilter implements GlobalFilter, Ordered {


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

                        log.info("原始Response:{}", lastStr);

                        if(lastStr.contains("swagger")) {
                            return bufferFactory.wrap(content);
                        }
                        //在此处处理返回结果
                        // String类型不能直接包装
                        if(!isJsonObject(lastStr)){
                            ObjectMapper objectMapper = new ObjectMapper();
                            try {
                                // 将数据包装在ResultVo里后转换为json串进行返回
                                lastStr=JSONUtil.toJsonStr(objectMapper.writeValueAsString(new ResultVo(lastStr)));
                                log.info("转化后的lastStr："+lastStr.getBytes());
                                //需要重新设置长度，不然显示不全
                                originalResponse.getHeaders().setContentLength(lastStr.getBytes().length);
                                 return bufferFactory.wrap(lastStr.getBytes());
                            } catch (JsonProcessingException e) {
                                throw new ApiException(ResultCode.RESPONSE_PACK_ERROR, e.getMessage());
                            }
                        }
                        else{
                            lastStr = JSONUtil.toJsonStr(new ResultVo(lastStr));
                            originalResponse.getHeaders().setContentLength(lastStr.getBytes().length);
                            return bufferFactory.wrap(lastStr.getBytes());
                        }
//                        switch (jsonObject.getInt(CODE)) {
//                            case NO_SUCH_MESSAGE:
//                                lastStr = JSONUtil.toJsonStr(getGlobalResponse(jsonObject));
//                                log.info("国际化处理后Response:{}", lastStr);
//                                return bufferFactory.wrap(lastStr.getBytes());
//                        }
                        //不处理异常则用json转换前的数据返回
                       // return bufferFactory.wrap(content);
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

    private   boolean isJsonObject(String content) {
        // 此处应该注意，不要使用StringUtils.isEmpty(),因为当content为"  "空格字符串时，JSONObject.parseObject可以解析成功，
        // 实际上，这是没有什么意义的。所以content应该是非空白字符串且不为空，判断是否是JSON数组也是相同的情况。
        if(StringUtils.isBlank(content)) {
            return false;
        }
        try {
            JSONObject jsonStr =JSONUtil.parseObj(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

