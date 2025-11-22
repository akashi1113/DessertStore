package com.csu.analyticsservice.service;

import com.csu.analyticsservice.dto.*;

import java.util.Map;

public interface AnalyticsService {

    /**
     * 获取销售统计数据
     */
    SalesStatisticsDTO getSalesStatistics(String token);

    /**
     * 获取用户分析数据
     */
    UserAnalyticsDTO getUserAnalytics(String token);

    /**
     * 获取商品分析数据
     */
    ProductAnalyticsDTO getProductAnalytics();

    /**
     * 获取综合仪表盘数据
     */
    DashboardDTO getDashboardData(String token);
}
