package com.zhulang.waveedu.basic.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-11
 */
@TableName("basic_admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键（雪花算法生成）
     */
    private Long id;

    /**
     * 用户名（root生成）,12位
     */
    private String username;

    /**
     * 密码，12位
     */
    private String password;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String icon;

    /**
     * 权限：0-超级管理员，1-普通管理员
     */
    private Integer role;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDeleted;

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
