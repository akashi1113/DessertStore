package com.csu.analyticsservice.dto;


import lombok.Data;
import java.util.List;

@Data
public class ProductAnalyticsDTO {
    private Integer totalProducts;        // 总商品数
    private Integer totalCategories;      // 总分类数
    private List<TopProductDTO> topProducts; // 热销商品

    @Data
    public static class TopProductDTO {
        private String productId;
        private String productName;
        private Integer salesCount;
    }
}

