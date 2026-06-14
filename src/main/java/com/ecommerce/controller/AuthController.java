package com.ecommerce.controller;

import com.ecommerce.dto.LoginRequest;
import com.ecommerce.service.UsersService;
import com.ecommerce.util.JwtUtil;
import com.ecommerce.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/api/auth/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            Integer userId = usersService.login(request.getUsername(), request.getPassword());
            String token = jwtUtil.generateToken(userId, request.getUsername());
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userId", userId);
            return Result.success(data);
        } catch (RuntimeException e) {
            return Result.error(401, e.getMessage());
        }
    }

    @PostMapping("/api/users/register")
    public Result<String> register(@RequestBody Map<String, String> body) {
        try {
            usersService.register(
                    body.get("username"),
                    body.get("password"),
                    body.getOrDefault("phone", "")
            );
            return Result.success("注册成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}