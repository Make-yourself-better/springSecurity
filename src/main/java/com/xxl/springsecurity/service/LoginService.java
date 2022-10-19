package com.xxl.springsecurity.service;

import com.xxl.springsecurity.security.userDetails.LoginUser;

import java.util.Map;

public interface LoginService {
    String login(Map<String, String> map);



}
