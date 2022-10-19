package com.xxl.springsecurity.mapper;



import com.xxl.springsecurity.security.userDetails.LoginUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoginMapper {



    List<String> getPower(@Param("uid") Long id);


    LoginUser queryUserByUserName(@Param("username") String username);
}
