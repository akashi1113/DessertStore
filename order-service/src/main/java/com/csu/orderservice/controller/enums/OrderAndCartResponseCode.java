package com.csu.orderservice.controller.enums;

import lombok.Getter;

@Getter
public enum OrderAndCartResponseCode {
        SUCCESS(0, "SUCCESS"),

                // 通用错误
                SERVER_ERROR(1,"服务器错误"),
                UNAUTHORIZED(2,"用户未登录或认证失败"),

                // 输入/验证错误 (状态码 3)
                INVALID_INPUT(3,"无效输入"),
                INVALID_ITEM_ID(3,"无效的商品 ID"),
                INVALID_QUANTITY_POSITIVE(3,"数量必须大于 0"),
                INVALID_QUANTITY_NEGATIVE(3,"数量不能为负"),
                INVALID_ADDRESS(3,"无效的地址信息"),
                PAYMENT_FAILED(3,"支付处理失败"),
                OUT_OF_STOCK(3,"商品库存不足"),

                // 资源/状态错误 (状态码 4)
                ITEM_NOT_IN_CART(4,"商品未在购物车中找到"),
                ORDER_NOT_FOUND(4,"订单未找到"),
                CART_NOT_FOUND(4,"购物车未找到");

        public int getStatusCode () {
            switch (this) {
                case SERVER_ERROR:
                    return 1;
                case UNAUTHORIZED:
                    return 2;
                case INVALID_INPUT:
                case INVALID_ITEM_ID:
                case INVALID_QUANTITY_POSITIVE:
                case INVALID_QUANTITY_NEGATIVE:
                case INVALID_ADDRESS:
                case PAYMENT_FAILED:
                case OUT_OF_STOCK:
                    return 3;
                case ITEM_NOT_IN_CART:
                case ORDER_NOT_FOUND:
                case CART_NOT_FOUND:
                    return 4;
                default:
                    return 0;
            }
        }
    private final int code;
    private final String message;

    OrderAndCartResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    }

