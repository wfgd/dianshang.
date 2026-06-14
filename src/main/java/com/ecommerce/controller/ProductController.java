package com.ecommerce.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecommerce.entity.Products;
import com.ecommerce.service.ProductsService;
import com.ecommerce.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductsService productsService;

    @GetMapping
    public Result<Page<Products>> list(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(required = false) String keyword) {
        Page<Products> result = productsService.pageQuery(page, size, keyword);
        return Result.success(result);
    }
}