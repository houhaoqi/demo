package com.hmdp.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 全局唯一id 黑马-redis-p51
 * @author haoqi
 * @Date 2022/12/2 - 15:59
 */
@Slf4j
@Component
public class RedisIdWorker {

//    public static void main(String[] args) {
//        // 设置起始时间，时间戳
//        LocalDateTime localDateTime = LocalDateTime.of(2022,12,1,0,0,0);
//        System.out.println(localDateTime.toEpochSecond(ZoneOffset.UTC)); //1669852800
//    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    // 设置起始时间 2022-12-1 0:0:0
    public static final Long BEGIN_TIMESTAMP = 1669852800L;
    //序列号长度
    public static final Long COUNT_BIT = 32L;

    public long nextId(String keyPrefix){
        // 1.生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long currentSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timeStamp = currentSecond - BEGIN_TIMESTAMP;

        // 2.生成序列号
        // 2.1.获取当前日期，精确到天
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        // 2.2.自增长
        long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);

        // 3.拼接并返回，位运算
        return timeStamp << COUNT_BIT | count;
    }



}
