package com.haoqi.common.exception;

import com.haoqi.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * @author haoqi
 * @Date 2022/9/8 - 11:54
 *
 * 捕获全局运行时异常--输出日志
 */
@Slf4j
@RestControllerAdvice //@RestControllerAdvice都是对Controller进行增强的，可以全局捕获spring mvc抛的异常
public class GlobalExceptionHandler {
    /**
     * 捕捉shiro校验异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED) //因为前后端分离 返回一个状态 一般是401 没有权限
    @ExceptionHandler(value =  ShiroException.class)//捕获运行时异常ShiroException是大部分异常的父类
    public Result handler(ShiroException e) {
        log.error("没有权限：-----------------{}",e);
        return Result.other(401,e.getMessage());
    }

    /**
     * 捕捉运行时异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //因为前后端分离 返回一个状态
    @ExceptionHandler(value =  RuntimeException.class)//捕获运行时异常
    public Result handler(RuntimeException e)  {
        log.error("运行时异常：-----------------{}",e);
        return Result.error(e.getMessage());
    }

    /**
     * 捕捉实体校验异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //因为前后端分离 返回一个状态
    @ExceptionHandler(value = MethodArgumentNotValidException.class)//捕获运行时异常
    public Result handler(MethodArgumentNotValidException e) {
        log.error("实体捕获异常：-----------------{}",e);
        BindingResult bindingException = e.getBindingResult();
        //多个异常顺序抛出
        ObjectError objectError = bindingException.getAllErrors().stream().findFirst().get();
        return Result.error(objectError.getDefaultMessage());
    }

    /**
     * 捕获拦截异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) {
        log.error("Assert异常:-------------->{}",e.getMessage());
        return Result.error(e.getMessage());
    }


}
