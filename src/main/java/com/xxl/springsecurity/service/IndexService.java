package com.xxl.springsecurity.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface IndexService {
    Map<String, Object> getUserInfo(String username);
    /**
     * 根据用户名获取动态菜单
     * @param username
     * @return
     */
    List<JSONObject> getMenu(String username);
}
