package com.zhulang.waveedu.share.po;

import com.baomidou.mybatisplus.annotation.*;
import com.zhulang.waveedu.common.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @author 飒沓流星
 * @create 2023/3/12 0:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("share_site")
public class Site {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String name;

    private Long userId;

    private Integer identity;

    private String introduce;

    private Long typeId;
    @Pattern(regexp = RegexUtils.RegexPatterns.IMAGE_REGEX,message = "图片链接有误")
    private String pictureUrl;

    private String siteUrl;

    private Integer sort;
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
