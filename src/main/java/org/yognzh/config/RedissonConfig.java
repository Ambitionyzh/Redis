package org.yognzh.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @program: jedis-demo
 * @description:
 * @date 2023/6/14 22:31
 */
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonConfig(){
        //配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.10.102:6379").setPassword("wuhu");
        return Redisson.create(config);
    }

}
