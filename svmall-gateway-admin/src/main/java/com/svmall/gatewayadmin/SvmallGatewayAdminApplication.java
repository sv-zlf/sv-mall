package com.svmall.gatewayadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;



@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages ="com.svmall")
public class SvmallGatewayAdminApplication  {

    public static void main(String[] args) {
        SpringApplication.run(SvmallGatewayAdminApplication.class, args);

    }


}
