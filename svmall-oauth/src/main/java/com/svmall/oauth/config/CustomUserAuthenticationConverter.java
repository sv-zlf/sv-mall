package com.svmall.oauth.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * @author zlf
 * @data 2023/5/27
 * @@description
 */
@Component
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

//    @Autowired
//    UserDetailsService userDetailsService;

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String name = authentication.getName();
        response.put("username", name);

//        Object principal = authentication.getPrincipal();
//        UserJwt userJwt;
//        if (principal instanceof UserJwt) {
//            userJwt = (UserJwt) principal;
//        }
//        }else{
        //refresh_token默认不去调用userdetailService获取用户信息，这里手动去调用
//            UserDetails userDetails = userDetailsService.loadUserByUsername(name);
//            userJwt = (UserJwt) userDetails;
//        }

        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        System.out.println("respone:"+response);
        return response;
    }

}