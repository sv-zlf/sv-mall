package com.svmall.user.controller.parm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zlf
 * @data 2023/2/16
 * @@description 管理员用户登陆参数
 */
@Data
public class AdminLoginParam implements Serializable {

    @ApiModelProperty("登录名")
    private String userName;

    @ApiModelProperty("用户密码")
    private String password;
}
