package com.svmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.svmall.user.controller.parm.AdminLoginParam;
import com.svmall.user.entity.AdminUser;
import com.svmall.user.mapper.AdminUserMapper;
import com.svmall.user.service.AdminUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zlf
 * @since 2023-02-03
 */
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    @Autowired
    AdminUserMapper adminUserMapper;


    @Override
    public String login(AdminLoginParam adminLoginParam){

        AdminUser adminUser=adminUserMapper.login(adminLoginParam.getUserName(),adminLoginParam.getPassword());
        if (adminUser!=null) {
            return "登陆成功";
        } else {
            return "登陆失败";
        }
    }
    @Override
    public List<AdminUser> selectList(){
        return adminUserMapper.selectList(null);
    }
}
