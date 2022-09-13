package com.haoqi.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author haoqi
 * @Date 2022/9/7 - 20:27
 *
 * 创建MybatisPlusConfig类(创建路径com/vueblog/config)
 * mybatis-plus分页
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.haoqi.mapper")
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }
}
