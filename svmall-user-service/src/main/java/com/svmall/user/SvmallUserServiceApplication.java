package com.svmall.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.svmall.common"})
@MapperScan(basePackages = "com.svmall.user.mapper")
public class SvmallUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SvmallUserServiceApplication.class, args);
    }

}
