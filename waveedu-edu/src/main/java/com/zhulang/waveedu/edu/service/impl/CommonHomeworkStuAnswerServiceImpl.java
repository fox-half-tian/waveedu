package com.zhulang.waveedu.edu.service.impl;

import com.zhulang.waveedu.edu.po.CommonHomeworkStuAnswer;
import com.zhulang.waveedu.edu.dao.CommonHomeworkStuAnswerMapper;
import com.zhulang.waveedu.edu.service.CommonHomeworkStuAnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 普通作业表的学生回答表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-04
 */
@Service
public class CommonHomeworkStuAnswerServiceImpl extends ServiceImpl<CommonHomeworkStuAnswerMapper, CommonHomeworkStuAnswer> implements CommonHomeworkStuAnswerService {
    @Resource
    private CommonHomeworkStuAnswerMapper commonHomeworkStuAnswerMapper;
}
