package com.zhulang.waveedu.edu.controller.programhomework;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.ProblemBankService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
     * 获取自己的题库列表
     *
     * @return 题库列表
     */
    @GetMapping("/get/selfProblemInfoList")
    public Result getSelfProblemInfoList(){
        return problemBankService.getSelfProblemInfoList();
    }

    /**
     * 获取公开题库的列表信息
     *
     * @return 题库列表
     */
    @GetMapping("/get/publicProblemInfoList")
    public Result getPublicProblemInfoList(){
        return problemBankService.getPublicProblemInfoList();
    }


    /**
     * 获取自己题库或公开题库中某个问题的详细信息
     *
     * @param problemId 问题id
     * @return 详细信息
     */
    @GetMapping("/get/selfOrPublicProblemDetailInfo")
    public Result getSelfOrPublicProblemDetailInfo(@RequestParam("problemId")Integer problemId){
        return problemBankService.getSelfOrPublicProblemDetailInfo(problemId);
    }

}
