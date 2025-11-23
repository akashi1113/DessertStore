package com.csu.orderservice.domain.dto;

import lombok.Data;

@Data
public class CartItemResponse {
    private String itemId;
    private Integer quantity;
    private Double totalPrice;
}
