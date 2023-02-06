package com.svmall.common.vo;

import lombok.Getter;

/**
 * @author zlf
 * @data 2023/2/6
 * @apiNote
 */
@Getter
public enum  AppCode implements StatusCode {

    APP_ERROR(2000, "业务异常"),
    PRICE_ERROR(2001, "价格异常");

    private int code;
    private String msg;

    AppCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}