package com.csu.productservice.mapper;

import com.csu.productservice.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper {
    List<Category> selectAllCategory();

    Category selectById(String categoryId);
}
