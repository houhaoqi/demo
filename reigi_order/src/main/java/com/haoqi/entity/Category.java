package com.haoqi.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分类表
 * @author haoqi
 * @Date 2022/7/22 - 15:15
 */
@Data
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Integer  type;   //类型 1 菜品分类 2 套餐分类
    private String name;    //分类名称
    private Integer  sort;   //顺序

    //自动填充公共属性@TableField
    //创建时间
    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时填充字段
    private LocalDateTime updateTime;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


}
