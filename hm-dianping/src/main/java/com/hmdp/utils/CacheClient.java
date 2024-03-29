package com.hmdp.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hmdp.entity.Shop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.internal.Function;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;

/**
 * @author Administrator
 * @version 1.0
 * @program: hm-dianping
 * @description:封装解决缓存穿透，缓存击穿，缓存雪崩的代码。生成一个工具类。
 * @date 2023/5/31 23:41
 */
@Component
@Slf4j
public class CacheClient {
    private final StringRedisTemplate stringRedisTemplate;


    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void set(String key, Object value, Long time , TimeUnit unit){
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value),time,unit);
    }

    public void setLogicExpire(String key, Object value, Long time , TimeUnit unit){
        //设置逻辑过期
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds((unit.toSeconds(time))));
        //将Java对象序列化为json存储在string类型的key中
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }
    /** 
     * @description:  使用泛型对方法进行封装，因为查询的时候返回值类型是不确定的,ID类型也不确定。由调用者传入真实类型
     * R为返回值类型，ID为参数类型。Class<R>type代表返回值具体类型，Function<ID,R>dbCallBack代表查数据库方法，由调入者传入。
     * @param: keyPrefix
     * @param: id
     * @param: type
     * @param: dbCallBack
     * @param: time
     * @param: unit 
     * @return: R 
     * @author yongzh
     * @date: 2024/3/29 23:20
     */ 
    public <R,ID>R queryWithPassThrough(String keyPrefix, ID id, Class<R>type, Function<ID,R> dbCallBack,
                                         Long time , TimeUnit unit) {
        String key = keyPrefix + id;
        //从redis查询商铺缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        //是否存在
        if(StrUtil.isNotBlank(json)){
            return JSONUtil.toBean(json, type);
        }
        //命中是否是空值
        if(json != null){
            return null;
        }
        //不存在，根据id查询数据库
        R r = dbCallBack.apply(id);
        if(r == null){
            stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }

        //存在，写入redis
        this.set(key,r,time,unit);
        return r;
    }

    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);
    public  <R,ID>R queryWithLogicalExpire(String keyPrefix,ID id,Class<R> type,Function<ID,R> dbFallback,
                                           Long time , TimeUnit unit) {
        String key = keyPrefix + id;
        //1.从redis查询商铺缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        //2.是否存在
        if(StrUtil.isBlank(shopJson)){
            //3.未命中返回null
            return null;
        }
        //4.命中，需要把json反序列化为对象
        RedisData redisData = JSONUtil.toBean(shopJson, RedisData.class);
        R r = JSONUtil.toBean((JSONObject) redisData.getData(),type);
        LocalDateTime expireTime = redisData.getExpireTime();

        //5判断是否过期
        if(expireTime.isAfter(LocalDateTime.now())){
            //5.1未过期，直接返回店铺信息
            return r;
        }
        //5.2已过期进行缓存重建

        //6缓存重建
        //6.1获取互斥锁
        String lockKey = LOCK_SHOP_KEY + id;
        boolean isLock = tryLock(lockKey);
        //6.2判断是否获取锁成功
        if(isLock){
            //成功拿到锁，开启独立线程，实现缓存重建
            CACHE_REBUILD_EXECUTOR.submit(()->{


                try {
                    //重建缓存
                    //1.查询数据胡
                    R r1 = dbFallback.apply(id);
                    //2.存入redis
                    this.setLogicExpire(key,r1,time,unit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    //释放锁
                    unlock(lockKey);
                }

            });
        }

        return r;
    }
    private boolean tryLock(String key){
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }
    private boolean unlock(String key){
        return  stringRedisTemplate.delete(key);
    }
}
