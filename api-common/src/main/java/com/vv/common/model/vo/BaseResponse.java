package com.vv.common.model.vo;


import lombok.Data;

@Data
public class BaseResponse<T> {
    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ResponseCode responseCode) {
        this(responseCode.getCode(), null, responseCode.getMessage());
    }
}
