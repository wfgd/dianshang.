package com.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommerce.entity.Products;
import com.ecommerce.mapper.ProductsMapper;
import com.ecommerce.service.ProductsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products> implements ProductsService {

    @Override
    public Page<Products> pageQuery(int page, int size, String keyword) {
        Page<Products> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Products> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Products::getName, keyword);
        }
        wrapper.orderByAsc(Products::getProductId);
        return baseMapper.selectPage(pageParam, wrapper);
    }
}