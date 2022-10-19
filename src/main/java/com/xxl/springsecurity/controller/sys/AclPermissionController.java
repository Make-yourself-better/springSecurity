package com.xxl.springsecurity.controller.sys;


import com.xxl.springsecurity.entity.AclPermission;
import com.xxl.springsecurity.entity.AclRolePermission;
import com.xxl.springsecurity.service.AclPermissionService;
import com.xxl.springsecurity.utils.result.R;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 权限 前端控制器
 * </p>
 *
 * @author xxl
 * @since 2022-10-16
 */
@Slf4j
@RestController
@RequestMapping("/admin/acl/permission")
public class AclPermissionController {
    @Autowired
    AclPermissionService permissionService;
    //获取全部菜单
    @ApiOperation(value = "查询所有菜单")
    @GetMapping
    public R indexAllPermission() {
        List<AclPermission> list =  permissionService.queryAllMenu();
        return R.ok().data("children",list);
    }

    @ApiOperation(value = "递归删除菜单")
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id) {
        permissionService.removeChildById(id);
        return R.ok();
    }
    @ApiOperation(value = "给角色分配权限")
    @PostMapping("/doAssign")
    public R doAssign(String roleId,String[] permissionId) {
        permissionService.saveRolePermissionRealtionShip(roleId, Arrays.asList(permissionId));
        return R.ok();
    }
    @ApiOperation(value = "根据角色获取菜单")
    @GetMapping("toAssign/{roleId}")
    public R toAssign(@PathVariable String roleId) {
        List<AclPermission> list = permissionService.selectAllMenu(roleId);
        return R.ok().data("children", list);
    }
    @ApiOperation(value = "新增菜单")
    @PostMapping("save")
    public R save(@RequestBody AclPermission permission) {
        permissionService.save(permission);
        return R.ok();
    }
    @ApiOperation(value = "修改菜单")
    @PutMapping("update")
    public R updateById(@RequestBody AclPermission permission) {
        permissionService.updateById(permission);
        return R.ok();
    }

}

