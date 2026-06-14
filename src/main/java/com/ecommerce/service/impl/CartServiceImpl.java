package com.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommerce.entity.Cart;
import com.ecommerce.mapper.CartMapper;
import com.ecommerce.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Override
    public void addToCart(Integer userId, Integer productId, Integer quantity) {
        // 检查是否已存在相同商品的购物车记录
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
               .eq(Cart::getProductId, productId);
        Cart existCart = baseMapper.selectOne(wrapper);

        if (existCart != null) {
            // 已存在则追加数量
            existCart.setQuantity(existCart.getQuantity() + quantity);
            baseMapper.updateById(existCart);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            baseMapper.insert(cart);
        }
    }

    @Override
    public List<Cart> listByUserId(Integer userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        return baseMapper.selectList(wrapper);
    }
}