package com.zhulang.waveedu.basic.Dao;

import com.zhulang.waveedu.basic.dao.UserMapper;
import com.zhulang.waveedu.basic.po.User;
import com.zhulang.waveedu.common.util.PasswordEncoderUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 狐狸半面添
 * @create 2023-01-18 23:01
 */
@SpringBootTest
public class UserDaoTest {
    @Resource
    private UserMapper userMapper;

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setPassword(PasswordEncoderUtils.encode("123456"));
        user.setPhone("15675229376");
        userMapper.insert(user);
        System.out.println(user.getId());
    }
}
