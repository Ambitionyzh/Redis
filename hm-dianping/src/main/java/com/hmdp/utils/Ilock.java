package com.hmdp.utils;

/**
 * @author yongzh
 * @version 1.0
 * @description:
 * @date 2023/6/10 22:15
 */
public interface Ilock
{
    /**
     * @description:  尝试获取锁
     * @param: timeoutSec锁持有的超时时间，过期后自动释放锁
     * @return: boolean true表示获取锁成功，false代表获取锁失败
     */
    boolean tryLock(long timeoutSec);


    void unlock();
}
