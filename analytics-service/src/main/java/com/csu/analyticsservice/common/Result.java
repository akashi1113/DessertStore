package com.csu.analyticsservice.common;

import lombok.Data;

@Data
public class Result<T> {
    private String status;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setStatus("SUCCESS");
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setStatus("ERROR");
        result.setMessage(message);
        return result;
    }
}

