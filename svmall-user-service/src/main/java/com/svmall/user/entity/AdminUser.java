package com.svmall.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zlf
 * @since 2023-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="AdminUser对象", description="")
public class AdminUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "管理员用户主键")
    @TableId(value = "admin_user_id", type = IdType.AUTO)
    private Long adminUserId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "是否锁定  0-未锁定 1-无法登录")
    private String isLocked;


}
