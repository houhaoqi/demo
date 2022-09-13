package com.haoqi.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
/**
 * c.1此类是一个通用结果类，服务端响应的所有结果最终都会包装成此种类型返回给前端页面
 *
 * @author haoqi
 * @Date 2022/7/21 - 10:56
 */

@Data
public class R<T> {
    //通用返回结果集，后台响应给前台数据封装成json对象
    private Integer code; //状态码 0false，1true
    private String msg; //响应错误状态信息
    private T data; //响应成功的数据
    private Map map = new HashMap(); //动态数据

    //响应成功数据集方法
    public static <T>R<T> success(T object){
        R<T> r = new R<T>();
        r.code = 1;
        r.data = object;
        return r;
    }
    //响应失败数据集方法
    public static <T>R<T> error(String msg){
        R<T> r = new R<T>();
        r.code = 0;
        r.msg = msg;
        return r;
    }
    //响应动态数据方法
    public R<T> add(String key,Object value){
        this.map.put(key,value);
        return this;
    }

}
