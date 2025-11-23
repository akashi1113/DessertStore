package com.csu.orderservice.domain.po;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
 public class Order implements Serializable {
        public String getMonthGroup() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            return sdf.format(this.orderDate);
        }

        private static final long serialVersionUID = 6321792448424424931L;

        private Long orderId;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date orderDate;
        private Long userId;
        private String shippingAddress1;
        private Double shippingCost;
        private Double tax;
        private Double grandTotal;
        private String shippingAddress2;
        private String firstName;
        private String lastName;
        private String phone;
        private String city;
        private Double subTotal;
       private List<LineItem> lineItems;
}
