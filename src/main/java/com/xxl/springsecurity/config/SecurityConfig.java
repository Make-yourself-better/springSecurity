package com.xxl.springsecurity.config;

import com.xxl.springsecurity.security.filter.MyJwtAuthenticationTokenFilter;
import com.xxl.springsecurity.security.handel.MyAccessDeniedHandler;
import com.xxl.springsecurity.security.handel.MyAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    MyAccessDeniedHandler accessDeniedHandler;

    @Autowired
    MyAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    MyJwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
   //放行的白名单
    private static final String[] URL_WHITELIST = {
            "/captchaImage",
            "/user/login",
            "/admin/acl/index/login"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //允许跨域 并关闭防护
        http.cors().and().csrf().disable();
        //异常处理
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint);
        //.and().logout().logoutUrl("/admin/acl/index/logout");
        //全局禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //请求拦截
        http.authorizeRequests().antMatchers(URL_WHITELIST).permitAll().anyRequest().authenticated();


        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //禁用缓存 全局使用json
        http.headers().cacheControl();
    }


    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //认证管理器
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        return authenticationManager;
    }
    //不进行认证的路径，可以直接访问
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/**");
    }
}
