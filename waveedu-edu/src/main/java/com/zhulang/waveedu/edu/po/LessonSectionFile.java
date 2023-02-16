package com.zhulang.waveedu.edu.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程小节的文件表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-16
 */
@TableName("edu_lesson_section_file")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonSectionFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 小节id
     */
    private Integer sectionId;

    /**
     * 文件类型：0代表视频，1代表资料
     */
    private Boolean type;

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
     * 上传者id
     */
    private Long userId;

    /**
     * 文件的字节大小
     */
    private Long fileByteSize;

    /**
     * 文件的格式大小
     */
    private String fileFormatSize;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
