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
 * @create 2023/3/12 0:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("share_site_apply")
public class SiteApply {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private Long siteId;

    private String name;

    private Long userId;

    private String introduce;

    private Long typeId;
    @Pattern(regexp = RegexUtils.RegexPatterns.IMAGE_REGEX,message = "图片链接有误")
    private String pictureUrl;

    private String siteUrl;

    private String remark;

    private Long adminId;
    /**
     *0申请中，1通过，2拒绝
     */
    private Integer status;
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
    /**
     * 是否已删除，1表示删除，0表示未删除
     */
    @TableLogic
    private Integer isDeleted;
}
