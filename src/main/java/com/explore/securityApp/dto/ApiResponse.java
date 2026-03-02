package com.explore.securityApp.dto;

import java.util.Date;

public class ApiResponse<T> {

    private Date timestamp;
    private Integer status;
    private String message;
    private T data;

    public ApiResponse(Date timestamp, Integer status, String message, T data) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<>(new Date(), 200, message, data);
    }

    public static <T> ApiResponse<T> error(Integer status,String message){
        return new ApiResponse<>(new Date(), status, message, null);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
