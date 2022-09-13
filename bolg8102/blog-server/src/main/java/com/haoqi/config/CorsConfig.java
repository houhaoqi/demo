package com.haoqi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域问题 这个配置是配置在Controller上的，在Controller之前还经过Filter
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //允许跨域请求域名
                .allowedOriginPatterns("*")
                //允许跨域方法
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                //允许跨域证书
                .allowCredentials(true)
                //允许跨域时间
                .maxAge(3600)
                .allowedHeaders("*");
    }
}
