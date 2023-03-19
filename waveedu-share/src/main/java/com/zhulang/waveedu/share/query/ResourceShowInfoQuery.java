package com.zhulang.waveedu.share.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-19 17:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceShowInfoQuery {
    /**
     * 自增id
     */
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
     * 上传的用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String userIcon;

    /**
     * 介绍
     */
    private String introduce;

    /**
     * 下载次数
     */
    private Integer downloadCount;

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
     * 创建时间
     */
    private String createTime;
}
