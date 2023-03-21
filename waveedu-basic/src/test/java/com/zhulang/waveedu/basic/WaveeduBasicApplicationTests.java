package com.zhulang.waveedu.basic;

import cn.hutool.core.util.RandomUtil;
import com.zhulang.waveedu.basic.dao.CollegeMapper;
import com.zhulang.waveedu.basic.po.College;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class WaveeduBasicApplicationTests {
    @Resource
    private CollegeMapper collegeMapper;

    private final static String str = "0123456789zxcvbnmasdfghjklqwertyuiopZXCVBNMASDFGHJKLQWERTYUIOP";

    @Test
    void contextLoads() {
        College college;
        for (int i = 10001;i<=19145;i++){
            college = new College();
            college.setId(i);
            college.setTchCode(RandomUtil.randomString(str,24));
            collegeMapper.updateById(college);
        }
    }

}
