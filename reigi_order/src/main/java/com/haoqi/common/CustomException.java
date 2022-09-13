package com.haoqi.common;

/**
 * 自定义异常类
 * 有projectObjectHandler接管
 *
 * @author haoqi
 * @Date 2022/7/22 - 16:50
 */
public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}
