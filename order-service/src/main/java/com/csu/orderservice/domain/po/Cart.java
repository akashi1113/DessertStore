package com.csu.orderservice.domain.po;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Cart {
    private Long cartId;
    private Long userId;
    private double subTotal;
    private double totalQuantity;
    private List<CartItem> items = new ArrayList<>();
}
