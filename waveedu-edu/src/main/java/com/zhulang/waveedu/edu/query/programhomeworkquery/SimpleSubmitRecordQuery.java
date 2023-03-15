package com.zhulang.waveedu.edu.query.programhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-15 22:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleSubmitRecordQuery {
    /**
     * 提交id
     */
    private Integer id;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 运行时间
     */
    private Integer runTime;
    /**
     * 运行内存
     */
    private Integer runMemory;
    /**
     * 语言
     */
    private String language;
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
}
