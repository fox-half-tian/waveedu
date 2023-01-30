package com.zhulang.waveedu.basic.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @author 狐狸半面添
 * @create 2023-01-18 19:13
 */
@TableName("basic_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * 用户id（雪花算法生成）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 密码
     */
    private String password;
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
    /**
     * 用户状态：0表示未冻结，1表示注销冻结
     */
    private Integer status;
    /**
     * 是否已删除，1表示删除，0表示未删除
     */
    @TableLogic
    private Integer isDeleted;
}
