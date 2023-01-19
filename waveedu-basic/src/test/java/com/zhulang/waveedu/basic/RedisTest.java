package com.zhulang.waveedu.basic;

import com.zhulang.waveedu.common.entity.RedisUser;
import com.zhulang.waveedu.common.util.RedisCacheUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 狐狸半面添
 * @create 2023-01-19 20:36
 */
@SpringBootTest
public class RedisTest {
    @Resource
    private RedisCacheUtils redisCacheUtils;

    @Test
    public void testRedisCache(){
        RedisUser redisUser = new RedisUser();
        redisUser.setJwt("adasda");
        redisCacheUtils.setCacheObject("a",redisUser);
    }
}
