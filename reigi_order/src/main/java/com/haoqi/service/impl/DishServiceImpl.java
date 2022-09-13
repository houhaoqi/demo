package com.haoqi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haoqi.common.CustomException;
import com.haoqi.dto.DishDto;
import com.haoqi.entity.Dish;
import com.haoqi.entity.DishFlavor;
import com.haoqi.mapper.DishMapper;
import com.haoqi.service.DishService;
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
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorServiceImpl dishFlavorService;

    //新增菜品同时保存对应的口味数据
    @Override
    @Transactional //声明式事务管理 -在启动类上开启事务功能注解@EnableTransactionManagement
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品基本信息到菜品表dish
        this.save(dishDto);
        Long dishId = dishDto.getId(); //菜品id
        //植入flavor菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        //获取对应id的口味
        flavors = flavors.stream().map((item) ->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    //修改的信息回显前台 没有操作数据库
    //页面发送ajax请求，请求服务端，根据id查询当前菜品信息，用于菜品信息回显
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //从dish表查询菜品基本信息
        Dish dish = this.getById(id);
        //将查到的dish信息复制给dishDto
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        //查询当前套餐对应的菜品信息，从dish_flavor表中查询
        //根据菜品dish的id查关联的口味flavor对应信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        //将口味信息存放集合flavors
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        //将集合的信息并入dishDto对象中
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    //保存到数据库
    //点击保存按钮，页面发送ajax请求，将修改后的套餐相关数据以JSON的形式提交到服务端
    @Override
    @Transactional //声明式事务管理
    public void updateWithFlavor(DishDto dishDto) {
        //根据传入对象的id更新dish表当前信息
        this.updateById(dishDto);
        //清理当前菜品对应口味数据dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        //根据dto的id查对应的菜品信息
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper); //delete

        //添加修改后提交的口味数据dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存
        dishFlavorService.saveBatch(flavors);
    }

    //根据菜品ids，删除对应口味信息
    @Override
    @Transactional
    public void removeWithFlavor(List<Long> ids) {
        //查询菜品售卖状态，是否能删除
        LambdaQueryWrapper<Dish> dishQueryWrapper  = new LambdaQueryWrapper<>();
        dishQueryWrapper.in(Dish::getId,ids);
        dishQueryWrapper.eq(Dish::getStatus,1);
        int count = this.count(dishQueryWrapper);
        if (count > 0){
            //在售状态，不能删除
            throw new CustomException("菜品正在售卖，不能删除！");
        }
        //可以删除，删除菜品表dish数据
        this.removeByIds(ids);
        //再删除flavor表里数据
        LambdaQueryWrapper<DishFlavor> flavorQueryWrapper = new LambdaQueryWrapper<>();
        flavorQueryWrapper.in(DishFlavor::getDishId,ids);
        //delete
        dishFlavorService.remove(flavorQueryWrapper);

    }
}
