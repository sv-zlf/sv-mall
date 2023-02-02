package com.svmall.gatewayadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SvmallGatewayAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SvmallGatewayAdminApplication.class, args);
    }

}
