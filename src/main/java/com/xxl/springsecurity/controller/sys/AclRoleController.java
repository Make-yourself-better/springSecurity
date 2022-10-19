package com.xxl.springsecurity.controller.sys;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.springsecurity.entity.AclRole;
import com.xxl.springsecurity.service.AclRoleService;
import com.xxl.springsecurity.utils.result.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xxl
 * @since 2022-10-16
 */
@RestController
@RequestMapping("/admin/acl/role")
public class AclRoleController {
    @Autowired
    AclRoleService roleService;

    @ApiOperation(value = "获取角色分页列表")
    @GetMapping("{page}/{limit}")
    public R index(@ApiParam(name = "page", value = "当前页码", required = true)
                   @PathVariable Long page,
                   @ApiParam(name = "limit", value = "每页记录数", required = true)
                   @PathVariable Long limit, AclRole aclRole){
        Page<AclRole> pageParam = new Page<>(page, limit);
        QueryWrapper<AclRole> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(aclRole.getRoleName())) {
            wrapper.like("role_name",aclRole.getRoleName());
        }
        roleService.page(pageParam,wrapper);
        return R.ok().data("items", pageParam.getRecords()).data("total", pageParam.getTotal());

    }
    @ApiOperation(value = "新增角色")
    @PostMapping("save")
    public R save(@RequestBody AclRole role) {
        roleService.save(role);
        return R.ok();
    }
    @ApiOperation(value = "修改角色")
    @PutMapping("update")
    public R updateById(@RequestBody AclRole role) {
        roleService.updateById(role);
        return R.ok();
    }
    @ApiOperation(value = "删除角色")
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id) {
        roleService.removeById(id);
        return R.ok();
    }
    @ApiOperation(value = "根据id列表删除角色")
    @DeleteMapping("batchRemove")
    public R batchRemove(@RequestBody List<String> idList) {
        roleService.removeByIds(idList);
        return R.ok();
    }

}

