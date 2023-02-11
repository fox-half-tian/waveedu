package com.zhulang.waveedu.edu.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 课程资料上传后，返回给前端的信息封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-11 21:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonFileDTO {

    /**
     * 课程id
     */
    private Long lessonId;

    /**
     * 文件名，不可超过255长度
     */
    private String fileName;

    /**
     * 存储路径
     */
    private String filePath;

    /**
     * 文件类型：文本，图片，音频，视频，其它
     */
    private String fileType;

    /**
     * 文件格式
     */
    private String fileFormat;

    /**
     * 上传者用户名
     */
    private String userName;

    /**
     * 文件的格式大小
     */
    private String fileFormatSize;

    /**
     * 文件下载次数
     */
    private Integer downloadCount;

    /**
     * 创建时间（上传时间）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
