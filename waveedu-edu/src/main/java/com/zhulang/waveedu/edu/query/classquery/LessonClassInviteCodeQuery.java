package com.zhulang.waveedu.edu.query.classquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程班级邀请码与是否禁用 封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-25 1:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonClassInviteCodeQuery {
    /**
     * 邀请码（随机六位）
     */
    private String inviteCode;

    /**
     * 是否禁用加入班级，0表示依旧生效，1表示禁用
     */
    private Integer isForbidJoin;

    /**
     * 课程id
     */
    private Long lessonId;
}
