package com.xxl.springsecurity.security.filter;


import com.xxl.springsecurity.utils.constant.CacheConstants;
import com.xxl.springsecurity.utils.securityUtils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyJwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private JwtUtils jwtUtils;

    //拦截器 在这里进行用户token 的认证
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       //判断是否有请求头 如果没有直接放行 如果有则对比   Authorization在yml中已经配置
        String header = request.getHeader("token");
        if (StringUtils.isNotBlank(header)){
            //如果用户登录携带了请求头
            //解析请求头返回一个uuid
            String uuid = jwtUtils.getUserNameFormToken(header);
            //因为将登录用户存入token时是按照USER_TOKEN_UUID+uuid生成的前缀
            //根据请求头的token取出用户信息  redisTemplate.opsForValue().set(CacheConstants.USER_TOKEN_UUID+uuid,loginUser,expiration,TimeUnit.MILLISECONDS);
            UserDetails user = (UserDetails) redisTemplate.opsForValue().get(CacheConstants.USER_TOKEN_UUID+uuid);
            if (user!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
                //在这里进行token判断如果快要超时就添加一个新的token
               //用户名和密码之后首先会进入到UsernamePasswordAuthenticationToken验证(Authentication)
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                //更改当前经过身份验证的主体，或删除身份验证信息。
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        //放行拦截器
        filterChain.doFilter(request,response);
    }
}
