package com.svmall.user.service.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zlf
 * @data 2023/6/6
 * @description
 */
@FeignClient(name = "auth-service")
public interface AuthService {
    @GetMapping("/oauth/check_token")
    String checkToken(@RequestParam MultiValueMap<String, String> bodys);

    @GetMapping("/oauth/token")
    String getToken(@RequestParam MultiValueMap<String, String> bodys);
}
