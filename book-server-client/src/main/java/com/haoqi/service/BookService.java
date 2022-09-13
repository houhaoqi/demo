package com.haoqi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.haoqi.domain.Book;

import java.util.List;

/**
 * @author haoqi
 * @Date 2022/3/6 - 15:59
 */
public interface BookService {
    boolean save(Book book);

    boolean update(Book book);

    boolean delete(Integer id);

    Book getById(Integer id);

    List<Book> getAll();

    IPage<Book> getByPage(int currentPage, int size);

}
