package com.xxl.springsecurity.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xxl.springsecurity.entity.AclRole;
import com.xxl.springsecurity.entity.AclUser;
import com.xxl.springsecurity.service.AclPermissionService;
import com.xxl.springsecurity.service.AclRoleService;
import com.xxl.springsecurity.service.AclUserService;
import com.xxl.springsecurity.service.IndexService;

import com.xxl.springsecurity.utils.exception.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.management.relation.Role;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    AclUserService userService;
    @Autowired
    AclRoleService roleService;
    @Autowired
    AclPermissionService permissionService;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public Map<String, Object> getUserInfo(String username) {
        Map<String, Object> result = new HashMap<>();
        //根据用户名字获取用户信息
       AclUser aclUser = userService.selectByUserName(username);
       if (StringUtils.isEmpty(aclUser)){
           throw new GuliException();
       }
        //根据用户id获取角色信息
        List<String> roleList = roleService.selectRoleByUserId(aclUser.getId());
        if(roleList.size() == 0) {
            //前端框架必须返回一个角色，否则报错，如果没有角色，返回一个空角色
            roleList.add("");
        }
        //根据用户id获取操作权限值
        List<String> permissionValueList = permissionService.selectPermissionValueByUserId(aclUser.getId());
        redisTemplate.opsForValue().set(username,permissionValueList);
        result.put("name", aclUser.getUsername());
        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        result.put("roles", roleList);
        result.put("permissionValueList", permissionValueList);
        return  result;
    }

    @Override
    public List<JSONObject> getMenu(String username) {
        AclUser aclUser = userService.selectByUserName(username);
        //根据用户id获取用户菜单权限
        List<JSONObject> permissionList = permissionService.selectPermissionByUserId(aclUser.getId());
        return permissionList;

    }
}
