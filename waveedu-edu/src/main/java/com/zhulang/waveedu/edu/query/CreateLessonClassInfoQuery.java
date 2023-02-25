package com.zhulang.waveedu.edu.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建的课程班级的信息列表封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-26 2:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLessonClassInfoQuery {
    /**
     * 班级Id
     */
    private Long classId;
    /**
     * 班级名
     */
    private String className;
    /**
     * 班级人数
     */
    private Integer classNum;
    /**
     * 课程封面
     */
    private String lessonCover;
    /**
     * 课程名
     */
    private String lessonName;
    /**
     * 课程id
     */
    private String lessonId;
}
