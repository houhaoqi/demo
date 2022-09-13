package com.haoqi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haoqi.common.R;
import com.haoqi.entity.Employee;
import com.haoqi.service.impl.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 1.员工管理界面的操作调用 service层增删改查
 * @author haoqi
 * @Date 2022/7/21 - 10:20
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeServiceImpl employeeService;

    /**
     * 员工登录，
     * 提交用户名/密码 -> 校验用户名/密码 -> 校验用户状态 -> 返回用户成功/失败
     */
    @PostMapping("/login")
    public R<Employee> login (HttpServletRequest request, @RequestBody Employee employee){
        //将post的username和password验证
        //1.查询数据库是否存在该用户
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper); //转换为employee对象
        //2.将password进行md5加密与数据库比对
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //校验
        if (emp == null){
            //3.如果没有username返回失败
            return R.error("没有该用户！");
        }else if (!emp.getPassword().equals(password)){
            //4.密码比对，如果不一致则返回登录失败结果
            return R.error("密码错误！");
        }else if(emp.getStatus() == 0){
            //5.查看员工状态，如果为已禁用状态，则返回员工已禁用结果
            return R.error("该用户已锁定！");
        }

        //6.查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    //退出登录
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功!");
    }

    //==================业务处理================

    //新增员工
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("add employee{}",employee.toString());
        //给账号设置初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //注册时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //绑定id
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        //注入数据库
        employeeService.save(employee);

        return R.success("注册成功！");
    }
    //员工列表分页 mybatisPlusConfig.java->controller.java
    @GetMapping("/page")
    public R<Page> page( int page, int pageSize, String name){
        log.info("page={}, pageSize={}, name={}",page,pageSize,name);
        //1.构造分页器
        Page pageInfo = new Page(page,pageSize); //页面信息=当前页面+页面大小
        //2.构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>(); //定义一个lambda带有employee的泛型
        //3.添加过滤条件，是否有name参数查询
        queryWrapper.like(!StringUtils.isEmpty(name),Employee::getName,name);
        //4.添加排序条件，按员工创建时间排序time
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //5.执行sql向数据库查询
        employeeService.page(pageInfo,queryWrapper);
        //6.返回页面信息
        return R.success(pageInfo);
    }
    /**
     * mybatis-plus对id使用了雪花算法，所以存入数据库中的id是19为长度，
     * 但是前端的js只能保证数据的前16位的数据的精度，对我们id后面三位数据进行了四舍五入，所以就出现了精度丢失
     *
     * 1.使用自定义消息转换器 ====>useThis.
     *      JacksonObjectMapper.java -> mvcConfig.java配置数据处理 json-java
     * 2.关闭mybatis-plus的雪花算法来处理ID
     * @param request
     * @param employee
     * @return
     */
    //根据id修改员工信息，编辑员工信息通用代码块
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        System.out.println(employee.toString());
        //获取前台传递的employee的json格式对象
        Long empId = (Long)request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);

        return R.success("员工信息修改成功!");
    }
    //根据id查询并编辑员工信息，将数据返显前台，前台会调用上面update方法进行修改
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable String id){
        System.out.println("根据id编辑员工信息");
        Employee emp = employeeService.getById(id);
        if (emp != null){
            //将数据返显给前台
            return R.success(emp);
        }
        return R.error("没有查询到该信息！");
    }
}
