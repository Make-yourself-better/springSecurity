package com.xxl.springsecurity.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxl.springsecurity.entity.AclPermission;
import com.xxl.springsecurity.entity.AclRolePermission;
import com.xxl.springsecurity.entity.AclUser;
import com.xxl.springsecurity.mapper.AclPermissionMapper;
import com.xxl.springsecurity.service.AclPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxl.springsecurity.service.AclRolePermissionService;
import com.xxl.springsecurity.service.AclUserService;
import com.xxl.springsecurity.utils.helper.MemuHelper;
import com.xxl.springsecurity.utils.helper.PermissionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.xxl.springsecurity.utils.helper.PermissionHelper.bulid;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author xxl
 * @since 2022-10-16
 */
@Service
public class AclPermissionServiceImpl extends ServiceImpl<AclPermissionMapper, AclPermission> implements AclPermissionService {
     @Autowired
    AclUserService userService;
     @Autowired
    AclRolePermissionService rolePermissionService;
     @Autowired
     AclRolePermissionService aclRolePermissionService;
    @Override
    public List<String> selectPermissionValueByUserId(String userId) {
        List<String> aclPermissions=null;
        if (this.isSysAdmin(userId)){
            //如果是超级管理员，获取所有菜单
            aclPermissions  = baseMapper.selectAllPermissionValue();
        }else {
            aclPermissions =  baseMapper.selectPermissionValueByUserId(userId);
        }
        return aclPermissions;

    }

    @Override
    public List<JSONObject> selectPermissionByUserId(String userId) {
        List<AclPermission> aclPermissions =null;
        if (this.isSysAdmin(userId)){
            //如果是超级管理员，获取所有菜单
             aclPermissions = baseMapper.selectList(null);
        }else {
            aclPermissions= baseMapper.selectPermissionByUserId(userId);
        }
        //TODO BUG 当用户第二次在添加其他权限的时候（例如普通管理员权限时）
        // 当管理员权限大于当前权限就出现权限重复 collect中就会出现两个树形结构导致result组装数据时为空（result结果只能是一条）
        // 查出来的permission_id集合必须去重 selectPermissionByUserId(根据userId查询其权限)方法必须加上DISTINCT字段


        //使用递归方法建菜单(树形结构)
        List<AclPermission> collect = getAclPermissions(aclPermissions);//0
      // List<AclPermission> collect = bulid(aclPermissions);//114
        List<JSONObject> result = MemuHelper.bulid(collect);
        return  result;
    }

    @Override
    public List<AclPermission> queryAllMenu() {
        List<AclPermission> aclPermissionList = baseMapper.selectList(new QueryWrapper<AclPermission>().orderByDesc("id"));
        List<AclPermission> aclPermissions = getAclPermissions(aclPermissionList);
        return aclPermissions;
    }

    @Override
    public void removeChildById(String id) {
        List<String> ids = new ArrayList<>();
        this.selectChildListById(id,ids);
        //把根据节点id放到list中
        ids.add(id);
        baseMapper.deleteBatchIds(ids);
    }



    @Override
    public List<AclPermission> selectAllMenu(String roleId) {
        List<AclPermission> allPermissionList = baseMapper.selectList(new QueryWrapper<AclPermission>().orderByAsc("CAST(id AS SIGNED)"));
        //根据角色id获取角色权限
        List<AclRolePermission> rolePermissionList = rolePermissionService.list(new QueryWrapper<AclRolePermission>().eq("role_id",roleId));
        for (int i = 0; i < allPermissionList.size(); i++) {
            AclPermission permission = allPermissionList.get(i);
            for (int m = 0; m < rolePermissionList.size(); m++) {
                AclRolePermission rolePermission = rolePermissionList.get(m);
                if(rolePermission.getPermissionId().equals(permission.getId())) {
                    permission.setSelect(true);
                }
            }
        }
        List<AclPermission> permissionList = bulid(allPermissionList);
        return permissionList;
    }

    @Override
    public void saveRolePermissionRealtionShip(String roleId, List<String> permissionIds) {
        //根据role_id查询当前RolePermission表是有否已分配的权限
        List<AclRolePermission> aclRolePermissionList = aclRolePermissionService.list(new QueryWrapper<AclRolePermission>().eq("role_id", roleId));
        aclRolePermissionList.stream().forEach(aclRolePermission -> {
            //删除权限表中已经分配的权限
            aclRolePermissionService.removeById(aclRolePermission.getId());
        });
        //roleId角色id
        //permissionId菜单id 数组形式
        //1 创建list集合，用于封装添加数据
        List<AclRolePermission> rolePermissionList = new ArrayList<>();
        //遍历所有菜单数组
        for(String perId : permissionIds) {
            //RolePermission对象
            AclRolePermission rolePermission = new AclRolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(perId);
            //封装到list集合
            rolePermissionList.add(rolePermission);
        }
        //添加到角色菜单关系表
        rolePermissionService.saveBatch(rolePermissionList);
    }

    private void selectChildListById(String id, List<String> ids) {
        List<AclPermission> aclPermissionList = baseMapper.selectList(new QueryWrapper<AclPermission>().eq("pid", id));
        aclPermissionList.stream().forEach(aclPermission -> {
            ids.add(aclPermission.getId());
            this.selectChildListById(aclPermission.getId(),ids);
        });

    }

    private List<AclPermission> getAclPermissions(List<AclPermission> aclPermissions) {
        List<AclPermission> finalAclPermissions = aclPermissions;
        List<AclPermission> collect = aclPermissions.stream().distinct().filter(aclPermission -> {
            return aclPermission.getPid().equals("0");
        }).map(aclPermission -> {
            aclPermission.setChildren(findChildren(aclPermission, finalAclPermissions));
            return aclPermission;
        }).collect(Collectors.toList());
        return collect;
    }

    private List<AclPermission> findChildren(AclPermission aclPermission, List<AclPermission> finalAclPermissions) {
        List<AclPermission> collect = finalAclPermissions.stream().filter(aclPermission1 -> {
            return aclPermission1.getPid().equals(aclPermission.getId());
        }).map(aclPermission1 -> {
            aclPermission1.setChildren(findChildren(aclPermission1, finalAclPermissions));
            return aclPermission1;
        }).collect(Collectors.toList());
        return collect;
    }


    /**
     * 判断用户是否系统管理员
     * @param userId
     * @return
     */
    private boolean isSysAdmin(String userId) {
        AclUser aclUser = userService.getById(userId);
        if (!StringUtils.isEmpty(aclUser)&&"admin".equals(aclUser.getUsername())){
            return true;
        }else {
            return false;
        }
    }
}
