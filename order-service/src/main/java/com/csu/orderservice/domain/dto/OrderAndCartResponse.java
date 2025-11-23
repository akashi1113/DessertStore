package com.csu.orderservice.domain.dto;


import com.csu.orderservice.controller.enums.OrderAndCartResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderAndCartResponse<T> {
    private final int status;
    private String message;
    private T data;

    private OrderAndCartResponse(int status, String message){
        this.status = status;
        this.message = message;
    }

    private OrderAndCartResponse(int status, String message, T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private OrderAndCartResponse(int status, T data){
        this.status = status;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess(){
        return this.status == OrderAndCartResponseCode.SUCCESS.getCode();
    }
    public static <T> OrderAndCartResponse<T> createForSuccess(){
        return new OrderAndCartResponse<>(OrderAndCartResponseCode.SUCCESS.getCode(), OrderAndCartResponseCode.SUCCESS.getMessage());
    }

    public static <T> OrderAndCartResponse<T> createForSuccess(String message){
        return new OrderAndCartResponse<>(OrderAndCartResponseCode.SUCCESS.getCode(), message);
    }
    public static <T> OrderAndCartResponse<T> createForSuccess(T data){
        return new OrderAndCartResponse<>(OrderAndCartResponseCode.SUCCESS.getCode(), OrderAndCartResponseCode.SUCCESS.getMessage(),data);
    }

    public static <T> OrderAndCartResponse<T> createForSuccess(String message, T data) {
        return new OrderAndCartResponse<>(OrderAndCartResponseCode.SUCCESS.getCode(), message, data);
    }


    public static <T> OrderAndCartResponse<T> error(int status, String message) {
        return new OrderAndCartResponse<>(status, message);
    }

    public static <T> OrderAndCartResponse<T> serverError() {
        return new OrderAndCartResponse<>(OrderAndCartResponseCode.SERVER_ERROR.getCode(), OrderAndCartResponseCode.SERVER_ERROR.getMessage());
    }

    public static <T> OrderAndCartResponse<T> unauthorized() {
        return new OrderAndCartResponse<>(OrderAndCartResponseCode.UNAUTHORIZED.getCode(), OrderAndCartResponseCode.UNAUTHORIZED.getMessage());
    }

    public static <T> OrderAndCartResponse<T> badRequest(String message, String errorCode) {
        return new OrderAndCartResponse<>(3, message); // 状态码 3 用于错误输入/验证
    }

    public static <T> OrderAndCartResponse<T> notFound(String message, String errorCode) {
        return new OrderAndCartResponse<>(4, message); // 状态码 4 用于资源未找到
    }

    public static <T> OrderAndCartResponse<T> badRequest(OrderAndCartResponseCode code, String specificMessage) {
        return new OrderAndCartResponse<>(code.getCode(), specificMessage != null ? specificMessage : code.getMessage());
    }

    public static <T> OrderAndCartResponse<T> badRequest(OrderAndCartResponseCode code) {
        return badRequest(code, null);
    }

    public static <T> OrderAndCartResponse<T> notFound(OrderAndCartResponseCode code) {
        return new OrderAndCartResponse<>(code.getCode(), code.getMessage());
    }
    // Overload allowing specific message
    public static <T> OrderAndCartResponse<T> notFound(OrderAndCartResponseCode code, String specificMessage) {
        return new OrderAndCartResponse<>(code.getCode(), specificMessage != null ? specificMessage : code.getMessage());
    }
}
