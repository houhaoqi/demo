package com.haoqi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haoqi.entity.Category;

/**
 * @author haoqi
 * @Date 2022/7/22 - 15:18
 */

public interface CategoryService extends IService<Category> {
    //要关联 Dish菜品，Setmeal套餐判断是否能删除
    //在CategoryService添加remove方法
    public void remove(Long id);
}
