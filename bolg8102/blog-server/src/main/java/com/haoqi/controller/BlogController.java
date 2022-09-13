package com.haoqi.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haoqi.common.lang.Result;
import com.haoqi.entity.Blog;
import com.haoqi.service.BlogService;
import com.haoqi.util.ShiroUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author anonymous
 * @since 2022-09-07
 */
@Slf4j
@RestController
public class BlogController {

    @Autowired
    BlogService blogService;

    /**
     * 分页-Page处理
     * @param currentPage 页码
     * @return
     */
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage){
        log.info("交互======> BlogController:/blogs...");
        Page page = new Page(currentPage,5); //每页显示5条
        IPage pageData = blogService.page(page,new QueryWrapper<Blog>().orderByDesc("created"));
        return Result.success(pageData);
    }

    /**
     * 根据blog索引id查询博客文章
     * @param id
     * @return
     */
    @GetMapping("/blog/{id}")
    public Result find(@PathVariable(name = "id") Long id){ //@PathVariable是spring3.0的一个新功能：接收请求路径中占位符的值
        log.info("交互======> BlogController:/blog/{id}...");
        Blog blog = blogService.getById(id);
        //判断是否为空 为空则断言异常
        Assert.notNull(blog,"博客不存在");
        return Result.success(blog);
    }

    /**
     * 新增或修改当前blog
     * @param blog
     * @return
     */
    @RequiresAuthentication //需要认证之后才能访问
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog){
        log.info("交互======> BlogController:/blog/edit...");
        //默认新增编辑博客对象
        Blog temp = null;
        //不为空则编辑博客
        if (blog.getId() != null){
            temp = blogService.getById(blog.getId()); //将数据内容传给编辑博客temp
            Assert.isTrue(temp.getUserId().longValue()== ShiroUtil.getProfile().getId().longValue(),"没有编辑权限");
        } else {
            //否则新增博客编辑
            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        }
        //将blog的值赋给temp 忽略 id userid created status 引用于hutool
        BeanUtil.copyProperties(blog,temp,"id","userId","created","status");
        blogService.saveOrUpdate(temp);

        return Result.success(blog);
    }

    /**
     * 根据id删除blog模块
     * @param id
     * @return
     */
    @RequiresAuthentication //需要认证之后才能访问
    @PostMapping("/blogdel/{id}")
    public Result delete(@PathVariable(name = "id") Long id){
        log.info("交互======> BlogController:/blog/delete/{id}...");
        boolean blogId = blogService.removeById(id);
        if (blogId == true){
            return Result.success("blog删除成功！");
        } else {
            return Result.error("blog删除失败！");
        }
    }

}
