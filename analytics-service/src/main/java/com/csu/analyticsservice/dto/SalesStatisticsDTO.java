package com.csu.analyticsservice.dto;


import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SalesStatisticsDTO {
    private BigDecimal totalSales;        // 总销售额
    private Integer totalOrders;          // 总订单数
    private BigDecimal avgOrderValue;     // 平均订单金额
    private List<DailySalesDTO> dailySales; // 每日销售

    @Data
    public static class DailySalesDTO {
        private String date;
        private BigDecimal amount;
        private Integer count;
    }
}
