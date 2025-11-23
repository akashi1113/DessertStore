/**
 * 用于返回订单详细信息的 DTO (GET /orders/{orderId})
 */
package com.csu.orderservice.domain.dto;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class OrderDetailsResponse {
    // 订单基本信息
    private Long orderId;
    private Date orderDate;
    private String shippingAddress1;
    private String shippingAddress2;
    private String city;
    private String firstName;
    private String lastName;
    private String phone;
    private Double grandTotal;


    // 订单项列表
    private List<OrderItemResponse> orderItems;
}

