package com.ecommerce.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommerce.entity.OrderItems;
import com.ecommerce.entity.Orders;

import java.util.List;
import java.util.Map;

public interface OrdersService extends IService<Orders> {

    /**
     * 从购物车下单
     */
    Integer createOrder(Integer userId, List<Integer> cartItemIds);

    /**
     * 分页查询我的订单
     */
    Page<Orders> pageMyOrders(Integer userId, int page, int size);

    /**
     * 查询单个订单（含明细）
     */
    Map<String, Object> getOrderDetail(Integer orderId, Integer userId);

    /**
     * 修改订单状态（支付/取消）
     */
    void updateOrderStatus(Integer orderId, Integer userId, Integer status);

    /**
     * 删除订单（含明细）
     */
    void deleteOrder(Integer orderId, Integer userId);
}