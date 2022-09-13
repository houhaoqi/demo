package com.haoqi.dto;

import com.haoqi.entity.OrderDetail;
import com.haoqi.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author haoqi
 * @Date 2022/7/24 - 11:34
 */
@Data
public class OrdersDto extends Orders {
    private List<OrderDetail> orderDetails;

    private String username;
    private String phone;
    private String address;
    private String consignee;
}
