package com.svmall.gatewayadmin.vo;

import lombok.Getter;

/**
 * @author zlf
 * @data 2023/2/6
 * @apiNote
 */
@Getter
public enum ResultCode implements StatusCode{
    SUCCESS(200, "请求成功"),
    FAILED(400, "请求失败"),
    TOKEN_INVALID(500, "token校验失败"),
    VALIDATE_ERROR(1001, "参数校验失败"),
    RESPONSE_PACK_ERROR(1002, "response返回包装失败");

    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}