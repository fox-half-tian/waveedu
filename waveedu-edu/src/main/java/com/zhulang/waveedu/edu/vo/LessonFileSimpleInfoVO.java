package com.zhulang.waveedu.edu.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 查询简单课程文件信息列表的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-14 20:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonFileSimpleInfoVO {
    /**
     * 主键id（雪花算法）
     */
    private Long id;

    /**
     * 文件名，不可超过255长度
     */
    private String fileName;

    /**
     * 文件类型：文本，图片，音频，视频，其它
     */
    private String fileType;

    /**
     * 文件格式
     */
    private String fileFormat;

    /**
     * 文件的格式大小
     */
    private String fileFormatSize;

    /**
     * 创建时间（上传时间）
     */
    private LocalDateTime createTime;
}
