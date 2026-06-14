package com.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommerce.entity.*;
import com.ecommerce.mapper.*;
import com.ecommerce.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createOrder(Integer userId, List<Integer> cartItemIds) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 创建订单
        Orders order = new Orders();
        order.setUserId(userId);
        order.setStatus(0);
        order.setCreateTime(new Date());
        order.setTotalAmount(BigDecimal.ZERO);
        baseMapper.insert(order);
        Integer orderId = order.getOrderId();

        for (Integer cartId : cartItemIds) {
            // 查询购物车记录
            Cart cart = cartMapper.selectById(cartId);
            if (cart == null || !cart.getUserId().equals(userId)) {
                throw new RuntimeException("购物车记录不存在: cartId=" + cartId);
            }

            // 查询商品
            Products product = productsMapper.selectById(cart.getProductId());
            if (product == null) {
                throw new RuntimeException("商品不存在: productId=" + cart.getProductId());
            }

            // 检查库存
            if (product.getStock() < cart.getQuantity()) {
                throw new RuntimeException("商品[" + product.getName() + "]库存不足，当前库存: " + product.getStock());
            }

            // 扣减库存
            product.setStock(product.getStock() - cart.getQuantity());
            productsMapper.updateById(product);

            // 计算金额（快照价格）
            BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(cart.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            // 创建订单明细
            OrderItems item = new OrderItems();
            item.setOrderId(orderId);
            item.setProductId(product.getProductId());
            item.setQuantity(cart.getQuantity());
            item.setPrice(product.getPrice());
            orderItemsMapper.insert(item);

            // 删除购物车记录
            cartMapper.deleteById(cartId);
        }

        // 更新订单总金额
        order.setTotalAmount(totalAmount);
        baseMapper.updateById(order);

        return orderId;
    }

    @Override
    public Page<Orders> pageMyOrders(Integer userId, int page, int size) {
        Page<Orders> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, userId)
               .orderByDesc(Orders::getCreateTime);
        return baseMapper.selectPage(pageParam, wrapper);
    }
}