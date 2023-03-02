package com.zhulang.waveedu.edu.query.lessonquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 缓存到redis中的课程信息封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-14 14:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonCacheQuery {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 课程名，最多24长度
     */
    private String name;

    /**
     * 课程介绍，最多255长度
     */
    private String introduce;

    /**
     * 课程封面-链接
     */
    private String cover;

    /**
     * 创建者的用户id
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
