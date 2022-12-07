package com.hmdp.utils;

import cn.hutool.core.lang.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author haoqi
 * @Date 2022/12/3 - 11:59
 */
public class SimpleRedisLock implements ILock {


    private String name;    // 具体业务，将锁前缀和业务名拼接之后当作key
    private StringRedisTemplate stringRedisTemplate;    //这里不需要@Autowired，因为该对象是我们使用构造函数手动new出来的

    private static final String KEY_PREFIX = "lock:";  // 锁的前缀
    private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "-"; // 锁的标识缀

    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean tryLock(long timeoutSec) {
        // 获取线程标识
//        long threadId = Thread.currentThread().getId();
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        // 获取锁，使用setnx方法进行枷锁，同时设置过期时间，防止死锁
        Boolean success = stringRedisTemplate.opsForValue().
                setIfAbsent(KEY_PREFIX+name,threadId+"",timeoutSec, TimeUnit.SECONDS);

        //自动拆箱可能会出现null，这样写更稳妥
        return Boolean.TRUE.equals(success);
    }


    // 通过lua脚本解锁，unlock.lua 确保原子性
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;
    static {
        UNLOCK_SCRIPT = new DefaultRedisScript();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    @Override
    public void unlock() {
        stringRedisTemplate.execute(UNLOCK_SCRIPT,
                Collections.singletonList(KEY_PREFIX + name),
                ID_PREFIX + Thread.currentThread().getId());
    }


    /*@Override
    public void unlock() {
        // 获取当前线程的标识
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        // 获取锁中的标识
        String id = stringRedisTemplate.opsForValue().get(KEY_PREFIX + name);
        // 判断标识是否一致
        if (threadId.equals(id)) {
            // 释放锁
            stringRedisTemplate.delete(KEY_PREFIX + name);
        }
    }

     */
}
