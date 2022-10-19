package com.xxl.springsecurity.security.handel;



import com.alibaba.fastjson.JSON;
import com.xxl.springsecurity.utils.result.R;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//未登录 或token过期
@Service
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        Object result = R.error().data("账号密码错误", HttpStatus.UNAUTHORIZED.value());
        String json = JSON.toJSONString(result);
        response.getWriter().write(json);
        writer.flush();
        writer.close();
    }
}
