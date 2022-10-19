package com.xxl.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxl.springsecurity.entity.AclRole;
import com.xxl.springsecurity.entity.AclUser;
import com.xxl.springsecurity.entity.AclUserRole;
import com.xxl.springsecurity.mapper.AclUserMapper;
import com.xxl.springsecurity.service.AclRoleService;
import com.xxl.springsecurity.service.AclUserRoleService;
import com.xxl.springsecurity.service.AclUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xxl
 * @since 2022-10-16
 */
@Service
public class AclUserServiceImpl extends ServiceImpl<AclUserMapper, AclUser> implements AclUserService {
    @Autowired
    AclUserRoleService userRoleService;

    @Override
    public AclUser selectByUserName(String username) {
        AclUser aclUser = baseMapper.selectOne(new QueryWrapper<AclUser>().eq("username", username));
        return aclUser;

    }


}
