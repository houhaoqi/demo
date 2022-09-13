package com.haoqi.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author haoqi
 * @Date 2022/3/5 - 22:11
 */

@Configuration
public class MPConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //用于Mybatis plus追加Sql语句的拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor()); //用于分页的拦截器方法

        return interceptor;
    }
}
