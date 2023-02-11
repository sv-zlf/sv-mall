package com.svmall.gatewayadmin.exception;

import com.svmall.gatewayadmin.vo.AppCode;
import com.svmall.gatewayadmin.vo.StatusCode;
import lombok.Getter;

/**
 * @author zlf
 * @data 2023/2/6
 * @apiNote
 */
@Getter
public class APIException extends RuntimeException {
    private int code;
    private String msg;

    // 手动设置异常
    public APIException(StatusCode statusCode, String message) {
        // message用于用户设置抛出错误详情，例如：当前价格-5，小于0
        super(message);
        // 状态码
        this.code = statusCode.getCode();
        // 状态码配套的msg
        this.msg = statusCode.getMsg();
    }

    // 默认异常使用APP_ERROR状态码
    public APIException(String message) {
        super(message);
        this.code = AppCode.APP_ERROR.getCode();
        this.msg = AppCode.APP_ERROR.getMsg();
    }

}