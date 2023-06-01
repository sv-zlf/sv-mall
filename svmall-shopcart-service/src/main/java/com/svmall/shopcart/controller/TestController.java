package com.svmall.shopcart.controller;

import com.svmall.common.exception.ErrorException;
import com.svmall.common.vo.ResultCode;
import com.svmall.common.vo.ResultVo;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zlf
 * @data 2023/5/24
 * @description
 */
@Api(tags = "绿灯测试")
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "hello,world!";
    }

    @GetMapping("/yyy")
    public String yyy(){
        return "sss";
    }

    @GetMapping("/error/hello")
    public ResultVo error() {
        throw new ErrorException(ResultCode.FAILED,"异常测试");
        //return new ResultVo(ResultCode.FAILED,"抛出异常");
    }
}
