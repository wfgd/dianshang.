package com.ecommerce.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecommerce.dto.OrderCreateDTO;
import com.ecommerce.entity.OrderItems;
import com.ecommerce.entity.Orders;
import com.ecommerce.mapper.OrderItemsMapper;
import com.ecommerce.service.OrdersService;
import com.ecommerce.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @PostMapping("/create")
    public Result<Map<String, Object>> create(@RequestAttribute("userId") Integer userId,
                                               @RequestBody OrderCreateDTO dto) {
        try {
            Integer orderId = ordersService.createOrder(userId, dto.getCartItemIds());
            Map<String, Object> data = new HashMap<>();
            data.put("orderId", orderId);
            return Result.success("下单成功", data);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my")
    public Result<Map<String, Object>> myOrders(@RequestAttribute("userId") Integer userId,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        Page<Orders> orderPage = ordersService.pageMyOrders(userId, page, size);

        // 查询每个订单的明细
        Map<Integer, List<OrderItems>> itemsMap = new HashMap<>();
        for (Orders order : orderPage.getRecords()) {
            List<OrderItems> items = orderItemsMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OrderItems>()
                            .eq(OrderItems::getOrderId, order.getOrderId())
            );
            itemsMap.put(order.getOrderId(), items);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("orders", orderPage.getRecords());
        data.put("total", orderPage.getTotal());
        data.put("items", itemsMap);
        return Result.success(data);
    }
}