package com.haoqi.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author haoqi
 * @Date 2022/9/8 - 9:45
 *
 * 自定义token
 * AuthenticationToken 用于收集用户提交的身份（如用户名）及凭据（如密码）
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token){
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    } //身份

    @Override
    public Object getCredentials() {
        return token;
    } //凭证
}

