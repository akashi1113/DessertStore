package com.csu.analyticsservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    /**
     * 获取所有分类
     * 对应：GET /category/categories
     */
    @GetMapping("/category/categories")
    Map<String, Object> getCategoryList();

    /**
     * 根据分类获取商品列表
     * 对应：GET /product/products/{id}
     */
    @GetMapping("/product/products/{id}")
    Map<String, Object> getProductListByCategory(@PathVariable("id") String categoryId);

    /**
     * 根据商品获取规格列表
     * 对应：GET /item/items/{id}
     */
    @GetMapping("/item/items/{id}")
    Map<String, Object> getItemListByProduct(@PathVariable("id") String productId);

    /**
     * 搜索商品
     * 对应：GET /item/search?keywords=xxx
     */
    @GetMapping("/item/search")
    Map<String, Object> searchItem(String keywords);
}

