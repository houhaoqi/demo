package com.hmdp;

import org.junit.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class HmDianPingApplicationTests {

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void testRedisson() throws InterruptedException {
        //获取可重入锁
        RLock lock = redissonClient.getLock("anyLock");
        //尝试获取锁，三个参数分别是：获取锁的最大等待时间(期间会重试)，锁的自动释放时间，时间单位
        boolean success = lock.tryLock(1,10, TimeUnit.SECONDS);
        //判断获取锁成功
        if (success) {
            try {
                System.out.println("执行业务");
            } finally {
                //释放锁
                lock.unlock();
            }
        }
    }

}
