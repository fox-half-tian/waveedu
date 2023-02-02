package com.zhulang.waveedu.basic.controller;

import com.zhulang.waveedu.basic.service.CollegeService;
import com.zhulang.waveedu.common.entity.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/getLike/{name}")
    public Result getCollegesByLike(@NotBlank(message = "不允许为空")
                                    @PathVariable("name") String name) {
        // 当前依旧是走的数据库查询，后面可能考虑为 redis 缓存进行模糊查询
        return collegeService.getCollegesByLike(name);
    }
}
