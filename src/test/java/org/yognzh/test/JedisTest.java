package org.yognzh.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yognzh.jedis.util.JedisConnectionFactory;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * @author yongzh
 * @version 1.0
 * @program: jedis-demo
 * @description:
 * @date 2023/5/26 23:20
 */
public class JedisTest {
    private Jedis jedis;
    @BeforeEach
    void setUp(){
        //jedis = new Jedis("192.168.10.102",6379);
        jedis = JedisConnectionFactory.getJedis();
        jedis.auth("wuhu");
        jedis.select(0);
    }
    @Test
    void testString(){
        String result = jedis.set("name","虎哥");
        System.out.println("result = " + result);
        //获取数据
        String name = jedis.get("name");
        System.out.println("name = " + name);
    }
    @Test
    void testHash(){
        jedis.hset("user:1","name","Jack");
        jedis.hset("user:1","age","21");

        Map<String, String> map = jedis.hgetAll("user:1");
        System.out.println(map);

    }
    @AfterEach
    void tearDown(){
        if(jedis != null){
            jedis.close();
        }
    }
}
