package com.vvic.jst.controller;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenguiquan
 * @description 结果封装
 * @date 2020-09-23 17:27
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -6436382930973741069L;

    public static final Integer BAD_REQUEST = 400;
    public static final Integer UNAUTHORIZED = 401;
    public static final Integer SERVER_ERROR = 500;


    /**
     * 响应编码
     */
    private int code = 200;

    /**
     * 响应消息
     */
    private String message = "操作成功";

    /**
     * 响应数据
     */
    private T data;

    public BaseResponse() {
    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public boolean isSuccess() {
        return this.code == 200;
    }

    public BaseResponse(T data) {
        this.data = data;
    }

    public BaseResponse error(int code, String message) {
        return new BaseResponse(code, message);
    }

    public static <T> BaseResponse<T> buildError(int code, String message) {
        return new BaseResponse<>(code, message);
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(data);
    }


    public static <T> BaseResponse<T> ok() {
        return new BaseResponse<>();
    }
}
