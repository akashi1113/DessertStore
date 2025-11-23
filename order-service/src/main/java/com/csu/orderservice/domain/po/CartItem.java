package com.csu.orderservice.domain.po;

import lombok.Data;

@Data
public class CartItem {
    private Long cartId;
    private String itemId;
    private Integer quantity;
    private Double totalPrice;
    private Double price;
}
