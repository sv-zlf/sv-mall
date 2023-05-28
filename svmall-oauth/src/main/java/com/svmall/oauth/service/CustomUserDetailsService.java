package com.svmall.oauth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author zlf
 * @data 2023/5/28
 * @@description
 */
@Component
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 通过业务方法获取用户及权限信息
//        Customer customer = customerService.getCustomer(s);
//        List<Authority> authorities = customerService.getCustomerAuthority(s);
//        // 对用户权限进行封装
//        List<SimpleGrantedAuthority> list = authorities.stream()
//                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority())).collect(Collectors.toList());
//        // 返回封装的UserDetails用户详情类
//        if (customer != null) {
//            UserDetails userDetails = new User(customer.getUsername(), customer.getPassword(), list);
//            return userDetails;
//        } else {
//            // 如果查询的用户不存在（用户名不存在），必须抛出此异常
//            throw new UsernameNotFoundException("当前用户不存在！");
//        }
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("USER"));
        System.out.println("ssss");
        UserDetails userDetails = new User("nacos", passwordEncoder.encode("nacos"), list);
        return userDetails;
    }
}