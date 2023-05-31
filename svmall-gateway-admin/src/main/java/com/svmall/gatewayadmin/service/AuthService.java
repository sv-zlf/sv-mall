package com.svmall.gatewayadmin.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zlf
 * @data 2023/5/30
 * @@description
 */
@FeignClient(name = "auth-service")
public interface AuthService {
    @GetMapping("/oauth/check_token")
    String checkToken(@RequestParam MultiValueMap<String, String> bodys);
}
