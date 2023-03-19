package com.zhulang.waveedu.share.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 资源分享申请表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-19
 */
@TableName("share_resource_apply")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceApply implements Serializable {

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
     * 标签
     */
    private String tag;

    /**
     * 备注（可以是拒绝理由）
     */
    private String mark;

    /**
     * 状态：0-未审批，1-审批通过，2-拒绝
     */
    private Integer status;

    /**
     * 审批的管理员Id
     */
    private Long adminId;

    /**
     * 审批通过时间
     */
    private LocalDateTime approveTime;

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
