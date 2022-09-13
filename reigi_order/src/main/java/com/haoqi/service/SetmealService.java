package com.haoqi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haoqi.dto.SetmealDto;
import com.haoqi.entity.Setmeal;

import java.util.List;

/**
 * @author haoqi
 * @Date 2022/7/22 - 16:40
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时需要删除和菜品关联的数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    /**
     * 根据id查询套餐，同时需要查询和菜品关联的数据
     * @param id
     * @return
     */
    public SetmealDto getByIdWithDish(Long id);

    /**
     * 修改套餐，同时需要修改和菜品关联的数据
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto);
}
