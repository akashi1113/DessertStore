package com.csu.orderservice.domain.po;

import lombok.Data;

@Data
public class Item {
    private String itemId;

    private String name;

    private String productId;

    private double price;

    private String image;

    private String description;

    private int stock;


}
