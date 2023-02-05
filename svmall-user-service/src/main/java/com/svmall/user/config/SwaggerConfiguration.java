package com.svmall.user.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

    @Bean(value = "coreApi")
    @Order(value = 1)
    public Docket coreApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(Docket.DEFAULT_GROUP_NAME)
                .apiInfo(groupApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.svmall.user.controller"))

                .paths(PathSelectors.any())
                .build();
    }

//    @Bean(value = "menuApi")
//    @Order(value = 1)
//    public Docket menuApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("随机菜单接口")
//                .apiInfo(groupApiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.bzf.menu.controller"))
//                .paths(PathSelectors.any())
//
//                .build();
//    }
    private ApiInfo groupApiInfo(){
        return new ApiInfoBuilder()
                .title("江大学生小助手")
                .description("<div style='font-size:14px;color:red;'助力江大学生'</div>")
                .termsOfServiceUrl("http://www.zlfeng.cn/")
               // .contact("group@qq.com")
                .version("1.0")
                .build();
    }


}