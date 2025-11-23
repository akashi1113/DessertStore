package com.csu.orderservice.domain.dto;

import java.util.Date;
import lombok.Data;

@Data
public class OrderSummaryResponse {

    private Long orderId;

    private Date orderDate;

    private Double grandTotal;
}

