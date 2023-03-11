package com.zhulang.waveedu.program.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.program.constant.AuthorTypeConstants;
import com.zhulang.waveedu.program.service.ProblemBankCaseService;
import com.zhulang.waveedu.program.service.ProblemBankService;
import com.zhulang.waveedu.program.vo.SaveProblemCaseVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

import static com.zhulang.waveedu.program.constant.AuthorTypeConstants.ADMIN;
import static com.zhulang.waveedu.program.constant.AuthorTypeConstants.USER;

/**
 * <p>
 * 编程问题题库测试实例表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
@RestController
@RequestMapping("/problem-bank-case")
public class ProblemBankCaseController {
    @Resource
    private ProblemBankCaseService problemBankCaseService;

    /**
     * 管理员添加测试案例
     *
     * @param saveProblemCaseVO 案例信息
     * @return 案例id
     */
    @PostMapping("/admin/saveCase")
    public Result adminSaveCase(@RequestBody @Validated SaveProblemCaseVO saveProblemCaseVO){
        return problemBankCaseService.saveCase(saveProblemCaseVO, ADMIN);
    }

    /**
     * 用户添加测试案例
     *
     * @param saveProblemCaseVO 案例信息
     * @return 案例id
     */
    @PostMapping("/user/saveCase")
    public Result userSaveCase(@RequestBody @Validated SaveProblemCaseVO saveProblemCaseVO){
        return problemBankCaseService.saveCase(saveProblemCaseVO, USER);
    }

    /**
     * 管理员移除测试案例
     *
     * @param caseId 案例id
     * @return 删除状况
     */
    @DeleteMapping("/admin/removeCase")
    public Result adminSaveCase(@RequestParam("caseId")Integer caseId){
        return problemBankCaseService.removeCase(caseId, ADMIN);
    }

    /**
     * 用户删除测试案例
     *
     * @param caseId 案例id
     * @return 删除状况
     */
    @DeleteMapping("/user/removeCase")
    public Result userSaveCase(@RequestParam("caseId") Integer caseId){
        return problemBankCaseService.removeCase(caseId, USER);
    }
}
