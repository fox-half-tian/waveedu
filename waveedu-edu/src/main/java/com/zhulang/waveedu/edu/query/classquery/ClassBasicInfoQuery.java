package com.zhulang.waveedu.edu.query.classquery;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 查看课程班级的基本信息的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-24 23:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassBasicInfoQuery {

    /**
     * 创建者的用户id
     */
    private Long creatorId;

    /**
     * 班级名，不可超过24长度
     */
    private String name;

    /**
     * 班级封面
     */
    private String cover;

    /**
     * 学生总数
     */
    private Integer num;

    /**
     * 是否开启结课，成绩将不再变化，0表示不开启，1表示开启
     */
    private Integer isEndClass;

    /**
     * 所属课程id
     */
    private Long lessonId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
