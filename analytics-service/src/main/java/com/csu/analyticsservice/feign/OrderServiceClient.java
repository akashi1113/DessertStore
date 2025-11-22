package com.csu.analyticsservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "order-service", path = "/orders")
public interface OrderServiceClient {

    /**
     * 获取订单历史
     * 对应：GET /orders
     */
    @GetMapping
    Map<String, Object> getOrderHistory(@RequestHeader("Authorization") String token);

    /**
     * 获取订单详情
     * 对应：GET /orders/{orderId}
     */
    @GetMapping("/{orderId}")
    Map<String, Object> getOrderDetails(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId
    );
}

