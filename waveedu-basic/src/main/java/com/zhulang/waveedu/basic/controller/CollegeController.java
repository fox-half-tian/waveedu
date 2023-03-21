package com.zhulang.waveedu.basic.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.basic.service.CollegeService;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

/**
 * @author 狐狸半面添
 * @create 2023-02-03 0:53
 */
@RestController
@RequestMapping("/college")
public class CollegeController {
    @Resource
    private CollegeService collegeService;

    /**
     * 模糊查询院校
     *
     * @param name 模糊名
     * @return 院校结果
     */
    @GetMapping("/getLike/name")
    public Result getCollegesByLike(@RequestParam("name") String name) {
        // 当前依旧是走的数据库查询，后面可能考虑为 redis 缓存进行模糊查询
        return collegeService.getCollegesByLike(name);
    }

    /**
     * 获取当前页数据
     *
     * @param pageNum 页数
     * @param recordNum 记录数
     * @return 数据信息列表
     */
    @GetMapping("/getPageRecords")
    public Result getPageRecords(@RequestParam(value = "pageNum")Integer pageNum,
                                 @RequestParam(value = "recordNum")Integer recordNum){
        return collegeService.getPageRecords(pageNum,recordNum);
    }

    /**
     * 修改教师邀请码
     *
     * @param object 院校id
     * @return 新的邀请码
     */
    @PutMapping("/modify/tchCode")
    public Result modifyTchCode(@RequestBody JSONObject object){
        try {
            return collegeService.modifyTchCode(Integer.parseInt(object.getString("collegeId")));
        } catch (NumberFormatException e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "院校id格式错误");
        }
    }

    /**
     * 通过院校name获取院校的详细信息
     *
     * @param name 院校名
     * @return 详细信息
     */
    @GetMapping("/getCollegeInfo")
    public Result getCollegeInfo(@RequestParam("name")String name){
        return collegeService.getCollegeInfo(name);
    }
}
