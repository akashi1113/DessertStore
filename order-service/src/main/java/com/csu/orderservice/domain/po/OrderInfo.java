package com.csu.orderservice.domain.po;

import java.util.Date;
import lombok.Data;

@Data
public class OrderInfo {
    private Long orderId;
    private Date orderDate;
    private Double grandTotal;
}
