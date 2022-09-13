package com.haoqi.common;

import com.sun.org.apache.bcel.internal.generic.RET;

/**
 * 基于ThreadLocal封装的工具类，用于保存和获取当前登录用户的id
 * 在过滤器LoginCheckFilter.java中调用此方法，获取用户id
 * @author haoqi
 * @Date 2022/7/22 - 12:21
 */

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    //利用ThreadLocal存放employee的id
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
