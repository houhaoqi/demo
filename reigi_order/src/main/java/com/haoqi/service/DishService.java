package com.haoqi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haoqi.dto.DishDto;
import com.haoqi.entity.Dish;

import java.util.List;

/**
 * @author haoqi
 * @Date 2022/7/22 - 16:39
 */
public interface DishService extends IService<Dish> {
    //新增菜品时带有菜品口味信息的方法
    public void saveWithFlavor(DishDto dishDto);
    //根据id修改菜品带有菜品口味信息方法
    public DishDto getByIdWithFlavor(Long id);
    //根据菜品信息，更新对应口味信息
    public void updateWithFlavor(DishDto dishDto);
    //根据id删除菜品带有菜品口味删除
    public void removeWithFlavor(List<Long> ids);
}
