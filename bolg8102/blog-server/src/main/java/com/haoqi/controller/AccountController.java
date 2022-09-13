package com.haoqi.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haoqi.common.dto.LoginDto;
import com.haoqi.common.lang.Result;
import com.haoqi.entity.User;
import com.haoqi.service.UserService;
import com.haoqi.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * @author haoqi
 * @Date 2022/9/8 - 14:56
 */
@Slf4j
@RestController
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * 用户登录
     * @param loginDto
     * @param response
     * @return
     */
    @PostMapping("/login")
    public Result login(@Validated  @RequestBody LoginDto loginDto, HttpServletResponse response){
        log.info("交互======> AccountController:/login ... ");
        /**
         * queryWrapper是mybatis plus中实现查询的对象封装操作类
         */
        User user = userService.getOne(new QueryWrapper<User>().eq("username",loginDto.getUsername()));

        //校验用户
//        Assert.notNull(user,"用户不存在"); //我感觉这样处理对返回给前端不够方便,主要会影响前端后置拦截的编写
        if(user==null){
            log.info("用户不存在");
            return Result.error("用户不存在");
        }

        if(!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
            log.info("密码错误");
            return Result.error("密码不正确");
        }
        //生成Token
        String jwt = jwtUtils.generateToken(user.getId());
        response.setHeader("Authorization",jwt);
        response.setHeader("Access-control-Expose-Headers","Authorization");

        return Result.success(
                MapUtil.builder()
                        .put("id",user.getId())
                        .put("username",user.getUsername())
                        .put("avatar",user.getAvatar())
                        .put("email",user.getEmail())
                        .map()
        );
    }

    /**
     * 退出
     * @return
     */
    @RequiresAuthentication //require认证之后才能登录的一个权限
    @GetMapping("/logout")
    public Result logout(){
        log.info("交互======> AccountController:/logout ... ");
        SecurityUtils.getSubject().logout();
        log.info("logout ...");
        return Result.success("退出成功");
    }

}
