package com.zhulang.waveedu.messagesdk.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.messagesdk.po.CommonHomeworkQuestion;

/**
 * <p>
 * 普通作业表的题目表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-28
 */
public interface CommonHomeworkQuestionService extends IService<CommonHomeworkQuestion> {

    /**
     * 根据 作业id 查询到该作业的总分数
     *
     * @param commonHomeworkId 作业id
     * @return 作业总分数
     */
    Integer getTotalScoreByCommonHomeworkId(Integer commonHomeworkId);
}
