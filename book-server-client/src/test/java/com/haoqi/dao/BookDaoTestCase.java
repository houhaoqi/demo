package com.haoqi.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haoqi.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author haoqi
 * @Date 2022/3/5 - 21:08
 */

@SpringBootTest
public class BookDaoTestCase {
    // 定义全局对象
    Book book = new Book();
    @Autowired
    private BookDao bookDao;

    @Test
    void testById() {
        //mybatis格式
        //System.out.println(bookDao.getById(1));
        //mybatis plus格式
        System.out.println(bookDao.selectById(1));
    }

    @Test
    void testSave() { //插入操作
//        book.setId(2);    //数据有id自增策略 yml配置中查看
        int i = 0;
        for (i = 7; i < 15; i++) {
            book.setType("测试id" + i);
            book.setName("测试id" + i);
            book.setDescription("测试id" + i);
            bookDao.insert(book);
        }
        System.out.println("共添加数据条数：" + (i - 7));
    }

    @Test
    void testUpdate() { //更改操作
        book.setId(4);
        book.setType("修改测试");
        book.setName("修改测试");
        book.setDescription("修改测试");
        bookDao.updateById(book); //根据id修改
    }

    @Test
    void testDelete() { //删除操作
        bookDao.deleteById(3);
    }

    @Test
    void testGetAll() { //获取表的所有数据
        System.out.println(bookDao.selectList(null));
    }

    @Test
    void testGetPage() { //制作分页
        IPage page = new Page(3, 3); // 第一页三条数据
        bookDao.selectPage(page, null);
        //观察表
        System.out.println(page.getCurrent()); //当前页
        System.out.println(page.getPages());    //总页数
        System.out.println(page.getRecords());  //当页数据内容
        System.out.println(page.getSize());     //每页啊数据条数
        System.out.println(page.getTotal());    //总数据条数
        System.out.println(page.getClass());    //页面类
    }

    @Test
    void testGetBy() { //按条件查询
        String name = "haoqi"; //null不连接查询
        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<Book>();
        lqw.like(name != null, Book::getName, name);

        // 带条件的分页查询
        System.out.println("======带条件的分页查询=========");
        IPage page = new Page(1, 3); // 第一页三条数据
        bookDao.selectPage(page, lqw);

    }

}
