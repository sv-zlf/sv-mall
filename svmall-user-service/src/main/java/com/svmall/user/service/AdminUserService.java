package com.svmall.user.service;

import com.svmall.user.controller.parm.AdminLoginParam;
import com.svmall.user.entity.AdminUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zlf
 * @since 2023-02-03
 */
public interface AdminUserService extends IService<AdminUser> {

    List<AdminUser> selectList();

    String login(AdminLoginParam adminLoginParam);

}
