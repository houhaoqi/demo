package com.haoqi.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.haoqi.controller.util.R;
import com.haoqi.domain.Book;
import com.haoqi.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.TreeMap;

/**
 * @author haoqi
 * @Date 2022/3/6 - 21:45
 */
@RestController  //该类中所有springMvcUrl接口映射返回json数据格式
@RequestMapping("/books")
public class BookControllerR {

    /*
    update和save用异步提交发送，参数通过请求体传json数据
    delete和select单个一般使用路径变量来传参
    形参位置注释表明参数来源
 */
    @Autowired
    private IBookService iBookService;

    @GetMapping("/books") //查询所有数据
    public R getAll() {
        return new R(true, iBookService.list());
    }

    @GetMapping("/{id}") // 通过id查询数据
    public R getById(@PathVariable Integer id) {
        System.out.println("hot dev ... ............................................");
        return new R(true, iBookService.getById(id));
    }

    @GetMapping("{currentPage}/{size}") // 查询 页/条 数据
    public R getPage(@PathVariable Integer currentPage, @PathVariable Integer size, Book book) {
        System.out.println("参数 ====> " + book);
        IPage page = iBookService.getPage(currentPage, size, book);
        // 如果当前页码值大于总页数，重新执行查询操作，使用最大页码显示当前页面
        if (currentPage > page.getCurrent()) {
            page = iBookService.getPage((int) page.getCurrent(), size, book);
        }
        return new R(true, page);
    }

    @PostMapping //增加数据
    public R save(@RequestBody Book book) {
        return new R(iBookService.save(book));
    }

    @PutMapping //修改数据
    public R update(@RequestBody Book book) {
        return new R(iBookService.updateById(book));
    }

    @DeleteMapping("/{id}") //根据id删除数据
    public R delete(@PathVariable Integer id) {
//        boolean flag = iBookService.removeById(id);
        return new R(iBookService.delete(id));
    }

}
