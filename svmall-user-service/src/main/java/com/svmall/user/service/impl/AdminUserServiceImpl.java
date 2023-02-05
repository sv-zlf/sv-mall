package com.svmall.user.service.impl;

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
    public List<AdminUser> selectList(){
        return adminUserMapper.selectList(null);
    }
}
