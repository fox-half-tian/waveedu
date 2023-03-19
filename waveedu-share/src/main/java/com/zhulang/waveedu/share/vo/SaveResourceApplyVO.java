package com.zhulang.waveedu.share.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author 狐狸半面添
 * @create 2023-03-19 11:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveResourceApplyVO {
    /**
     * 标题
     */
    @NotBlank(message = "标题不允许为空")
    @Length(min = 6,max = 255,message = "标题应在6-255字间")
    private String title;

    /**
     * 介绍
     */
    @NotBlank(message = "介绍不允许为空")
    @Length(max = 512,message = "介绍最多512字")
    private String introduce;

    /**
     * 文件名
     */
    @NotBlank(message = "文件名不允许为空")
    @Length(min = 1,max = 255,message = "文件名应在1-255字间")
    private String fileName;

    /**
     * 标签
     */
    private String tag;

    /**
     * 文件信息（加密处理）
     */
    @NotBlank(message = "文件信息不能为空")
    private String fileInfo;
}
