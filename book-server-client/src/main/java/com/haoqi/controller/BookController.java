package com.haoqi.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.haoqi.domain.Book;
import com.haoqi.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author haoqi
 * @Date 2022/3/6 - 21:45
 */
//@RestController //停用 避免与BookControllerR冲突
//@RequestMapping("/books")
public class BookController {
    @Autowired
    private IBookService iBookService;

    @GetMapping //查询所有数据
    public List<Book> getAll() {
        return iBookService.list();
    }

    @GetMapping("/{id}") // 通过id查询数据
    public Book getById(@PathVariable Integer id) {
        return iBookService.getById(id);
    }

    @GetMapping("{currentPage}/{size}") // 查询 页/条 数据
    public IPage<Book> getPage(@PathVariable Integer currentPage, @PathVariable Integer size) {
        return iBookService.getPage(currentPage, size);
    }

    @PostMapping //增加数据
    public boolean save(@RequestBody Book book) {
        return iBookService.save(book);
    }

    @PutMapping //修改数据
    public boolean update(@RequestBody Book book) {
        return iBookService.updateById(book);
    }

    @DeleteMapping("/{id}") //根据id删除数据
    public boolean delete(@PathVariable Integer id) {
        return iBookService.delete(id);
    }

}
