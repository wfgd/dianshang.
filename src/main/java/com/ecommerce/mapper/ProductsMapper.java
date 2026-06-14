package com.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecommerce.entity.Products;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductsMapper extends BaseMapper<Products> {
}