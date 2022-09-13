package com.haoqi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan //springMvc扫描静态资源
@EnableTransactionManagement //开启事务功能注解
public class ReigiOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReigiOrderApplication.class, args);
    }

}
