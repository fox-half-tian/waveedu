package com.zhulang.waveedu.share.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-19 16:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelfApprovedInfoQuery {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 介绍
     */
    private String introduce;

    /**
     * 资源路径
     */
    private String filePath;

    /**
     * 文件名
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
     * 文件的字节大小
     */
    private Long fileByteSize;

    /**
     * 文件的格式大小
     */
    private String fileFormatSize;

    /**
     * 标签
     */
    private String tag;

    /**
     * 申请时间
     */
    private LocalDateTime applyTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 审批时间
     */
    private LocalDateTime approveTime;

    /**
     * 备注
     */
    private String mark;
}
