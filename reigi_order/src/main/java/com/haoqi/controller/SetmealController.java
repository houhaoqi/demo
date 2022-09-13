package com.haoqi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haoqi.common.R;
import com.haoqi.dto.SetmealDto;
import com.haoqi.entity.Category;
import com.haoqi.entity.Dish;
import com.haoqi.entity.Setmeal;
import com.haoqi.entity.SetmealDish;
import com.haoqi.service.CategoryService;
import com.haoqi.service.impl.CategoryServiceImpl;
import com.haoqi.service.impl.DishServiceImpl;
import com.haoqi.service.impl.SetmealDishServiceImpl;
import com.haoqi.service.impl.SetmealServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haoqi
 * @Date 2022/7/23 - 17:46
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private SetmealServiceImpl setmealService;
    @Autowired
    private DishServiceImpl dishService;
    @Autowired
    private SetmealDishServiceImpl setmealDishService;

    /**
     * 在开发代码之前，需要梳理一下新增套餐时前端页面和服务端的交互过程：
     *
     * 页面(backend/page/combo/add.html)发送ajax请求，请求服务器获取套餐分类数据并展示到下拉框中
     * 页面发送ajax请求，请求服务端获取菜品分类数据并展示到添加菜品窗口中
     * 页面发送ajax请求，请求服务端，根据菜品分类查询对应的菜品数据并展示到添加菜品窗口中
     * 页面发送请求进行图片上传，请求服务端将图片保存到服务器
     * 页面发送请求进行图片下载，将上传的图片进行回显
     * 点击保存按钮，发送ajax请求，将套餐相关数据以json形式提交到服务端
     * 开发新增套餐功能，其实就是在服务端编写代码去处理前端页面发送的这6次请求即可。
     *
     * 编写控制器
     */
    //开发新增套餐功能，就是在服务端编写代码去处理前端页面发送的这6次请求即可。
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }
    /**
     * 分页显示套餐信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构建分页构造器对象
        Page<Setmeal> pageInfo= new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        //添加查询条件,根据name进行like模糊查询
        queryWrapper.like(name!=null,Setmeal::getName,name);
        //添加排序条件，根据更新时间来降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list =records.stream().map((item)-> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }
    //修改套餐起售/停售状态
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status,@RequestParam List<Long> ids){
        Setmeal setmeal = new Setmeal();
        for (Long dishId : ids) {
            //根据id修改状态status
            setmeal.setId(dishId);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
            return R.success("套餐状态修改成功！");
        }
        return R.error("套餐状态修异常！");
    }
    //删除套餐
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids{}",ids);
        setmealService.removeWithDish(ids);
        return R.success("套餐删除成功");
    }

    //通过id查询套餐
    @GetMapping("{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        log.info("id:{}",id);
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }
    //修改套餐（先清理，再重新插入）
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.updateWithDish(setmealDto);
        return R.success("修改套餐成功！");
    }

    // 前端发送的请求：http://localhost:8080/setmeal/list?categoryId=1516353794261180417&status=1
    // 注意: 请求后的参数 是以key-value键值对的方式 传入，而非JSON格式，不需要使用@RequestBody 来标注，
    //   只需要用包含 参数(key)的实体对象接收即可
    @GetMapping("/list")  // 在消费者端 展示套餐信息
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId+'_' +#setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        Long categoryId = setmeal.getCategoryId();
        Integer status = setmeal.getStatus();
        queryWrapper.eq(categoryId != null,Setmeal::getCategoryId,categoryId);
        queryWrapper.eq(status != null,Setmeal::getStatus,status);

        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> setmeals = setmealService.list(queryWrapper);

        return R.success(setmeals);
    }

    /**
     * 用户查看套餐详情
     * @param SetmealId
     * @return
     */
    @GetMapping("/dish/{id}")
    //使用路径来传值的
    public R<List<Dish>> dish(@PathVariable("id") Long SetmealId) {

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, SetmealId);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        LambdaQueryWrapper<Dish> queryWrapper2 = new LambdaQueryWrapper<>();
        ArrayList<Long> dishIdList = new ArrayList<>();
        for (SetmealDish setmealDish : list) {
            Long dishId = setmealDish.getDishId();
            dishIdList.add(dishId);
        }
        queryWrapper2.in(Dish::getId, dishIdList);
        List<Dish> dishList = dishService.list(queryWrapper2);

        return R.success(dishList);
    }

}
