package com.haoqi.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.sql.SQLIntegrityConstraintViolationException;

/**
     全局异常处理
     如果类上加有 @RestController、@Controller注解(annotations的属性值)的类中有方法抛出异常，
     由GlobalExceptionHander来处理异常
 * @author haoqi
 * @Date 2022/7/21 - 19:48
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //截取异常
    //添加/修改 数据时存在的异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e){
        log.error(e.getMessage()); //报错记得打日志
        //员工注册是否异常
        if (e.getMessage().contains("Duplicate entry")){
            String[] split = e.getMessage().split(" ");
            String msg = split[2]+"已存在!";
            return R.error(msg);
        }

        return R.error("未知错误！");
    }

    //自定义异常处理 runtimeException
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException e){
        log.error(e.getMessage());
        return R.error(e.getMessage());
    }

}
