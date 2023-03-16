package com.zhulang.waveedu.edu.controller.programhomework;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.ProgramHomeworkProblemService;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.ModifyProblemVO;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SaveProblemVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 编程作业表的题目表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
@RestController
@RequestMapping("/program-homework-problem")
public class ProgramHomeworkProblemController {
    @Resource
    private ProgramHomeworkProblemService programHomeworkProblemService;

    /**
     * 保存一道题目
     *
     * @param saveProblemVO 作业id + 问题标题
     * @return 题目id
     */
    @PostMapping("/save")
    public Result saveProblem(@RequestBody @Validated SaveProblemVO saveProblemVO){
        return programHomeworkProblemService.saveProblem(saveProblemVO);
    }

    /**
     * 修改问题
     *
     * @param modifyProblemVO 问题信息
     * @return 修改状况
     */
    @PutMapping("/modify")
    public Result modifyProblem(@RequestBody @Validated ModifyProblemVO modifyProblemVO){
        return programHomeworkProblemService.modifyProblem(modifyProblemVO);
    }

    /**
     * 删除题目
     *
     * @param problemId 题目id
     * @return 删除状况
     */
    @DeleteMapping("/remove")
    public Result removeProblem(@RequestParam("problemId")Integer problemId){
        return programHomeworkProblemService.removeProblem(problemId);
    }

    /**
     * 作业创建者获取作业问题列表
     *
     * @param homeworkId 作业id
     * @return 列表信息
     */
    @GetMapping("/tch/getHomeworkProblemList")
    public Result tchGetHomeworkProblemList(@RequestParam("homeworkId")Integer homeworkId){
        return programHomeworkProblemService.tchGetHomeworkProblemList(homeworkId);
    }

    /**
     * 学生获取问题的详细信息
     *
     * @param problemId 问题id
     * @return 详细信息
     */
    @GetMapping("/stu/getHomeworkProblemDetailInfo")
    public Result stuGetHomeworkProblemDetailInfo(@RequestParam("problemId")Integer problemId){
        return programHomeworkProblemService.stuGetHomeworkProblemDetailInfo(problemId);
    }

    /**
     * 教师获取问题的详细信息
     *
     * @param problemId 问题id
     * @return 详细信息
     */
    @GetMapping("/tch/getHomeworkProblemDetailInfo")
    public Result tchGetHomeworkProblemDetailInfo(@RequestParam("problemId")Integer problemId){
        return programHomeworkProblemService.tchGetHomeworkProblemDetailInfo(problemId);
    }

}
