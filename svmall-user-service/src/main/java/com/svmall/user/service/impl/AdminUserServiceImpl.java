package com.svmall.user.service.impl;

import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.svmall.common.utils.RedisUtil;
import com.svmall.user.controller.parm.AdminLoginParam;
import com.svmall.user.entity.AdminUser;
import com.svmall.user.mapper.AdminUserMapper;
import com.svmall.user.service.AdminUserService;
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
    private RedisUtil redisUtil;

    @Override
    public String login(AdminLoginParam adminLoginParam){

        AdminUser adminUser=adminUserMapper.login(adminLoginParam.getUserName(),adminLoginParam.getPassword());
        if (adminUser!=null) {
            Map<String,Object> playroad=new HashMap<String,Object>();
            playroad.put("uid",adminUser.getAdminUserId());
            playroad.put("expire_time",System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);
            String token=JWTUtil.createToken(playroad,"bf1314520".getBytes());
            redisUtil.set(token,playroad,System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);
            return token;
        } else {
            return "登陆失败";
        }
    }
    @Override
    public List<AdminUser> selectList(){
        return adminUserMapper.selectList(null);
    }
}
