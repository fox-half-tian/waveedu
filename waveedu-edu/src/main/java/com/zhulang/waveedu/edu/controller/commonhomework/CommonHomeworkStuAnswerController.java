package com.zhulang.waveedu.edu.controller.commonhomework;


import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.CommonHomeworkStuAnswerService;
import com.zhulang.waveedu.edu.vo.commonhomeworkvo.HomeworkAnswerVO;
import com.zhulang.waveedu.edu.vo.commonhomeworkvo.MarkHomeworkVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 普通作业表的学生回答表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-04
 */
@RestController
@RequestMapping("/common-homework-stu-answer")
public class CommonHomeworkStuAnswerController {
    @Resource
    private CommonHomeworkStuAnswerService commonHomeworkStuAnswerService;

    /**
     * 验证题目答案，学生可调用
     *
     * @param homeworkAnswerVos 题目id + 学生答案
     * @return 验证状况
     */
    @PostMapping("/stu/verifyAnswers")
    public Result verifyAnswers(@RequestBody List<HomeworkAnswerVO> homeworkAnswerVos) {
        return commonHomeworkStuAnswerService.verifyAnswers(homeworkAnswerVos);
    }

    /**
     * 获取学生的作业情况
     *
     * @param homeworkId 作业id
     * @param stuId      学生id
     * @return 情况
     */
    @GetMapping("/tch/get/stuHomeworkAnswers")
    public Result getStuHomeworkAnswers(@RequestParam("homeworkId") Integer homeworkId,
                                        @RequestParam("stuId") Long stuId) {
        return commonHomeworkStuAnswerService.getStuHomeworkAnswers(homeworkId, stuId);
    }

    /**
     * 创建者给学生的探究类作业批阅分数
     *
     * @param markHomeworkVO 信息
     * @return 是否成功
     */
    @PostMapping("/tch/mark/homework")
    public Result markHomework(@Validated @RequestBody MarkHomeworkVO markHomeworkVO) {
        return commonHomeworkStuAnswerService.markHomework(
                markHomeworkVO.getStuId(),
                markHomeworkVO.getComment(),
                markHomeworkVO.getInnerMarkList()
        );
    }

    /**
     * 将文件信息进行解密
     *
     * @param object 加密信息
     * @return 文件名 + 文件路径
     */
    @PostMapping("/saveFile")
    public Result saveFile(@RequestBody JSONObject object){
        return commonHomeworkStuAnswerService.saveFile(object.getString("fileInfo"));
    }

}
