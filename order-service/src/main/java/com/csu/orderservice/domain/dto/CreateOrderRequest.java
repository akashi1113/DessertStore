package com.csu.orderservice.domain.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CreateOrderRequest {
    @NotNull(message = "收货人名字不能为空")
    private String firstName;

    @NotNull(message = "收货人姓氏不能为空")
    private String lastName;

    @NotNull(message = "地址1不能为空")
    private String shippingAddress1;

    private String shippingAddress2;

    @NotNull(message = "城市不能为空")
    private String city;

    @NotNull(message = "电话不能为空")
    private String phone;

//    @NotNull(message = "订单金额不能为空")
//    private Double subTotal;

//    @NotNull(message = "配送费用不能为空")
//    private String shippingCost;
//
//    @NotNull(message = "税费不能为空")
//    private String tax;

    @NotNull(message = "总金额不能为空")
    private Double grandTotal;

    private Long cartId;

    @NotNull(message = "订单项不能为空")
    @Size(min = 1, message = "至少需要一个订单项")
    private List<CreateOrderItem> orderItems;

    @Data
    public static class CreateOrderItem {
        @NotNull(message = "商品ID不能为空")
        private String itemId;

        @NotNull(message = "商品数量不能为空")
        private Integer quantity;

        @NotNull(message = "订单项总价不能为空")
        private  Double totalPrice;
    }
}

