package com.haoqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haoqi.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author haoqi
 * @Date 2022/7/24 - 16:00
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
