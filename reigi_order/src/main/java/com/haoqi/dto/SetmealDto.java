package com.haoqi.dto;

import com.haoqi.entity.Setmeal;
import com.haoqi.entity.SetmealDish;
import lombok.Data;

import java.util.List;

/**
 * @author haoqi
 * @Date 2022/7/23 - 17:42
 */
@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;
    private String categoryName;

}
