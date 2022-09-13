package com.haoqi.dto;

import com.haoqi.entity.Dish;
import com.haoqi.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO，全称Data Transfer Object，即数据传输对象，一般用于展示层与服务层之间的数据传输。
 * 涉及多表之间的数据处理
 * 前面没有使用到DTO是因为，前面的传输数据和实体类的属性都是一一对应的，现在传输的数据跟实体类里的属性并不是一一对应的，这时候就需要专门的DTO来传输。
 * 新建：
 * 这里创建一个DTO包建一个DishDTO类，这里只用到了List 下面两个后续开发使用
 *
 * @author haoqi
 * @Date 2022/7/23 - 10:44
 */
@Data
public class DishDto extends Dish {
    //在具有Dish类的属性之上添加口味DishFlavor类
    private List<DishFlavor> flavors = new ArrayList<>();
    private String categoryName;
    private Integer copies;
    //业务实现层，因为多表查询所以添加了事务管理
    //在dishServiceImpl添加自定义事务管理方法

}
