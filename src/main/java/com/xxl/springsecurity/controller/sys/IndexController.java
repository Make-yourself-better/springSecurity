package com.xxl.springsecurity.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.xxl.springsecurity.security.userDetails.LoginUser;
import com.xxl.springsecurity.service.IndexService;
import com.xxl.springsecurity.service.LoginService;
import com.xxl.springsecurity.utils.constant.CacheConstants;
import com.xxl.springsecurity.utils.result.R;
import com.xxl.springsecurity.utils.securityUtils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/acl/index")
public class IndexController {
    @Autowired
    IndexService indexService;
    @Autowired
    private LoginService loginService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/login")
    public R login(@RequestBody Map<String,String> map){
        if (CollectionUtils.isEmpty(map)){
            throw new ArithmeticException("参数错误");
        }
        String token = loginService.login(map);
        return R.ok().data("token",token);
    }
    /**
     * 根据token获取用户信息
     */
    @GetMapping("info")
    public R info(){
        //获取当前用户登录名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> userInfo  = indexService.getUserInfo(username);
        return R.ok().data(userInfo);
    }
    /**
     * 获取菜单
     * @return
     */
    @GetMapping("menu")
    public R getMenu(){
        //获取当前登录用户用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<JSONObject> permissionList = indexService.getMenu(username);
        return R.ok().data("permissionList", permissionList);
    }

    @PostMapping("logout")
    public R logout(HttpServletRequest request){
        String token = request.getHeader("token");
        String uuid = jwtUtils.getUserNameFormToken(token);
        //删除即可
        redisTemplate.delete((CacheConstants.USER_TOKEN_UUID + uuid));
        return R.ok();
    }
}
