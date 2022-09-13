package com.haoqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haoqi.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author haoqi
 * @Date 2022/7/24 - 16:02
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
