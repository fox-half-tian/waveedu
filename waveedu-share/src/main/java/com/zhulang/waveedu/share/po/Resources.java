package com.zhulang.waveedu.share.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 资源分析表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-19
 */
@TableName("share_resource")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resources implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 上传用户id
     */
    private Long userId;

    /**
     * 介绍
     */
    private String introduce;

    /**
     * 资源路径
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
     * 文件的字节大小
     */
    private Long fileByteSize;

    /**
     * 文件的格式大小
     */
    private String fileFormatSize;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 标签
     */
    private String tag;

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
