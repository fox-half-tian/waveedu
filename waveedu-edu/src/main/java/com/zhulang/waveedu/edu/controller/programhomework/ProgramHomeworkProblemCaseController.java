package com.zhulang.waveedu.edu.controller.programhomework;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.ProgramHomeworkProblemCaseService;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SaveCaseVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 编程作业问题测试实例表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-13
 */
@RestController
@RequestMapping("/program-homework-problem-case")
public class ProgramHomeworkProblemCaseController {
    @Resource
    private ProgramHomeworkProblemCaseService programHomeworkProblemCaseService;

    /**
     * 添加测试案例
     *
     * @param saveCaseVO 案例信息
     * @return 案例id
     */
    @PostMapping("/save")
    public Result saveCase(@Validated @RequestBody SaveCaseVO saveCaseVO){
        return programHomeworkProblemCaseService.saveCase(saveCaseVO);
    }

    /**
     * 删除测试案例
     *
     * @param caseId 案例Id
     * @return 删除状况
     */
    @DeleteMapping("/remove")
    public Result removeCase(@RequestParam("caseId")Integer caseId){
        return programHomeworkProblemCaseService.removeCase(caseId);
    }

}
