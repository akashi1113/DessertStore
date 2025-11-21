package com.csu.productservice.mapper;

import com.csu.productservice.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository()
public interface ProductMapper {
    Product selectById(String productId);

    List<Product> selectByCategory(String categoryId);

}
