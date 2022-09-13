package com.haoqi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haoqi.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author haoqi
 * @Date 2022/3/6 - 21:32
 */
@SpringBootTest
public class IBookServiceImpl {
    @Autowired
    private IBookService bookService;

    @Test
    public void testSave() {
        Book book = new Book();
        book.setName("service测试数据5");
        book.setType("service测试类型5");
        book.setDescription("service测试描述数据5");
        bookService.save(book);
    }

    @Test
    public void testUpdate() {
        Book book = new Book();
        book.setId(5);
        book.setName("service测试数据1");
        book.setType("service测试类型1");
        book.setDescription("service测试描述数据1");
        bookService.updateById(book);
    }

    @Test
    public void testDelete() {
        bookService.removeById(9);
    }

    @Test
    public void testGetAll() {
        bookService.list();
    }

    @Test
    public void testGetPage() {

        IPage<Book> page = new Page<>(2, 5);
        bookService.page(page);
        System.out.println(page.getCurrent());
        System.out.println(page.getSize());
        System.out.println(page.getTotal());
        System.out.println(page.getPages());
        System.out.println(page.getRecords());
    }
}
