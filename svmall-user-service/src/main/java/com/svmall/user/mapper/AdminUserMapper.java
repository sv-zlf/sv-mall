package com.svmall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.svmall.user.entity.AdminUser;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zlf
 * @since 2023-02-03
 */

public interface AdminUserMapper extends BaseMapper<AdminUser> {

    AdminUser login(@Param("userName") String userName, @Param("password") String password);
}
