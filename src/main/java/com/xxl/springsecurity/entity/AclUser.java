package com.xxl.springsecurity.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

import com.xxl.springsecurity.utils.valid.ListValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author xxl
 * @since 2022-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="AclUser对象", description="用户表")
public class AclUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会员id")
      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @NotEmpty(message = "用户名不能为空")
    @ApiModelProperty(value = "微信openid")
    private String username;

    @NotEmpty(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @NotEmpty(message = "昵称不能为空")
    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "用户头像")
    private String salt;

    @ApiModelProperty(value = "用户签名")
    private String token;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;

    /*@ListValue(values = {0,1}) //自定义校验注解
    @TableField(exist = false)
    private Integer showStatus;*/

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;


}
