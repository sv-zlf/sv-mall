package com.svmall.common.advice;

import com.svmall.common.exception.ErrorException;
import com.svmall.common.vo.ResultCode;
import com.svmall.common.vo.ResultVo;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zlf
 * @data 2023/6/1
 * @description
 */
@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler({BindException.class})
    public ResultVo MethodArgumentNotValidExceptionHandler(BindException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return new ResultVo(ResultCode.VALIDATE_ERROR, objectError.getDefaultMessage());
    }

    @ExceptionHandler({ErrorException.class})
    public ResultVo MethodArgumentNotValidExceptionHandler(ErrorException e) {
        // 从异常对象中拿到ObjectError对象
        return new ResultVo(e.getCode(),e.getMessage(),null);
    }
}
