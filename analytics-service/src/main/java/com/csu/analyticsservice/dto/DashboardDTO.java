package com.csu.analyticsservice.dto;

import lombok.Data;

@Data
public class DashboardDTO {
    private SalesStatisticsDTO salesData;
    private UserAnalyticsDTO userData;
    private ProductAnalyticsDTO productData;
}

