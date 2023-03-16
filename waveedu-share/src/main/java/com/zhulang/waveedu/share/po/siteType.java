package com.zhulang.waveedu.share.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 飒沓流星
 * @create 2023/3/12 0:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("share_site_type")
public class siteType {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer sort;

    private Long topicId;
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
