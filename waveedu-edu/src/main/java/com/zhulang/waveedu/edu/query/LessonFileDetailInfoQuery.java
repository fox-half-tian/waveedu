package com.zhulang.waveedu.edu.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 查询详细课程文件信息列表的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-14 21:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonFileDetailInfoQuery {
    /**
     * 主键id（雪花算法）
     */
    private Long id;

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
     * 上传者姓名
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
    private LocalDateTime createTime;
}
