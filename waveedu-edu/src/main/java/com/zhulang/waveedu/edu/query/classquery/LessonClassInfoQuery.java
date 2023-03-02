package com.zhulang.waveedu.edu.query.classquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 查询课程班级信息的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-27 13:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonClassInfoQuery {
    /**
     * 班级Id
     */
    private Long id;
    /**
     * 班级名
     */
    private String name;
    /**
     * 学生人数
     */
    private Integer num;
    /**
     * 是否结课
     */
    private Integer isEndClass;
    /**
     * 是否禁止加入
     */
    private Integer isForbidJoin;
    /**
     * 创建时间
     */
    private LocalDate createTime;
    /**
     * 创建者姓名
     */
    private String creatorName;
    /**
     * 邀请码
     */
    private String inviteCode;
}
