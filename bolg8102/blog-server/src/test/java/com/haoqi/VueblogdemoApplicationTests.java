package com.haoqi;

import com.haoqi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootTest
class VueblogdemoApplicationTests {

    @Autowired
    UserService userService;

    public Object index(){
        return  userService.getById(1);
    }

    @Test
    void contextLoads() {
        System.out.println(index());
    }

}
