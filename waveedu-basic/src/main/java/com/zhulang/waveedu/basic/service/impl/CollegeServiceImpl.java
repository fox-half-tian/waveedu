package com.zhulang.waveedu.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.basic.dao.CollegeMapper;
import com.zhulang.waveedu.basic.po.College;
import com.zhulang.waveedu.basic.service.CollegeService;
import com.zhulang.waveedu.common.entity.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 狐狸半面添
 * @create 2023-02-03 0:25
 */
@Service
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College> implements CollegeService {
    @Resource
    private CollegeMapper collegeMapper;
    @Override
    public Result getCollegesByLike(String name) {
        List<String> names = collegeMapper.selectNamesLikeName(name);
        return Result.ok(names);
    }
}
