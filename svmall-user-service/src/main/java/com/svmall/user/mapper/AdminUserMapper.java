package com.svmall.user.mapper;

import com.svmall.user.entity.AdminUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zlf
 * @since 2023-02-03
 */

@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {

    AdminUser login(@Param("userName") String userName, @Param("password") String password);
}
