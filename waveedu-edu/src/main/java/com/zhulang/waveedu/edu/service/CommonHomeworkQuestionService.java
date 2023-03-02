package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.CommonHomeworkQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.vo.homework.ModifyCommonHomeworkQuestionVO;
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

    /**
     * 删除一个题目
     * 只有未发布的作业才可以删除题目
     *
     * @param questionId 题目Id
     * @return 是否删除
     */
    Result delQuestion(Integer questionId);

    /**
     * 修改一个题目
     *
     * @param modifyCommonHomeworkQuestionVO 修改后的题目内容
     * @return 修改状况
     */
    Result modifyQuestion(ModifyCommonHomeworkQuestionVO modifyCommonHomeworkQuestionVO);

    /**
     * 根据 作业id 查询到该作业的总分数（未校验）
     *
     * @param commonHomeworkId 作业id
     * @return 作业总分数
     */
    Integer getTotalScoreByCommonHomeworkId(Integer commonHomeworkId);

    /**
     * 查询作业的总分数（校验参数）
     *
     * @param homeworkId 作业id
     * @return 总分
     */
    Result getHomeworkTotalScore(Integer homeworkId);
}
