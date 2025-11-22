package com.csu.analyticsservice.controller;

import com.csu.analyticsservice.dto.DashboardDTO;
import com.csu.analyticsservice.dto.ProductAnalyticsDTO;
import com.csu.analyticsservice.dto.SalesStatisticsDTO;
import com.csu.analyticsservice.dto.UserAnalyticsDTO;
import com.csu.analyticsservice.service.AnalyticsService;
import com.csu.analyticsservice.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "http://localhost:5173")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * 获取销售统计数据
     * GET /api/analytics/sales
     */
    @GetMapping("/sales")
    public Result<SalesStatisticsDTO> getSalesStatistics(
            @RequestHeader("Authorization") String token) {
        SalesStatisticsDTO data = analyticsService.getSalesStatistics(token);
        return Result.success(data);
    }

    /**
     * 获取用户分析数据
     * GET /api/analytics/users
     */
    @GetMapping("/users")
    public Result<UserAnalyticsDTO> getUserAnalytics(
            @RequestHeader("Authorization") String token) {
        UserAnalyticsDTO data = analyticsService.getUserAnalytics(token);
        return Result.success(data);
    }

    /**
     * 获取商品分析数据
     * GET /api/analytics/products
     */
    @GetMapping("/products")
    public Result<ProductAnalyticsDTO> getProductAnalytics() {
        ProductAnalyticsDTO data = analyticsService.getProductAnalytics();
        return Result.success(data);
    }

    /**
     * 获取综合仪表盘数据
     * GET /api/analytics/dashboard
     */
    @GetMapping("/dashboard")
    public Result<DashboardDTO> getDashboardData(
            @RequestHeader("Authorization") String token) {
        DashboardDTO data = analyticsService.getDashboardData(token);
        return Result.success(data);
    }
}

