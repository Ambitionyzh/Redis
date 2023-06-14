package org.yognzh.jedis.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author yongzh
 * @version 1.0
 * @program: jedis-demo
 * @description:
 * @date 2023/5/27 11:10
 */
public class JedisConnectionFactory {
    private static final JedisPool JEDIS_POOL ;
    static {
        //配置连接池
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(8);
        poolConfig.setMaxIdle(8);
        poolConfig.setMinIdle(0);
        poolConfig.setMaxWaitMillis(1000);
        //创建连接对象
        JEDIS_POOL = new JedisPool(poolConfig,"192.168.10.102",6379,1000,"wuhu");

    }
    public static Jedis getJedis(){
        return JEDIS_POOL.getResource();
    }
}
