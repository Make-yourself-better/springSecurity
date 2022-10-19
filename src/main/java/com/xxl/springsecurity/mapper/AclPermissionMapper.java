package com.xxl.springsecurity.mapper;

import com.alibaba.fastjson.JSONObject;
import com.xxl.springsecurity.entity.AclPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author xxl
 * @since 2022-10-16
 */
public interface AclPermissionMapper extends BaseMapper<AclPermission> {

    List<AclPermission> selectPermissionByUserId(@Param("userId") String userId);

    List<String> selectAllPermissionValue();

    List<String> selectPermissionValueByUserId(String userId);
}
