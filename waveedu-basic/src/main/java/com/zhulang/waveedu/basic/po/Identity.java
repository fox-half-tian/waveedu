package com.zhulang.waveedu.basic.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户身份表
 *
 * @author 狐狸半面添
 * @create 2023-02-03 0:27
 */
@TableName("basic_identity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Identity {
    /**
     * 数据库自增id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 院校id
     */
    private Integer collegeId;
    /**
     * 身份类型，0代表学生，1代表老师
     */
    private Integer type;
    /**
     * 学号/工号，最大16长度
     */
    private String number;
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
