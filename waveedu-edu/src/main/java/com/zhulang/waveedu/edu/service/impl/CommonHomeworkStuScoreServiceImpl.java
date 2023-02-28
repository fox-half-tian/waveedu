package com.zhulang.waveedu.edu.service.impl;

import com.zhulang.waveedu.edu.po.CommonHomeworkStuScore;
import com.zhulang.waveedu.edu.dao.CommonHomeworkStuScoreMapper;
import com.zhulang.waveedu.edu.service.CommonHomeworkStuScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 普通作业表的学生成绩表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-01
 */
@Service
public class CommonHomeworkStuScoreServiceImpl extends ServiceImpl<CommonHomeworkStuScoreMapper, CommonHomeworkStuScore> implements CommonHomeworkStuScoreService {
    @Resource
    private CommonHomeworkStuScoreMapper commonHomeworkStuScoreMapper;
}
