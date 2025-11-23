package com.csu.orderservice.domain.dto;


import java.util.List;
import lombok.Data;

@Data
public class OrderPrepareResponse {

    private Long cartId;

    private Double totalQuantity;

    private Double subTotal;
    private List<OrderItemResponse> orderItems;
}


