package com.svmall.gatewayadmin.filter;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author zlf
 * @data 2023/2/8
 * @apiNote
 */
@Component
@Slf4j
public class ApiRespGlobalFilter implements GlobalFilter, Ordered {

    private final ObjectMapper jsonMapper;

    public ApiRespGlobalFilter(ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        log.info(path);
        if (path.contains("/v2/api-docs")) {
            return chain.filter(exchange);
        }
        //获取response的 返回数据
        ServerHttpResponse response = exchange.getResponse();
        log.info("statusCode"+String.valueOf(response.getStatusCode()));
        HttpStatus statusCode = response.getStatusCode();
        if (statusCode == HttpStatus.OK) {

            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
                @Override
                public boolean setStatusCode(HttpStatus status) {
                    return super.setStatusCode(HttpStatus.OK);
                }

                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    ServerHttpResponse delegateResponse = this.getDelegate();
                    MediaType contentType = delegateResponse.getHeaders().getContentType();
                    log.debug("contentType:{}", contentType);

                    if (null == contentType) {
                        String resp = "{\"success\":true,\"code\":\"200\",\"message\":\"成功\"}";
                        byte[] newRs = resp.getBytes(StandardCharsets.UTF_8);
                        delegateResponse.setStatusCode(HttpStatus.OK);
                        delegateResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
                        delegateResponse.getHeaders().setContentLength(newRs.length);
                        DataBuffer buffer = delegateResponse.bufferFactory().wrap(newRs);
                        return delegateResponse.writeWith(Flux.just(buffer));
                    }

                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer join = dataBufferFactory.join(dataBuffers);
                        byte[] content = new byte[join.readableByteCount()];
                        join.read(content);
                        DataBufferUtils.release(join);

                        Charset contentCharset = contentType.getCharset();
                        contentCharset = (null == contentCharset) ? StandardCharsets.UTF_8 : contentCharset;
                        if (contentType.equalsTypeAndSubtype(MediaType.TEXT_HTML)) {
                            String responseData = new String(content, contentCharset);
                            log.debug("响应内容:{}", responseData);
                            final String redirectCommand = "redirect:";
                            int redirectCommandIndex = responseData.indexOf(redirectCommand);
                            if (redirectCommandIndex >= 0) {
                                String redirectUri = responseData.substring(redirectCommandIndex + redirectCommand.length()).trim();

                                delegateResponse.setStatusCode(HttpStatus.SEE_OTHER);
                                delegateResponse.getHeaders().set(HttpHeaders.LOCATION, redirectUri);
                            }
                        }
                        if (contentType.includes(MediaType.APPLICATION_JSON) || contentType.includes(MediaType.APPLICATION_JSON_UTF8)) {

                            String responseData = new String(content, contentCharset);
                            log.debug("响应内容:{}", responseData);

                            ObjectNode objectNode = jsonMapper.createObjectNode();
                            objectNode.put("success", true);
                            objectNode.put("code", "200");
                            objectNode.put("message", "成功");

                            try {
                                JsonNode respDataNode = jsonMapper.readTree(responseData);
                                if (respDataNode.isObject()) {
                                    JsonNode statusNode = respDataNode.path("status");
                                    JsonNode messageNode = respDataNode.path("message");
                                    JsonNode errorNode = respDataNode.path("error");
                                    JsonNode pathNode = respDataNode.path("path");
                                    if (statusNode.isNumber() && errorNode.isTextual() && (messageNode.isMissingNode() || messageNode.isNull() || messageNode.isTextual()) && pathNode.isTextual()) {
                                        objectNode.put("success", false);

                                        JsonNode errorCodeNode = respDataNode.get("errorCode");
                                        if (null != errorCodeNode && !errorCodeNode.isNull()) {
                                            objectNode.put("code", errorCodeNode.asText());
                                        } else {
                                            String status = statusNode.asText("500");
                                            objectNode.put("code", status);
                                        }

                                        String message = respDataNode.path("message").asText("No message available");
                                        if (Objects.equals(message, "No message available")) {
                                            message = "系统开小差，请联系管理员！";
                                        }
                                        objectNode.put("message", message);
                                        objectNode.set("data", respDataNode.path("data"));

                                        byte[] newRs = jsonMapper.writeValueAsBytes(objectNode);
                                        delegateResponse.getHeaders().setContentLength(newRs.length);
                                        return delegateResponse.bufferFactory().wrap(newRs);
                                    }

                                    JsonNode respStatusNode = respDataNode.path("respStatus");
                                    if (respStatusNode.isBoolean() && respStatusNode.asBoolean() && statusNode.isNumber() && (messageNode.isMissingNode() || messageNode.isNull() || messageNode.isTextual())) {
                                        ObjectNode respObjectNode = (ObjectNode) respDataNode;
                                        respObjectNode.put("success", true);
                                        respObjectNode.put("code", statusNode.asText("200"));

                                        respObjectNode.remove("status");
                                        respObjectNode.remove("respStatus");

                                        byte[] newRs = jsonMapper.writeValueAsBytes(respObjectNode);
                                        delegateResponse.getHeaders().setContentLength(newRs.length);
                                        return delegateResponse.bufferFactory().wrap(newRs);
                                    }
                                }

                                objectNode.set("data", respDataNode);
                                byte[] newRs = jsonMapper.writeValueAsBytes(objectNode);
                                delegateResponse.getHeaders().setContentLength(newRs.length);
                                return delegateResponse.bufferFactory().wrap(newRs);
                            } catch (IOException e) {
                                log.error("\n", e);
                                String errResp = "{\"success\":false,\"code\":\"500\",\"message\":\"System Error\"}";
                                byte[] newRs = errResp.getBytes(contentCharset);
                                delegateResponse.getHeaders().setContentLength(newRs.length);
                                return delegateResponse.bufferFactory().wrap(newRs);
                            }
                        }

                        return delegateResponse.bufferFactory().wrap(content);
                    }));
                }
            };
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -2;
    }
}