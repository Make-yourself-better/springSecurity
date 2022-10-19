package com.xxl.springsecurity.security.userDetails;


import com.xxl.springsecurity.mapper.LoginMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //通过用户名查询用户信息
        if (StringUtils.isEmpty(username)){
            throw new UsernameNotFoundException("用户名未填写");
        }
        LoginUser longinUser = loginMapper.queryUserByUserName(username);
        //查询用户对应的权限 并将权限信息放入Authorities中
        longinUser.setAuthorities(power(longinUser.getId()));

        return longinUser;
    }

    private List<GrantedAuthority> power(Long id) {
        List<String> powers = loginMapper.getPower(id);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",",powers));
    }


}

