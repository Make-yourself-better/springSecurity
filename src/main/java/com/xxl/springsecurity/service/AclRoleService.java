package com.xxl.springsecurity.service;

import com.xxl.springsecurity.entity.AclRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xxl
 * @since 2022-10-16
 */
public interface AclRoleService extends IService<AclRole> {

    List<String> selectRoleByUserId(String id);

    Map<String, Object> findRoleByUserId(String userId);


    void saveUserRoleRealtionShip(String userId, String[] roleIds);
}
