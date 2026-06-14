package com.ecommerce.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommerce.entity.Orders;

import java.util.List;

public interface OrdersService extends IService<Orders> {

    /**
     * 从购物车下单
     */
    Integer createOrder(Integer userId, List<Integer> cartItemIds);

    /**
     * 分页查询我的订单
     */
    Page<Orders> pageMyOrders(Integer userId, int page, int size);
}