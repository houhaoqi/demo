package com.haoqi.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author haoqi
 * @Date 2022/3/5 - 21:01
 */

@Data  // get/set(), toString(), hashcode(), equals() 等，，， 没有构造方法
@TableName("testdb") //填写数据库表名，用于mybatisplus查找
public class Book {
    private Integer id;
    private String type;
    private String name;
    private String description;
}
