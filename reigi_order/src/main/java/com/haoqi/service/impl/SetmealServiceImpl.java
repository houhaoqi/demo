package com.haoqi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haoqi.common.CustomException;
import com.haoqi.dto.SetmealDto;
import com.haoqi.entity.Setmeal;
import com.haoqi.entity.SetmealDish;
import com.haoqi.mapper.SetmealMapper;
import com.haoqi.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haoqi
 * @Date 2022/7/22 - 16:41
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishServiceImpl setmealDishService;

    //新增套餐，同时需要保存套餐和菜品的关系
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐基本信息，操作setmeal表执行inert操作
        this.save(setmealDto);
        //套餐id
        Long setmealId = setmealDto.getId();
        //保存setmel和dish联系，操作setmeal_dish，insert操作
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes.stream().map((item)->{
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        //保存
        setmealDishService.saveBatch(dishes);
    }

    /**
     * 删除套餐，同时需要删除套餐和菜品关联的数据
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in (1,2,3) and status = 1/0
        //查询套餐售卖状态，是否能删除
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper();
        setmealQueryWrapper.in(Setmeal::getId,ids);
        setmealQueryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(setmealQueryWrapper);
        if (count > 0){
            //>0不能删除，抛出业务异常
            throw new CustomException("套餐正在售卖，不能删除");
        }
        //可以删除，先删除套餐表中的数据--setmeal
        this.removeByIds(ids);
        //在删除关系表setmeal_dish
        //delete from setmeal_dish where setmeal.id in (1,2,...);
        LambdaQueryWrapper<SetmealDish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.in(SetmealDish::getSetmealId,ids);
        //删除
        setmealDishService.remove(dishQueryWrapper);

    }

    //修改的信息回显前台 没有操作数据库
    //页面发送ajax请求，请求服务端，根据id查询当前菜品信息，用于套餐信息回显
    @Override
    @Transactional
    public SetmealDto getByIdWithDish(Long id) {
        //从setmeal表里根据id获取数据
        Setmeal setmeal = this.getById(id);
        //将查到的setmeal信息复制给setmealDto对象
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        //查询当前套餐对应的菜品信息，从setmeal_dish表中查询
        //根据套餐setmeal的id查关联的菜品dish对应信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        //将菜品信息收集
        List<SetmealDish> dishes = setmealDishService.list(queryWrapper);
        //将信息塞入setmealDto对象
        setmealDto.setSetmealDishes(dishes);

        return setmealDto;
    }

    //保存到数据库
    //点击保存按钮，页面发送ajax请求，将修改后的套餐相关数据以JSON的形式提交到服务端
    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        //根据传入对象的id更新dish表当前信息
        this.updateById(setmealDto);
        //清理当前菜品对应口味数据dish_flavor表的delete操作
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        //根据dto的id查对应的菜品信息
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        //添加修改后的套餐数据setmeal_dish表的insert操作
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存
        setmealDishService.saveBatch(dishes);
    }
}
