package com.csu.orderservice.domain.dto;

import java.util.List;
import lombok.Data;

@Data
public class CartResponse {
    private Long cartId;
    private Double totalQuantity;
    private Double subTotal;
    private List<CartItemResponse> items;
}
