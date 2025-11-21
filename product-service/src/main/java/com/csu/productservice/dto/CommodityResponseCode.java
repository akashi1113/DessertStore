package com.csu.productservice.dto;

public enum CommodityResponseCode {

    SUCCESS(200,"查询成功"),
    ERROR(201,"查询失败");

    private final int code;
    private final String description;

    CommodityResponseCode(int code,String description){
        this.code=code;
        this.description=description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
