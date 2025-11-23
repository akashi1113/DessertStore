package com.csu.orderservice.domain.dto;

import com.csu.orderservice.domain.po.LineItem;
import com.csu.orderservice.domain.po.Order;

import java.util.List;

public class InsertOrderRequest {
    private Order order;
    private List<LineItem> lineItems;

    // Getters and Setters
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
}
