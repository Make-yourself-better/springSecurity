package com.xxl.springsecurity.controller.sys;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.springsecurity.entity.AclUser;
import com.xxl.springsecurity.service.AclRoleService;
import com.xxl.springsecurity.service.AclUserService;
import com.xxl.springsecurity.utils.result.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author xxl
 * @since 2022-10-16
 */
@RestController
@RequestMapping("/admin/acl/user")
@CrossOrigin
public class AclUserController {
    @Autowired
    AclUserService aclUserService;
    @Autowired
    AclRoleService aclRoleService;
    @ApiOperation(value = "获取管理用户分页列表")
    @GetMapping("{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                    AclUser userQueryVo) {
        Page<AclUser> pageParam = new Page<>(page, limit);
        QueryWrapper<AclUser> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(userQueryVo.getUsername())) {
            wrapper.like("username",userQueryVo.getUsername());
        }
        IPage<AclUser> pageModel = aclUserService.page(pageParam, wrapper);
        return R.ok().data("items", pageModel.getRecords()).data("total", pageModel.getTotal());
    }

    @ApiOperation(value = "新增管理用户")
    @PostMapping("save")                          //,不是统一异常处理必须加上 BindingResult bindingResult
    public R save( @Valid @RequestBody AclUser aclUser) {
//        if (bindingResult.hasErrors()){
//            Map<String, Object> map = new HashMap<>();
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            fieldErrors.stream().forEach(fieldError -> {
//                String field = fieldError.getField();
//                String message = fieldError.getDefaultMessage();
//                map.put(field,message);
//            });
//            return R.error().data(map);
//        }
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String encodePassword = bCryptPasswordEncoder.encode(aclUser.getPassword());
            aclUser.setPassword(encodePassword);
            aclUserService.save(aclUser);
            return R.ok();

    }
    @ApiOperation(value = "修改管理用户")
    @PutMapping("update")
    public R updateById(@RequestBody AclUser user) {
        aclUserService.updateById(user);
        return R.ok();
    }

    @ApiOperation(value = "删除管理用户")
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id) {
        aclUserService.removeById(id);
        return R.ok();
    }
    @ApiOperation(value = "根据id列表删除管理用户")
    @DeleteMapping("batchRemove")
    public R batchRemove(@RequestBody List<String> idList) {
        aclUserService.removeByIds(idList);
        return R.ok();
    }
    @ApiOperation(value = "根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public R toAssign(@PathVariable String userId){
        Map<String, Object> roleMap = aclRoleService.findRoleByUserId(userId);
        return R.ok().data(roleMap);
    }

    @ApiOperation(value = "根据用户分配角色")
    @PostMapping("/doAssign/{userId}")
    public R doAssign(@PathVariable("userId") String userId,
                      @RequestBody String[] roleIds){
        aclRoleService.saveUserRoleRealtionShip(userId, roleIds);
        return R.ok();
    }

    //修改时的数据回显
    @GetMapping("get/{id}")
    public R showUser(@PathVariable("id")String id){
        AclUser aclUser = aclUserService.getById(id);
        return R.ok().data("item",aclUser);
    }
}

