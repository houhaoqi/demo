package com.haoqi.common.lang;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.Data;
import org.mybatis.spring.annotation.MapperScan;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haoqi
 * @Date 2022/9/7 - 20:47
 *
 * 封装结果集返回给前台
 */
@Data
public class Result<T> implements Serializable { //序列化操作
    // 通用响应参数 状态码，状态信息，状态数据
    private int code;  //状态码 成功：200，失败：400
    private String msg; //响应状态信息
    private T data; //响应数据

    // 成功响应的方法
    public static <T>Result<T> success(T object){
        Result<T> r = new Result<T>();
        r.code = 200;
        r.msg = "successful";
        r.data = object;
        return r;
    }

    // 失败响应的方法
    public static <T>Result<T> error(String msg){
        Result<T> r = new Result<T>();
        r.code = 400;
        r.msg = msg;
        return r;
    }

    // 其他异常响应数据的方法
    public static <T>Result<T> other(int code, String msg){
        Result<T> r = new Result<>();
        r.code = code;
        r.msg = msg;
        return r;
    }
}
