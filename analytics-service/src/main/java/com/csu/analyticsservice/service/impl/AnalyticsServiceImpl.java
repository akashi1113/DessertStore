package com.csu.analyticsservice.service.impl;

import com.csu.analyticsservice.dto.*;
import com.csu.analyticsservice.feign.*;
import com.csu.analyticsservice.service.AnalyticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private OrderServiceClient orderServiceClient;

    @Autowired
    private ProductServiceClient productServiceClient;

    @Autowired
    private UserServiceClient userServiceClient;

    @Override
    public SalesStatisticsDTO getSalesStatistics(String token) {
        SalesStatisticsDTO dto = new SalesStatisticsDTO();

        try {
            Map<String, Object> orderResponse = orderServiceClient.getOrderHistory(token);

            if ("SUCCESS".equals(orderResponse.get("status"))) {
                List<Map<String, Object>> orders = (List<Map<String, Object>>) orderResponse.get("data");

                BigDecimal totalSales = orders.stream()
                        .map(order -> new BigDecimal(order.get("grandTotal").toString()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                Integer totalOrders = orders.size();

                BigDecimal avgOrderValue = totalOrders > 0
                        ? totalSales.divide(new BigDecimal(totalOrders), 2, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;

                dto.setTotalSales(totalSales);
                dto.setTotalOrders(totalOrders);
                dto.setAvgOrderValue(avgOrderValue);

                Map<String, SalesStatisticsDTO.DailySalesDTO> dailyMap = new HashMap<>();
                for (Map<String, Object> order : orders) {
                    String date = order.get("orderDate").toString().substring(0, 10);
                    BigDecimal amount = new BigDecimal(order.get("grandTotal").toString());

                    dailyMap.computeIfAbsent(date, k -> {
                        SalesStatisticsDTO.DailySalesDTO daily = new SalesStatisticsDTO.DailySalesDTO();
                        daily.setDate(date);
                        daily.setAmount(BigDecimal.ZERO);
                        daily.setCount(0);
                        return daily;
                    });

                    SalesStatisticsDTO.DailySalesDTO daily = dailyMap.get(date);
                    daily.setAmount(daily.getAmount().add(amount));
                    daily.setCount(daily.getCount() + 1);
                }

                dto.setDailySales(new ArrayList<>(dailyMap.values()));
            }
        } catch (Exception e) {
            log.error("获取销售统计失败", e);
        }

        return dto;
    }

    @Override
    public UserAnalyticsDTO getUserAnalytics(String token) {
        UserAnalyticsDTO dto = new UserAnalyticsDTO();

        try {
            Map<String, Object> orderResponse = orderServiceClient.getOrderHistory(token);

            if ("SUCCESS".equals(orderResponse.get("status"))) {
                List<Map<String, Object>> orders = (List<Map<String, Object>>) orderResponse.get("data");

                Set<Long> activeUserIds = orders.stream()
                        .map(order -> Long.parseLong(order.get("userId").toString()))
                        .collect(Collectors.toSet());

                dto.setActiveUsers(activeUserIds.size());

                Map<String, Integer> vipMap = new HashMap<>();
                for (Long userId : activeUserIds) {
                    try {
                        Map<String, Object> userResponse = userServiceClient.getAccount(token, userId);
                        if ("SUCCESS".equals(userResponse.get("status"))) {
                            Map<String, Object> userData = (Map<String, Object>) userResponse.get("data");
                            String vipLevel = userData.get("VIPLevel").toString();
                            vipMap.put(vipLevel, vipMap.getOrDefault(vipLevel, 0) + 1);
                        }
                    } catch (Exception e) {
                        log.warn("获取用户{}信息失败", userId, e);
                    }
                }

                List<UserAnalyticsDTO.VIPLevelDTO> vipList = vipMap.entrySet().stream()
                        .map(entry -> {
                            UserAnalyticsDTO.VIPLevelDTO vip = new UserAnalyticsDTO.VIPLevelDTO();
                            vip.setLevel(entry.getKey());
                            vip.setCount(entry.getValue());
                            return vip;
                        })
                        .collect(Collectors.toList());

                dto.setVipDistribution(vipList);
                dto.setTotalUsers(activeUserIds.size());
            }
        } catch (Exception e) {
            log.error("获取用户分析失败", e);
        }

        return dto;
    }

    @Override
    public ProductAnalyticsDTO getProductAnalytics() {
        ProductAnalyticsDTO dto = new ProductAnalyticsDTO();

        try {
            Map<String, Object> categoryResponse = productServiceClient.getCategoryList();
            if ("SUCCESS".equals(categoryResponse.get("status"))) {
                List<Map<String, Object>> categories = (List<Map<String, Object>>) categoryResponse.get("data");
                dto.setTotalCategories(categories.size());

                int totalProducts = 0;
                for (Map<String, Object> category : categories) {
                    String categoryId = category.get("categoryid").toString();
                    Map<String, Object> productResponse = productServiceClient.getProductListByCategory(categoryId);
                    if ("SUCCESS".equals(productResponse.get("status"))) {
                        List<Map<String, Object>> products = (List<Map<String, Object>>) productResponse.get("data");
                        totalProducts += products.size();
                    }
                }
                dto.setTotalProducts(totalProducts);
            }

            dto.setTopProducts(new ArrayList<>());

        } catch (Exception e) {
            log.error("获取商品分析失败", e);
        }

        return dto;
    }

    @Override
    public DashboardDTO getDashboardData(String token) {
        DashboardDTO dto = new DashboardDTO();
        dto.setSalesData(getSalesStatistics(token));
        dto.setUserData(getUserAnalytics(token));
        dto.setProductData(getProductAnalytics());
        return dto;
    }
}

