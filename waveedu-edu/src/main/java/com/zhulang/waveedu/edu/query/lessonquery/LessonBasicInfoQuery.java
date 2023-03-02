package com.zhulang.waveedu.edu.query.lessonquery;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 课程基本信息封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-03 21:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonBasicInfoQuery {
    /**
     * 课程id
     */
    private Long id;

    /**
     * 课程名，最多24长度
     */
    private String name;

    /**
     * 课程介绍，最多512长度
     */
    private String introduce;

    /**
     * 课程封面-链接
     */
    private String cover;

    /**
     * 创建时间
     */
    private LocalDate createTime;

    /**
     * 创建者的用户id
     */
    private Long creatorId;

    /**
     * 创作者的头像
     */
    private String creatorIcon;

}
