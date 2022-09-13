package com.haoqi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haoqi.common.R;
import com.haoqi.entity.Category;
import com.haoqi.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author haoqi
 * @Date 2022/7/22 - 15:20
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    //新增 菜品/套餐 分类公用一个方法即可处理
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category{}",category);
        categoryService.save(category);
        return R.success("新增成功！");
    }
    //分页查询 --分类管理页面
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        //1.构造分页器
        Page<Category> pageInfo = new Page(page,pageSize);
        //2.构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //3.根据表中字段sort进行顺序排序
        queryWrapper.orderByAsc(Category::getSort);
        //4.执行sql查询数据库
        categoryService.page(pageInfo,queryWrapper);
        //5.返回页面信息
        return R.success(pageInfo);
    }
    //根据id删除分类 菜品/套餐 判断是否有关联数据，否则无法删除
    @DeleteMapping
    public R<String> delete(Long ids){
        //categoryService.removeById(ids);
        //在service
        // 层自定义删除方法
        categoryService.removeById(ids);
        return R.success("删除成功");
    }
    //根据id修改分类
    @PutMapping
    public R<String> update(@RequestBody Category category){
        System.out.println(category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功！");
    }
    //根据条件查询list数据 --新增菜品分类
    // (菜品管理的新增页面会调用 分类管理->分类数据，前台会在category页面这里请求数据)
    @GetMapping("/list")
    public R<List<Category>> List(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //添加排序条件 先按sort排序若相同则按创建时间排序
        queryWrapper.orderByDesc(Category::getUpdateTime).orderByAsc(Category::getSort);
        //植入list
        List<Category> list = categoryService.list(queryWrapper);
        System.out.println(list);
        return R.success(list);
    }

}
