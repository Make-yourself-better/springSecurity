package com.xxl.springsecurity.service;

import com.xxl.springsecurity.entity.AclUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author xxl
 * @since 2022-10-16
 */
public interface AclUserService extends IService<AclUser> {


    AclUser selectByUserName(String username);


}
