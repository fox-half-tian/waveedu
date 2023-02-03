package com.zhulang.waveedu.edu.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 课程表
 *
 * @author 狐狸半面添
 * @create 2023-02-03 15:57
 */
@TableName("edu_lesson")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id（雪花算法）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 课程名，最多24长度
     */
    private String name;

    /**
     * 课程介绍，最多255长度
     */
    private String introduce;

    /**
     * 课程封面-链接
     */
    private String cover;

    /**
     * 创建者的用户id
     */
    private Long creatorId;

    /**
     * 教学团队邀请码（随机六位）
     */
    private String tchInviteCode;

    /**
     * 是否禁用邀请码，0表示依旧生效，1表示禁用
     */
    private Integer codeIsForbidden;
    /**
     * 删除标志（0表示未删除，1表示删除）
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