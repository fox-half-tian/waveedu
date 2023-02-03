package com.zhulang.waveedu.edu.test.dao;

import com.zhulang.waveedu.edu.dao.LessonMapper;
import com.zhulang.waveedu.edu.query.TchInviteCodeQuery;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 狐狸半面添
 * @create 2023-02-03 22:55
 */
@SpringBootTest
public class LessonTest {
    @Resource
    private LessonMapper lessonMapper;

    @Test
    public void testSelectTchInviteCodeById(){
        TchInviteCodeQuery info = lessonMapper.selectTchInviteCodeById(1621451079562858497L);
        System.out.println(info);
    }
}
