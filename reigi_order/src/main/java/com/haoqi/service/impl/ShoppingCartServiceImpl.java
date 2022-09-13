package com.haoqi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haoqi.entity.ShoppingCart;
import com.haoqi.mapper.ShoppingCartMapper;
import com.haoqi.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author haoqi
 * @Date 2022/7/24 - 15:59
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
