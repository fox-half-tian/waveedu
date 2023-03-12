package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.po.ProgramHomeworkProblem;
import com.zhulang.waveedu.edu.dao.ProgramHomeworkProblemMapper;
import com.zhulang.waveedu.edu.service.LessonClassProgramHomeworkService;
import com.zhulang.waveedu.edu.service.ProgramHomeworkProblemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SaveProblemVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.naming.spi.ObjectFactory;

/**
 * <p>
 * 编程作业表的题目表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
@Service
public class ProgramHomeworkProblemServiceImpl extends ServiceImpl<ProgramHomeworkProblemMapper, ProgramHomeworkProblem> implements ProgramHomeworkProblemService {
    @Resource
    private ProgramHomeworkProblemMapper programHomeworkProblemMapper;
    @Resource
    private LessonClassProgramHomeworkService lessonClassProgramHomeworkService;

    @Override
    public Result saveProblem(SaveProblemVO saveProblemVO) {
        Long userId = UserHolderUtils.getUserId();
        // 1.校验身份与作业状况
        Integer status = lessonClassProgramHomeworkService.getIsPublishByHomeworkIdAndCreatorId(saveProblemVO.getHomeworkId(), userId);
        if (status==null){
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(),HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        if (status!=0){
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(),"只允许修改未发布的作业信息");
        }
        // 2.属性设置
        ProgramHomeworkProblem problem = BeanUtil.copyProperties(saveProblemVO, ProgramHomeworkProblem.class);
        // 3.保存
        programHomeworkProblemMapper.insert(problem);
        // 4.返回问题id
        return Result.ok(problem.getId());
    }
}
