package com.haoqi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haoqi.entity.Employee;
import com.haoqi.mapper.EmployeeMapper;
import com.haoqi.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author haoqi
 * @Date 2022/7/21 - 10:20
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
