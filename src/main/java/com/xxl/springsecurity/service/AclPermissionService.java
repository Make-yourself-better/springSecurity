package com.xxl.springsecurity.service;

import com.alibaba.fastjson.JSONObject;
import com.xxl.springsecurity.entity.AclPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author xxl
 * @since 2022-10-16
 */
public interface AclPermissionService extends IService<AclPermission> {

    List<String> selectPermissionValueByUserId(String userId);

    List<JSONObject> selectPermissionByUserId(String userId);

    List<AclPermission> queryAllMenu();

    void removeChildById(String id);



    List<AclPermission> selectAllMenu(String roleId);

    void saveRolePermissionRealtionShip(String roleId, List<String> permissionIds);
}
