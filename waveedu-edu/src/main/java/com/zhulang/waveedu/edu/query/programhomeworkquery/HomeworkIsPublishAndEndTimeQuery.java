package com.zhulang.waveedu.edu.query.programhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-15 15:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkIsPublishAndEndTimeQuery {
    /**
     * 发布状况
     */
    private Integer isPublish;
    /**
     * 截止时间
     */
    private LocalDateTime endTime;
}
