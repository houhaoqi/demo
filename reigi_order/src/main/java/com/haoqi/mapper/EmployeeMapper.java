package com.haoqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haoqi.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author haoqi
 * @Date 2022/7/21 - 10:19
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    //mybatis-plus crud...
}
