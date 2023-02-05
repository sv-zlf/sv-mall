package com.svmall.gatewayadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;


@EnableDiscoveryClient
@SpringBootApplication
public class SvmallGatewayAdminApplication  {

    public static void main(String[] args) {
        SpringApplication.run(SvmallGatewayAdminApplication.class, args);

    }


}
