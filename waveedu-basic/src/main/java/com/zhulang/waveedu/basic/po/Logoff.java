package com.zhulang.waveedu.basic.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * 注销表（定时任务）
 *
 * @author 狐狸半面添
 * @create 2023-01-19 13:53
 */
@TableName("basic_logoff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Logoff implements Serializable {
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
     * 电话
     */
    private String phone;

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
     * 是否已删除，1表示删除，0表示未删除
     */
    @TableLogic
    private Integer isDeleted;
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
