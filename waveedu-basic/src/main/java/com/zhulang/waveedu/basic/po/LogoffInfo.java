package com.zhulang.waveedu.basic.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 注销信息表
 *
 * @author 狐狸半面添
 * @create 2023-02-03 0:35
 */
@TableName("basic_logoff_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoffInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id（数据库自增策略）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 注销原因，最多255个字符
     */
    private String reason;

    /**
     * 注销时间
     */
    private LocalDateTime logoffTime;
    /**
     * 截止时间
     */
    private LocalDateTime endTime;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
