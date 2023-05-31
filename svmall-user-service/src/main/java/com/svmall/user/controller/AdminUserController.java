package com.svmall.user.controller;


import com.svmall.user.controller.parm.AdminLoginParam;
import com.svmall.user.entity.AdminUser;
import com.svmall.user.service.AdminUserService;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zlf
 * @since 2023-02-03
 */
@RestController
@RequestMapping("/adminUser")
public class AdminUserController {

    @Autowired
    AdminUserService adminUserService;

    @GetMapping("/hello")
    public String hello() {
        return "hello,world!";
    }

    @ApiOperation(value = "登录接口", notes = "返回token")
    @PostMapping("/login")
    public String login(@RequestBody AdminLoginParam adminLoginParam) {
        return adminUserService.login(adminLoginParam);
    }

    @GetMapping("/hello1")
    public AdminUser hello1() {
        AdminUser adminUser = new AdminUser();
        adminUser.setUserName("ddd");
        return adminUser;
    }

}

