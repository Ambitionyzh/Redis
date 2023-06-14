package com.hmdp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @program: hm-dianping
 * @description:
 * @date 2023/6/14 23:30
 */
@Slf4j
@SpringBootTest
public class RedissonTest {
    @Resource
    private RedissonClient redissonClient;

    private RLock lock;

    @BeforeEach
    void setUp() {
        lock = redissonClient.getLock("lock");
    }

    @Test
    void method1() {
        boolean success = lock.tryLock();
        if (!success) {
            log.error("获取锁失败，1");
            return;
        }
        try {
            log.info("获取锁成功");
            method2();
        } finally {
            log.info("释放锁，1");
            lock.unlock();
        }
    }

    void method2() {
        RLock lock = redissonClient.getLock("lock");
        boolean success = lock.tryLock();
        if (!success) {
            log.error("获取锁失败，2");
            return;
        }
        try {
            log.info("获取锁成功，2");
        } finally {
            log.info("释放锁，2");
            lock.unlock();
        }
    }
}
