package com.svmall.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ImportResource;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@ImportResource("classpath:/spring/spring-dao.xml")
@SpringBootApplication(exclude = { TransactionAutoConfiguration.class})
@EnableDiscoveryClient
@EnableSwagger2WebMvc
public class SvmallUserServiceApplication {

    static Logger logger= LoggerFactory.getLogger(SvmallUserServiceApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SvmallUserServiceApplication.class, args);
    }

}
