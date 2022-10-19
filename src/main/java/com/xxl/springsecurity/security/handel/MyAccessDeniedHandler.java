package com.xxl.springsecurity.security.handel;
import com.alibaba.fastjson.JSON;

import com.xxl.springsecurity.utils.result.R;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Service
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    //权限认证处理器
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        Object result = R.error().data("认证失败请重新登录", HttpStatus.UNAUTHORIZED.value());
        String json = JSON.toJSONString(result);
        response.getWriter().write(json);
        writer.flush();
        writer.close();
    }

}
