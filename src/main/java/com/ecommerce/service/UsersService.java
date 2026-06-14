package com.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommerce.entity.Users;

public interface UsersService extends IService<Users> {

    /**
     * 用户注册（密码BCrypt加密）
     */
    void register(String username, String password, String phone);

    /**
     * 用户登录，返回用户ID
     */
    Integer login(String username, String password);
}