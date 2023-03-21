package com.zhulang.waveedu.basic.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.basic.dao.CollegeMapper;
import com.zhulang.waveedu.basic.po.College;
import com.zhulang.waveedu.basic.service.CollegeService;
import com.zhulang.waveedu.common.constant.HttpStatus;
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

    @Override
    public Result getPageRecords(Integer pageNum, Integer recordNum) {
        Page<College> page = new Page<>(pageNum, recordNum);
        collegeMapper.selectPage(page, null);
        return Result.ok(page);
    }

    @Override
    public Result modifyTchCode(Integer collegeId) {
        College college = new College();
        college.setId(collegeId);
        String tchCode = RandomUtil.randomString("0123456789zxcvbnmasdfghjklqwertyuiopZXCVBNMASDFGHJKLQWERTYUIOP", 24);
        college.setTchCode(tchCode);
        int updateCount = collegeMapper.updateById(college);
        return updateCount != 0 ? Result.ok(tchCode) : Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "修改失败");
    }

    @Override
    public Result getCollegeInfo(String name) {
        return Result.ok(collegeMapper.selectOne(new LambdaQueryWrapper<College>()
                .eq(College::getName, name)));
    }
}
