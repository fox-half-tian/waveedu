package com.zhulang.waveedu.basic;

import cn.hutool.core.lang.UUID;
import com.zhulang.waveedu.common.entity.RedisUser;
import com.zhulang.waveedu.common.util.RedisCacheUtils;
import com.zhulang.waveedu.common.util.RegexUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        redisUser.setUuid("adasda");
        redisCacheUtils.setCacheObject("a",redisUser);
    }

    @Test
    public void testUUID(){
        String uuid = UUID.randomUUID().toString(true);
        System.out.println(uuid);
        System.out.println(uuid.length());
        System.out.println(RegexUtils.isUUIDInvalid(uuid));
    }

    @Test
    public void testStringEqual(){
        System.out.println("1619233554649366529".length());
    }

}
