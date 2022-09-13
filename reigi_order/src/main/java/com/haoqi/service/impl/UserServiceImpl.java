package com.haoqi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haoqi.entity.User;
import com.haoqi.mapper.UserMapper;
import com.haoqi.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author haoqi
 * @Date 2022/7/24 - 15:59
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
