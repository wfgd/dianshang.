package com.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommerce.entity.Cart;

import java.util.List;

public interface CartService extends IService<Cart> {

    /**
     * 添加商品到购物车
     */
    void addToCart(Integer userId, Integer productId, Integer quantity);

    /**
     * 查询用户购物车列表
     */
    List<Cart> listByUserId(Integer userId);
}