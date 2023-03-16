package com.zhulang.waveedu.edu.query.programhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-13 18:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TchSimpleHomeworkProblemInfoQuery {
    /**
     * 问题id
     */
    private Integer id;
    /**
     * 问题标题
     */
    private String title;
    /**
     * 问题难度
     */
    private Integer difficulty;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
