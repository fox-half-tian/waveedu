package com.zhulang.waveedu.edu.query.lessonquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 创建的课程的简单信息查询封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-12 0:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLessonSimpleInfoQuery {
    /**
     * 课程id
     */
    private Long id;
    /**
     * 课程名
     */
    private String name;
    /**
     * 课程封面
     */
    private String cover;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
