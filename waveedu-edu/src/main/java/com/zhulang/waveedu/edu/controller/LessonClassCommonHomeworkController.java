package com.zhulang.waveedu.edu.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.LessonClassCommonHomeworkService;
import com.zhulang.waveedu.edu.vo.homeworkvo.ModifyCommonHomeworkVo;
import com.zhulang.waveedu.edu.vo.homeworkvo.PublishCommonHomeworkVO;
import com.zhulang.waveedu.edu.vo.homeworkvo.SaveCommonHomeworkVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 课程班级的普通作业表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-27
 */
@RestController
@RequestMapping("/lesson-class-common-homework")
public class LessonClassCommonHomeworkController {
    @Resource
    private LessonClassCommonHomeworkService lessonClassCommonHomeworkService;

    /**
     * 保存一份作业（未发布）
     *
     * @param saveCommonHomeworkVO 作业信息
     * @return 作业id
     */
    @PostMapping("/saveHomework")
    public Result saveHomework(@Validated @RequestBody SaveCommonHomeworkVO saveCommonHomeworkVO) {
        return lessonClassCommonHomeworkService.saveHomework(saveCommonHomeworkVO);
    }

    /**
     * 发布作业
     *
     * @param publishCommonHomeworkVO 作业id+是否定时发布+定时发布时间
     * @return 发布状况
     */
    @PostMapping("/publish")
    public Result publish(@Validated @RequestBody PublishCommonHomeworkVO publishCommonHomeworkVO) {
        return lessonClassCommonHomeworkService.publish(publishCommonHomeworkVO);
    }

    /**
     * 修改普通作业信息，只允许创建者进行操作
     * 允许修改：作业标题，难度，截止时间
     *
     * @param modifyCommonHomeworkVo 作业id，作业标题，难度，截止时间
     * @return 修改状况
     */
    @PutMapping("/modify/info")
    public Result modifyInfo(@Validated @RequestBody ModifyCommonHomeworkVo modifyCommonHomeworkVo) {
        return lessonClassCommonHomeworkService.modifyInfo(modifyCommonHomeworkVo);
    }

    /**
     * 取消预发布，状态变为未发布，只允许创建者操作
     *
     * @param object 作业Id
     * @return 修改状况
     */
    @PutMapping("/modify/cancelPreparePublish")
    public Result modifyCancelPreparePublish(@RequestBody JSONObject object) {
        try {
            return lessonClassCommonHomeworkService.modifyCancelPreparePublish(Integer.parseInt(object.getString("homeworkId")));
        } catch (NumberFormatException e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "作业id格式错误");
        }
    }

    /**
     * 创建者获取班级作业的详细信息列表，班级创建者可以调用这个接口
     * 可传 isPublish
     *
     * @param classId   班级id
     * @param isPublish 发布状态
     * @return 作业信息，按照时间从近到远进行了排序
     */
    @GetMapping("/get/tch/homeworkDetailListInfo")
    public Result getTchHomeworkDetailListInfo(@RequestParam("classId") Long classId,
                                               @RequestParam(value = "isPublish", required = false) Integer isPublish) {
        return lessonClassCommonHomeworkService.getTchHomeworkDetailListInfo(classId, isPublish);
    }

    /**
     * 创建者获取班级作业的简单信息，班级创建者可以调用这个接口
     * 可传 isPublish
     *
     * @param classId   班级id
     * @param isPublish 发布状态
     * @return 作业信息：作业id，作业标题，作业状态，按照时间从近到远进行了排序
     */
    @GetMapping("/get/tch/homeworkSimpleListInfo")
    public Result getTchHomeworkSimpleListInfo(@RequestParam("classId") Long classId,
                                               @RequestParam(value = "isPublish", required = false) Integer isPublish) {
        return lessonClassCommonHomeworkService.getTchHomeworkSimpleListInfo(classId, isPublish);
    }

    /**
     * 创建者获取班级一个作业的详细信息，班级创建者可以调用这个接口
     *
     * @param homeworkId 作业id
     * @return 作业信息
     */
    @GetMapping("/get/tch/homeworkDetailInfo")
    public Result getTchHomeworkDetailInfo(@RequestParam("homeworkId") Integer homeworkId) {
        return lessonClassCommonHomeworkService.getTchHomeworkDetailInfo(homeworkId);
    }

    /**
     * 学生获取班级作业的简单信息列表，班级的学生可以调用这个接口
     *
     * @param classId 班级id
     * @return 作业信息：作业id，作业标题，作业状态，截止时间，按照时间从近到远进行了排序
     */
    @GetMapping("/get/stu/homeworkSimpleListInfo")
    public Result getStuHomeworkSimpleListInfo(@RequestParam("classId") Long classId) {
        return lessonClassCommonHomeworkService.getStuHomeworkSimpleListInfo(classId);
    }

    /**
     * 学生获取班级一个作业的详细信息
     * 类型，标题，难度，满分，学生分数，开始时间，结束时间，状态,教师评价
     *
     * @param homeworkId 班级id
     * @return 作业详细信息
     */
    @GetMapping("/get/stu/homeworkDetailInfo")
    public Result getStuHomeworkDetailInfo(@RequestParam("homeworkId") Integer homeworkId) {
        return lessonClassCommonHomeworkService.getStuHomeworkDetailInfo(homeworkId);
    }

    /**
     * 删除一份作业
     *
     * @param homeworkId 作业id
     * @return 删除状况
     */
    @DeleteMapping("/remove")
    public Result removeHomework(@RequestParam("homeworkId") Integer homeworkId) {
        return lessonClassCommonHomeworkService.removeHomework(homeworkId);
    }
}
