package com.zhulang.waveedu.common.util;

import cn.hutool.core.util.BooleanUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author 狐狸半面添
 * @create 2023-01-18 20:08
 */
public class RedisLockUtils {
    private final RedisTemplate redisTemplate;

    public RedisLockUtils(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public boolean tryLock(final String key,final Long timeout) {
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, "1", timeout, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    public void unlock(String key) {
        redisTemplate.delete(key);
    }
}
