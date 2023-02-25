package com.zhulang.waveedu.edu.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询加入班级的列表信息
 *
 * @author 狐狸半面添
 * @create 2023-02-25 12:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinClassInfoQuery {
    /**
     * 班级id
     */
    private Long classId;
    /**
     * 班级名
     */
    private String className;
    /**
     * 是否结课：0代表未结课，1代表结课
     */
    private Integer isEndClass;
    /**
     * 课程id
     */
    private Long lessonId;
    /**
     * 课程封面
     */
    private String lessonCover;
    /**
     * 课程名
     */
    private String lessonName;
}
