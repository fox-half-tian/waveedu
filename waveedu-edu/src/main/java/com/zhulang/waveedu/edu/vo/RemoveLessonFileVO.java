package com.zhulang.waveedu.edu.vo;

import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 移除课程资料的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-11 23:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveLessonFileVO {
    /**
     * 课程id
     */
    @NotNull
    @SnowIdValidate(message = "课程id无效")
    private Long lessonId;

    /**
     * 课程资料id
     */
    @NotNull
    @SnowIdValidate(message = "课程资料id无效")
    private Long lessonFileId;
}
