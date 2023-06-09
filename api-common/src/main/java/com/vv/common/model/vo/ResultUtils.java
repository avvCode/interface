package com.vv.common.model.vo;

import com.vv.common.enums.ResponseCode;

/**
 * 返回工具类
 *
 * @author vv
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    public static <T> BaseResponse <T> success(ResponseCode responseCode, String message){
        return new BaseResponse<>(responseCode.getCode(),null,message);
    }

    /**
     * 失败
     *
     * @param responseCode
     * @return
     */
    public static BaseResponse error(ResponseCode responseCode) {
        return new BaseResponse<>(responseCode);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @return
     */
    public static BaseResponse error(int code, String message) {
        return new BaseResponse(code, null, message);
    }

    /**
     * 失败
     *
     * @param responseCode
     * @return
     */
    public static BaseResponse error(ResponseCode responseCode, String message) {
        return new BaseResponse(responseCode.getCode(), null, message);
    }
}
