package com.svmall.common.exception;


import com.svmall.common.vo.StatusCode;
import lombok.Getter;

/**
 * @author zlf
 * @data 2023/2/6
 * @apiNote 异常处理
 */
@Getter
public class ErrorException extends RuntimeException {
    private int code;
    private String msg;

    // 手动设置异常
    public ErrorException(StatusCode statusCode, String message) {
        // message用于用户设置抛出错误详情，例如：当前价格-5，小于0
        super(message);
        // 状态码
        this.code = statusCode.getCode();
        // 状态码配套的msg
        this.msg = statusCode.getMsg();
    }

    // 自定义异常信息
    public ErrorException(int code, String msg, String message) {
        super(message);
        this.code = code;
        this.msg=msg;

    }

}