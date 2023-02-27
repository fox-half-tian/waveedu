package com.zhulang.waveedu.edu.service.impl;

import com.zhulang.waveedu.edu.po.CommonHomeworkQuestion;
import com.zhulang.waveedu.edu.dao.CommonHomeworkQuestionMapper;
import com.zhulang.waveedu.edu.service.CommonHomeworkQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 普通作业表的题目表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-28
 */
@Service
public class CommonHomeworkQuestionServiceImpl extends ServiceImpl<CommonHomeworkQuestionMapper, CommonHomeworkQuestion> implements CommonHomeworkQuestionService {
    @Resource
    private CommonHomeworkQuestionMapper commonHomeworkQuestionMapper;
}
