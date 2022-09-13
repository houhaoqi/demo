package com.haoqi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haoqi.entity.OrderDetail;
import com.haoqi.mapper.OrderDetailMapper;
import com.haoqi.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author haoqi
 * @Date 2022/7/24 - 15:59
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
