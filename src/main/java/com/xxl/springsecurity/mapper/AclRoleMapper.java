package com.xxl.springsecurity.mapper;

import com.xxl.springsecurity.entity.AclRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xxl
 * @since 2022-10-16
 */
public interface AclRoleMapper extends BaseMapper<AclRole> {

    List<String> selectRoleByUserId(@Param("id") String id);
}
