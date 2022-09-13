package com.haoqi.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * @author haoqi
 * @Date 2022/9/8 - 11:46
 *
 * :登录调用AccountRealm类下面的doGetAuthenticationInfo
 * 创建类AccountProfile 用于传递数据
 */
@Data
public class AccountProfile implements Serializable {

    private Long id;

    private String username;

    private String avatar;

    private String email;


}
