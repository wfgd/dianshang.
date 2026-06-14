package com.ecommerce.controller;

import com.ecommerce.entity.Cart;
import com.ecommerce.service.CartService;
import com.ecommerce.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public Result<String> add(@RequestAttribute("userId") Integer userId,
                               @RequestBody Map<String, Integer> body) {
        Integer productId = body.get("productId");
        Integer quantity = body.getOrDefault("quantity", 1);
        cartService.addToCart(userId, productId, quantity);
        return Result.success("添加成功");
    }

    @GetMapping("/list")
    public Result<List<Cart>> list(@RequestAttribute("userId") Integer userId) {
        List<Cart> cartList = cartService.listByUserId(userId);
        return Result.success(cartList);
    }
}