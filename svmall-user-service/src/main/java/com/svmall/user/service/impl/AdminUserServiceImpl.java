package com.svmall.user.service.impl;

import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.svmall.common.exception.ErrorException;
import com.svmall.common.utils.RedisUtil;
import com.svmall.common.vo.ResultCode;
import com.svmall.user.controller.parm.AdminLoginParam;
import com.svmall.user.entity.AdminUser;
import com.svmall.user.mapper.AdminUserMapper;
import com.svmall.user.service.AdminUserService;
import com.svmall.user.service.openfeign.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private AuthService authService;



    @Override
    public String login(AdminLoginParam adminLoginParam){

        AdminUser adminUser=adminUserMapper.login(adminLoginParam.getUserName(),adminLoginParam.getPassword());
        if (adminUser!=null) {
            return token;
        } else {
            throw new ErrorException(ResultCode.FAILED, "登陆用户名或者密码不对");
        }
    }
    @Override
    public List<AdminUser> selectList(){
        return adminUserMapper.selectList(null);
    }
}
