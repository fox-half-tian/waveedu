package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.CommonHomeworkQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.vo.homework.SaveCommonHomeworkQuestionVO;

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
     * 添加一个题目
     *
     * @param saveCommonHomeworkQuestionVO 题目内容
     * @return 题目id
     */
    Result saveQuestion(SaveCommonHomeworkQuestionVO saveCommonHomeworkQuestionVO);
}
