package com.xxl.springsecurity.service.impl;


import com.xxl.springsecurity.security.userDetails.LoginUser;
import com.xxl.springsecurity.service.LoginService;
import com.xxl.springsecurity.utils.exception.CaptchaExpireException;
import com.xxl.springsecurity.utils.constant.CacheConstants;
import com.xxl.springsecurity.utils.securityUtils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    //获取token
    @Override
    public String login(Map<String, String> map) {
        //获取用户输入的信息
        String username = map.get("username");
        String password = map.get("password");
//        String uuid = map.get("uuid");
//        String code = map.get("code");
//
//        //获取reids中的验证码
//        String captcha = (String) redisTemplate.opsForValue().get(CacheConstants.CAPTCHA_CODE_KEY + uuid);
//        //比对验证码
//        if (StringUtils.isNotBlank(captcha)){
//            if (!captcha.equals(code)){
//                throw new CaptchaExpireException();
//            }
//        }else {
//            throw new CaptchaExpireException();
//        }
        //进入UserDetailsService进行用户查询 如果没有该用户输入的用户信息则失败 进入失败处理器 进行用户权限的设置
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        //上一步已经将用户相关的权限信息放入 直接取出来
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        //根据用户信息生成token  根据uuid生成  并存入reids中
        return jwtUtils.generatorToken(loginUser);

    }


}
