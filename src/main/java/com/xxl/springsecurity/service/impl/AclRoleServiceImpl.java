package com.xxl.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxl.springsecurity.entity.AclRole;
import com.xxl.springsecurity.entity.AclUserRole;
import com.xxl.springsecurity.mapper.AclRoleMapper;
import com.xxl.springsecurity.service.AclRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxl.springsecurity.service.AclUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xxl
 * @since 2022-10-16
 */
@Service
public class AclRoleServiceImpl extends ServiceImpl<AclRoleMapper, AclRole> implements AclRoleService {
    @Autowired
    AclUserRoleService userRoleService;
    @Override
    public List<String> selectRoleByUserId(String id) {
       List<String> aclRoleList =  baseMapper.selectRoleByUserId(id);
       return aclRoleList;

    }

    @Override
    public Map<String, Object> findRoleByUserId(String userId) {
        List<AclRole> allRolesList = baseMapper.selectList(null);
        //根据用户id，查询用户拥有的角色id
        //.select只返回指定字段的值
        List<AclUserRole> existUserRoleList = userRoleService.list(new QueryWrapper<AclUserRole>().eq("user_id", userId).select("role_id"));
        //收集所有的role_id
        List<String> existRoleList = existUserRoleList.stream().map(aclUserRole -> {
            return aclUserRole.getRoleId();
        }).collect(Collectors.toList());

        //对角色进行分类  直接前面已经收集起来的role_id 与总的role_id相同的集合
        List<AclRole> assignRoles = new ArrayList<AclRole>();
        for (AclRole role : allRolesList) {
            //已分配
            if(existRoleList.contains(role.getId())) {
                assignRoles.add(role);
            }
        }


        //目标：返回一个总的权限 和一个登陆用户的权限
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoles", assignRoles);
        roleMap.put("allRolesList", allRolesList);
        return roleMap;
    }

    @Override
    public void saveUserRoleRealtionShip(String userId, String[] roleIds) {
        //删除当前id的用户角色角色关联表信息
        userRoleService.remove(new QueryWrapper<AclUserRole>().eq("user_id", userId));
        //在user_role添加新的信息
        List<AclUserRole> userRoleList = new ArrayList<>();
        for(String roleId : roleIds) {
            if(!StringUtils.isEmpty(roleId)) {
                AclUserRole userRole = new AclUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleList.add(userRole);
            }
        }
        userRoleService.saveBatch(userRoleList);
    }
}
