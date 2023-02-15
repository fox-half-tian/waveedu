package com.zhulang.waveedu.edu.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询课程教学团队的教师信息的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-15 16:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonTchInfoQuery {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户头像
     */
    private String icon;
    /**
     * 用户单位
     */
    private String collegeName;
}
