package com.svmall.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class SvmallApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SvmallApiApplication.class, args);
    }

}
