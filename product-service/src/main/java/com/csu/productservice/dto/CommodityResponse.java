package com.csu.productservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.io.Serializable;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommodityResponse<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    private CommodityResponse(int status){
        this.status=status;
    }

    private CommodityResponse(int status,String msg){
        this.status=status;
        this.msg=msg;
    }

    private CommodityResponse(int status,String msg,T data){
        this.status=status;
        this.msg=msg;
        this.data=data;
    }

    private CommodityResponse(int status,T data){
        this.status=status;
        this.data=data;
    }

    @JsonIgnore
    public boolean isSuccess(){
        return this.status==CommodityResponseCode.SUCCESS.getCode();
    }

    public static <T> CommodityResponse <T> createForSuccess(){
        return new CommodityResponse<T>(CommodityResponseCode.SUCCESS.getCode());
    }

    public static <T> CommodityResponse <T> createForSuccess(T data){
        return new CommodityResponse<T>(CommodityResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> CommodityResponse <T> createForSuccessMessage(String msg){
        return new CommodityResponse<T>(CommodityResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> CommodityResponse <T> createForSuccess(T data,String msg){
        return new CommodityResponse<T>(CommodityResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> CommodityResponse <T> createForError(){
        return new CommodityResponse<T>(CommodityResponseCode.ERROR.getCode(),CommodityResponseCode.ERROR.getDescription());
    }

    public static <T> CommodityResponse <T> createForError(String msg){
        return new CommodityResponse<T>(CommodityResponseCode.ERROR.getCode(),msg);
    }

    public static <T> CommodityResponse <T> createForError(int code,String msg){
        return new CommodityResponse<T>(code,msg);
    }
}
