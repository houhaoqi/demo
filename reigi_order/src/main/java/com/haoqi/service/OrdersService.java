package com.haoqi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haoqi.dto.OrdersDto;
import com.haoqi.entity.Orders;

/**
 * @author haoqi
 * @Date 2022/7/24 - 15:59
 */
public interface OrdersService extends IService<Orders> {
    //用户下单业务
    public void submit(Orders orders);

    // 用户个人中心订单信息查看
    public Page<OrdersDto> userPage(int page, int pageSize);

    //再来一单
    public void again(Orders order);

}
