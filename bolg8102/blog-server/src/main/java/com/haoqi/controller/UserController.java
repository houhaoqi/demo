package com.haoqi.controller;


import com.haoqi.common.lang.Result;
import com.haoqi.entity.User;
import com.haoqi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author anonymous
 * @since 2022-09-07
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequiresAuthentication //登录拦截注解
    @GetMapping("/index")
    public Object index(){
        log.info("交互======> UserController:/index ... ");
        User user = userService.getById(1);
        System.out.println(user);
        return Result.success(user);
    }

    /**
     *
     * @RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)；
     * GET方式无请求体，所以使用@RequestBody接收数据时，
     * 前端不能使用GET方式提交数据，
     * 而是用POST方式进行提交。在后端的同一个接收方法里，
     * @RequestBody与@RequestParam()可以同时使用，@RequestBody最多只能有一个，
     * 而@RequestParam()可以有多个。
     *
     * @Validated注解用于检查user中填写的规则
     * 这里加Validated注解如果校验不通过可以直接抛出异常,异常处理在全局异常处理
     */
    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user){
        log.info("交互======> UserController:/save ... ");
        return Result.success(user);
    }



}
