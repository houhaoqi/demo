package com.haoqi.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 公共字段自动填充
 *
 * @author haoqi
 * @Date 2022/7/22 - 11:37
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    //对数据库insert/update时会自动添加
    @Override
    public void insertFill(MetaObject metaObject) {
        System.out.println("自动添加公用字段insert...");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        //====调用BaseContext获取登录用户的id
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    //更新时自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        System.out.println("公共字段自动填充update...");
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
