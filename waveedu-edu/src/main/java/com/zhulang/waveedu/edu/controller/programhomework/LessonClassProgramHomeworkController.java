package com.zhulang.waveedu.edu.controller.programhomework;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.LessonClassCommonHomeworkService;
import com.zhulang.waveedu.edu.service.LessonClassProgramHomeworkService;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.ModifyProgramHomeworkVO;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SaveProgramHomeworkVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 * 编程作业表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
@RestController
@RequestMapping("/lesson-class-program-homework")
public class LessonClassProgramHomeworkController {
    @Resource
    private LessonClassProgramHomeworkService lessonClassProgramHomeworkService;

    /**
     * 添加一份作业
     *
     * @param saveProgramHomeworkVO 班级id + 作业标题
     * @return 作业id
     */
    @PostMapping("/save")
    public Result saveHomework(@RequestBody @Validated SaveProgramHomeworkVO saveProgramHomeworkVO){
        return lessonClassProgramHomeworkService.saveHomework(saveProgramHomeworkVO);
    }

    /**
     * 修改作业信息（标题与截止时间）
     *
     * @param modifyProgramHomeworkVO 标题，截止时间
     * @return 修改状况
     */
    @PutMapping("/modify/info")
    public Result modifyInfo(@RequestBody @Validated ModifyProgramHomeworkVO modifyProgramHomeworkVO){
        return lessonClassProgramHomeworkService.modifyInfo(modifyProgramHomeworkVO);
    }

    /**
     * 删除作业
     *
     * @param homeworkId 作业Id
     * @return 删除状态
     */
    @DeleteMapping("/remove")
    public Result removeHomework(@RequestParam("homeworkId")Integer homeworkId){
        return lessonClassProgramHomeworkService.removeHomework(homeworkId);
    }

}
