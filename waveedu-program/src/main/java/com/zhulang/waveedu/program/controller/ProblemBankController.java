package com.zhulang.waveedu.program.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.program.constant.AuthorTypeConstants;
import com.zhulang.waveedu.program.service.ProblemBankService;
import com.zhulang.waveedu.program.vo.ModifyProblemVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.zhulang.waveedu.program.constant.AuthorTypeConstants.ADMIN;
import static com.zhulang.waveedu.program.constant.AuthorTypeConstants.USER;

/**
 * <p>
 * 编程问题题库表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-11
 */
@RestController
@RequestMapping("/problem-bank")
public class ProblemBankController {
    @Resource
    private ProblemBankService problemBankService;

    /**
     * 管理员创建题目
     *
     * @param object 题目标题
     * @return 创建状况
     */
    @PostMapping("/admin/saveProblem")
    public Result adminSaveProblem(@RequestBody JSONObject object){
        return problemBankService.saveProblem(object.getString("title"), ADMIN);
    }


    /**
     * 用户创建题目
     *
     * @param object 题目标题
     * @return 题目信息
     */
    @PostMapping("/user/saveProblem")
    public Result userSaveProblem(@RequestBody JSONObject object){
        return problemBankService.saveProblem(object.getString("title"),USER);
    }

    /**
     * 管理员修改题目
     *
     * @param modifyProblemVO 题目信息
     * @return 修改状况
     */
    @PutMapping("/admin/modifyProblem")
    public Result adminModifyProblem(@RequestBody @Validated ModifyProblemVO modifyProblemVO){
        return problemBankService.modifyProblem(modifyProblemVO,ADMIN);
    }

    /**
     * 用户修改题目
     *
     * @param modifyProblemVO 题目信息
     * @return 修改状况
     */
    @PutMapping("/user/modifyProblem")
    public Result userModifyProblem(@RequestBody @Validated ModifyProblemVO modifyProblemVO){
        return problemBankService.modifyProblem(modifyProblemVO,USER);
    }

    /**
     * 管理员删除题目
     *
     * @param problemId 题目id
     * @return 修改状况
     */
    @DeleteMapping("/admin/removeProblem")
    public Result adminRemoveProblem(@RequestParam("problemId")Integer problemId){
        return problemBankService.removeProblem(problemId,ADMIN);
    }

    /**
     * 用户删除题目
     *
     * @param problemId 题目id
     * @return 修改状况
     */
    @DeleteMapping("/user/removeProblem")
    public Result userRemoveProblem(@RequestParam("problemId")Integer problemId){
        return problemBankService.removeProblem(problemId,USER);
    }

    /**
     * 用户获取题目列表
     *
     * @return 题目信息列表
     */
    @GetMapping("/user/getProblemList")
    public Result userGetProblemList(){
        return problemBankService.getProblemList(USER);
    }

    /**
     * 管理员获取题目列表
     *
     * @return 题目信息列表
     */
    @GetMapping("/admin/getProblemList")
    public Result adminGetProblemList(){
        return problemBankService.getProblemList(ADMIN);
    }

    /**
     * 管理员获取问题的详细信息
     *
     * @param problemId 问题id
     * @return 信息
     */
    @GetMapping("/admin/getProblemDetailInfo")
    public Result adminGetProblemDetailInfo(@RequestParam("problemId")Integer problemId){
        return problemBankService.getProblemDetailInfo(problemId,ADMIN);
    }

    /**
     * 用户获取问题的详细信息
     *
     * @param problemId 问题id
     * @return 信息
     */
    @GetMapping("/user/getProblemDetailInfo")
    public Result userGetProblemDetailInfo(@RequestParam("problemId")Integer problemId){
        return problemBankService.getProblemDetailInfo(problemId,USER);
    }
}
