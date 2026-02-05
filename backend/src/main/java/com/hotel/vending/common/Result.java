package com.hotel.vending.common;

import lombok.Data;

/**
 * Unified API Response
 */
@Data
public class Result<T> {
    
    private Integer code;
    private String message;
    private T data;
    
    private static final Integer SUCCESS_CODE = 200;
    private static final Integer ERROR_CODE = 500;
    
    public static <T> Result<T> success() {
        return success(null);
    }
    
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(SUCCESS_CODE);
        result.setMessage("Success");
        result.setData(data);
        return result;
    }
    
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(ERROR_CODE);
        result.setMessage(message);
        return result;
    }
    
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
