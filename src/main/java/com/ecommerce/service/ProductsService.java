package com.ecommerce.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommerce.entity.Products;

public interface ProductsService extends IService<Products> {

    /**
     * 分页模糊搜索商品
     */
    Page<Products> pageQuery(int page, int size, String keyword);
}