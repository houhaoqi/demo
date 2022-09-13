package com.haoqi.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haoqi.domain.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author haoqi
 * @Date 2022/3/5 - 21:05
 */

@Mapper
@Repository
public interface BookDao extends BaseMapper<Book> {
    //mybatis
//    @Select("select * from testdb where id = #{id}")
//    Book getById(Integer id);
}

