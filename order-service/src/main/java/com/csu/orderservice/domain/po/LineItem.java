package com.csu.orderservice.domain.po;

import java.io.Serializable;
import lombok.Data;

@Data
 public class LineItem implements Serializable {
       private Long orderId;
       private Long cartId;
       private int quantity;
       private String itemId;
       private double totalPrice;

}
