package com.zhulang.waveedu.basic.Dao;

import com.zhulang.waveedu.basic.service.CollegeService;
import com.zhulang.waveedu.common.entity.Result;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 狐狸半面添
 * @create 2023-02-03 1:14
 */
@SpringBootTest
public class CollegeDaoTest {
    @Resource
    private CollegeService collegeService;
    @Test
    public void testGetLike(){
        Result result = collegeService.getCollegesByLike("湖南");
        List<String> data = (List<String>)result.getData();
        for (String datum : data) {
            System.out.println(datum);
        }
    }

}
