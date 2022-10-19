//package com.xxl.springsecurity.controller.sys;
//
//
//import com.xxl.springsecurity.service.LoginService;
//import com.xxl.springsecurity.utils.result.R;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/user/")
//@CrossOrigin
//public class LoginController {
//    @Autowired
//    private LoginService loginService;
//
//    @PostMapping("login")
//    public R login(@RequestBody Map<String,String> map){
//        if (CollectionUtils.isEmpty(map)){
//          throw new ArithmeticException("参数错误");
//        }
//        String token = loginService.login(map);
//        return R.ok().data("token",token);
//
//    }
//}
