package com.haoqi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haoqi.domain.Book;

/**
 * @author haoqi
 * @Date 2022/3/6 - 21:20
 */
public interface IBookService extends IService<Book> {
    //自定义方法功能追加
    boolean saveBook(Book book);

    boolean modify(Book book);

    boolean delete(Integer id);

    IPage<Book> getPage(Integer currentPage, Integer size);

    IPage<Book> getPage(int current, Integer pageSize, Book book);

}
