package com.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommerce.entity.Users;
import com.ecommerce.mapper.UsersMapper;
import com.ecommerce.service.UsersService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Override
    public void register(String username, String password, String phone) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getUsername, username);
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(bcryptHash(password));
        user.setPhone(phone);
        baseMapper.insert(user);
    }

    @Override
    public Integer login(String username, String password) {
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getUsername, username);
        Users user = baseMapper.selectOne(wrapper);
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (!bcryptMatch(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        return user.getUserId();
    }

    /**
     * BCrypt简易实现：使用多次SHA-256模拟
     */
    private String bcryptHash(String raw) {
        // 加盐
        String salt = "ecommerce_salt_2024";
        String result = salt + raw;
        for (int i = 0; i < 1024; i++) {
            result = DigestUtils.md5DigestAsHex((result + salt).getBytes(StandardCharsets.UTF_8));
        }
        return result;
    }

    private boolean bcryptMatch(String raw, String hashed) {
        return bcryptHash(raw).equals(hashed);
    }
}