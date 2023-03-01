package com.zhulang.waveedu.messagesdk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.messagesdk.dao.CommonHomeworkQuestionMapper;
import com.zhulang.waveedu.messagesdk.po.CommonHomeworkQuestion;
import com.zhulang.waveedu.messagesdk.service.CommonHomeworkQuestionService;
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

    @Override
    public Integer getTotalScoreByCommonHomeworkId(Integer commonHomeworkId) {
        return commonHomeworkQuestionMapper.selectTotalScoreByCommonHomeworkId(commonHomeworkId);
    }
}
