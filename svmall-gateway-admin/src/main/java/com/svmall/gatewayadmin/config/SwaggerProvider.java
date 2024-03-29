package com.svmall.gatewayadmin.config;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import reactor.core.publisher.Flux;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * @author zlf
 * @data 2023/5/25
 * @@description
 */
/**
 * 聚合系统接口<br>
 *
 * @date: 2021/9/22 <br>
 * @author: llxiao <br>
 * @since: 1.0 <br>
 * @version: 1.0 <br>
 */
@Slf4j
@Component
@Primary
@AllArgsConstructor
public class SwaggerProvider implements SwaggerResourcesProvider, WebFluxConfigurer {

    /**
     * Swagger2默认的url后缀
     */
    public static final String SWAGGER2URL = "/v2/api-docs";

    /**
     * 网关路由
     */
    ///@Autowired
    ///private RouteLocator routeLocator;

    /**
     * yml方式获取路由服务
     */
    ///@Autowired
    ///private GatewayProperties gatewayProperties;

    /**
     * 使用动态路由，获取到指定的路由规则
     */
    @Autowired
    private RouteDefinitionRepository routeDefinitionRepository;

    /**
     * 聚合其他服务接口
     *
     * @return java.util.List<springfox.documentation.swagger.web.SwaggerResource>
     * @author llxiao
     * @date 2022/3/30
     **/
    @Override
    public List<SwaggerResource> get() {

        //接口资源列表
        List<SwaggerResource> resourceList = new ArrayList<>();

        //服务名称列表
        Set<String> routeHosts = new HashSet<>();

        // 去重，多负载服务只添加一次
        Set<String> existsServer = new HashSet<>();

        // 动态网关方式获取路由  nacos动态网关
        Flux<RouteDefinition> routeDefinitions = routeDefinitionRepository.getRouteDefinitions();
        routeDefinitions.subscribe(routeDefinition -> {
            List<PredicateDefinition> predicates = routeDefinition.getPredicates();
            if (CollectionUtil.isNotEmpty(predicates)) {
                String host = routeDefinition.getUri().getHost();
                routeHosts.add(host);
                String url = "/" + host + SWAGGER2URL;
                if (!existsServer.contains(url)) {
                    existsServer.add(url);
                    SwaggerResource swaggerResource = new SwaggerResource();
                    swaggerResource.setUrl(url);
                    swaggerResource.setName(host);
                    resourceList.add(swaggerResource);
                }

            }
        });
        log.info("网关服务地址列表：{}", JSON.toJSONString(routeHosts));
        return resourceList;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** swagger-ui 地址 */
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
        //favicon.ico
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/");
    }
}
