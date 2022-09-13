package com.haoqi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haoqi.common.R;
import com.haoqi.dto.DishDto;
import com.haoqi.entity.Category;
import com.haoqi.entity.Dish;
import com.haoqi.entity.DishFlavor;
import com.haoqi.service.impl.CategoryServiceImpl;
import com.haoqi.service.impl.DishFlavorServiceImpl;
import com.haoqi.service.impl.DishServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haoqi
 * @Date 2022/7/23 - 9:30
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private DishServiceImpl dishService;
    @Autowired
    private DishFlavorServiceImpl dishFlavorService;

    //接受前台新增菜品数据
    //开发新增菜品功能，就是在服务端编写代码去处理前端页面发送的四次请求即可。
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        //将dish，dish_flavors数据用自定义事务管理方法存放
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功!");
    }
    /**
     * 菜品分页查询时前端页面和服务端的交互过程：
     * 1、页面（backend/page/food/list.html）发送ajax请求，将分页查询参数（page、pageSize、name）提交到服务端，获取分页数据
     * 2、页面发送请求，请求服务端进行图片下载，用于页面图片展示
     * 开发菜品信息分页查询功能，就是在服务端，编写代码去处理前端页面发送的这2次请求即可。
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //构造分页器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //添加过滤条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        //添加查询条件,根据name进行like模糊查询
        queryWrapper.like(name!=null,Dish::getName,name);
        //添加排序条件，根据更新时间排序
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo,queryWrapper);
        //拷贝对象
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");// （数据源，拷贝的数据，不拷贝数据的属性）

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            //拷贝对象
            BeanUtils.copyProperties(item,dishDto);
            //获取分类栏目id-》根据id查询name字段
            Long categoryId = item.getCategoryId();
            //获取该id对象
            Category category = categoryService.getById(categoryId);
            //如果该栏目有分类归属不为空
            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());//收集
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }
    /**
     * 修改菜品时交互过程
     * 1.页面发送ajax请求，请求服务端获取分类数据，用于菜品分类下拉框中数据展示
     *     ----在categoryController().List() 方法新增菜品分类已完成
     * 2.页面发送请求，请求服务端进行图片下载，用于页图片回显
     *     ----CommonController().upload()/.download()方法文件上传下载已完成
     *
     * 3.页面发送ajax请求，请求服务端，根据id查询当前菜品信息，用于菜品信息回显  get_select
     * 4.点击保存按钮，页面发送ajax请求，将修改后的菜品相关数据以json形式提交到服务端 put_update
     */
    //3.后台根据前台的id返回相应信息
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable("id") Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    //4.后台保存前台提交修改好的信息
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }
//
//        /**
//     * 根据条件查询对应的菜品数据
//     * @param dish
//     * @return
//     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        //构造查询条件
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
//        //添加条件，查询状态为1（起售状态）的菜品
//        queryWrapper.eq(Dish::getStatus,1);
//        //添加排序条件
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//    }

    //=============================
    //完成套餐添加页面的列表
    /**
     *根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());

        //添加条件，查询条件为1(起售)
        queryWrapper.eq(Dish:: getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item)->{
            DishDto dishDto =new DishDto();

            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();//分类Id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category!=null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            //当前菜品Id
            Long dishId = item.getId();

            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
    //=============================


    //修改套餐起售/停售状态
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status, @RequestParam List<Long> ids){
        Dish dish = new Dish();
        for (Long dishId : ids) {
            dish.setId(dishId);
            dish.setStatus(status);
            dishService.updateById(dish);
            return R.success("菜品状态修改成功！");
        }
        return R.error("菜品状态修异常！");
    }

    //删除菜品
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids{}",ids);
        dishService.removeWithFlavor(ids);

        return R.success("菜品删除成功！");
    }


}
