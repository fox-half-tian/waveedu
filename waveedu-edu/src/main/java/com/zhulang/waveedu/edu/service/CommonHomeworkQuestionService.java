package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.CommonHomeworkQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.vo.homeworkvo.ModifyCommonHomeworkQuestionVO;
import com.zhulang.waveedu.edu.vo.homeworkvo.SaveCommonHomeworkQuestionVO;

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
     * 根据 作业id 查询到该作业的总分数（未校验），从question表获取
     *
     * @param homeworkId 作业id
     * @return 作业总分数
     */
    Integer getTmpTotalScoreByCommonHomeworkId(Integer homeworkId);

    /**
     * 查询作业的总分数（校验参数），从question表获取
     *
     * @param homeworkId 作业id
     * @return 总分
     */
    Result getTmpTotalScore(Integer homeworkId);

    /**
     * 教师预览作业的所有题目
     *
     * @param homeworkId 作业Id
     * @param pattern    预览模式，0-普通预览（无答案与解析），1-详细预览（有答案与解析）
     * @return 题目列表
     */
    Result getTchHomeworkQuestionListInfo(Integer homeworkId, Integer pattern);

    /**
     * 保存问题并修改总分数
     *
     * @param question 问题信息
     */
    void saveQuestionAndModifyTotalScore(CommonHomeworkQuestion question);

    /**
     * 删除问题并修改总分数
     *
     * @param questionId 问题id
     * @param homeworkId 问题所在的作业id
     */
    void removeQuestionAndModifyTotalScore(Integer questionId, Integer homeworkId);

    /**
     * 修改问题并修改总分数
     *
     * @param question   问题信息
     * @param homeworkId 问题所在的作业id
     */
    void modifyQuestionAndModifyTotalScore(CommonHomeworkQuestion question, Integer homeworkId);

    /**
     * 班级学生获取问题信息
     *
     * @param homeworkId 作业id
     * @return 根据学生对作业的状态进行返回
     */
    Result getStuHomeworkQuestionListInfo(Integer homeworkId);
}
