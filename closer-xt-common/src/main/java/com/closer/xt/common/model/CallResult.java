package com.closer.xt.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallResult<T> implements Serializable {
    private int code;
    private String msg;
    private T result;

    public static <T>CallResult<T> success() {
        return new CallResult<T>(BusinessCodeEnum.DEFAULT_SUCCESS.getCode(), BusinessCodeEnum.DEFAULT_SUCCESS.getMsg(), null);
    }

    public static <T>CallResult<T> success(T result) {
        return new CallResult<T>(BusinessCodeEnum.DEFAULT_SUCCESS.getCode(), BusinessCodeEnum.DEFAULT_SUCCESS.getMsg(), result);
    }

    public static <T>CallResult<T> success(int code, String msg, T result) {
        return new CallResult<>(code, msg, result);
    }

    public static <T>CallResult<T> fail() {
        return new CallResult<>(BusinessCodeEnum.DEFAULT_SYS_ERROR.getCode(), BusinessCodeEnum.DEFAULT_SYS_ERROR.getMsg(), null);
    }

    public static <T>CallResult<T> fail(T result) {
        return new CallResult<>(BusinessCodeEnum.DEFAULT_SYS_ERROR.getCode(), BusinessCodeEnum.DEFAULT_SYS_ERROR.getMsg(), result);
    }

    public static <T>CallResult<T> fail(int code, String msg) {
        return new CallResult<>(code, msg, null);
    }

    public static <T>CallResult<T> fail(int code, String msg, T result) {
        return new CallResult<>(code, msg, result);
    }

    public boolean isSuccess() {
        return this.code == BusinessCodeEnum.DEFAULT_SUCCESS.getCode();
    }
}
