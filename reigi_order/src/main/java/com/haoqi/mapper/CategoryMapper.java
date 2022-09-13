package com.haoqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haoqi.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author haoqi
 * @Date 2022/7/22 - 15:17
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    //mybatis-plus crud...
}
