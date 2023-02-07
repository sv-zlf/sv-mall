package com.svmall.user.controller;


import com.svmall.user.service.AdminUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zlf
 * @since 2023-02-03
 */
@CrossOrigin
@RestController
@RequestMapping("/adminUser")
public class AdminUserController {

    @Autowired
    AdminUserService adminUserService;

    @GetMapping("/hello")
    public String hello() {
        return "hello,world!";
    }

}

