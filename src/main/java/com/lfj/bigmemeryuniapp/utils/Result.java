package com.lfj.bigmemeryuniapp.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    /** 业务状态码 */
    private int code;

    /** 提示信息 */
    private String message;

    /** 返回数据 */
    private T data;

    /** 成功返回 */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /** 成功（无数据） */
    public static <T> Result<T> success() {
        return success(null);
    }

    /** 失败返回 */
    public static <T> Result<T> fail(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    /** 失败（默认 500） */
    public static <T> Result<T> fail(String message) {
        return fail(500, message);
    }
}
