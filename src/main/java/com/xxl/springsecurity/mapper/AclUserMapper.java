package com.xxl.springsecurity.mapper;

import com.xxl.springsecurity.entity.AclUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author xxl
 * @since 2022-10-16
 */
public interface AclUserMapper extends BaseMapper<AclUser> {

    AclUser getuserByname(@Param("username") String username);


}
