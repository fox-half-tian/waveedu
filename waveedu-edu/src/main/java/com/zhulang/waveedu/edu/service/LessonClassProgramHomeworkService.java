package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.LessonClassProgramHomework;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.ModifyProgramHomeworkVO;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SaveProgramHomeworkVO;

/**
 * <p>
 * 编程作业表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
public interface LessonClassProgramHomeworkService extends IService<LessonClassProgramHomework> {

    /**
     * 添加一份作业
     *
     * @param saveProgramHomeworkVO 班级id + 作业标题
     * @return 作业id
     */
    Result saveHomework(SaveProgramHomeworkVO saveProgramHomeworkVO);

    /**
     * 修改作业信息（标题与截止时间）
     *
     * @param modifyProgramHomeworkVO 标题，截止时间
     * @return 修改状况
     */
    Result modifyInfo(ModifyProgramHomeworkVO modifyProgramHomeworkVO);
}
