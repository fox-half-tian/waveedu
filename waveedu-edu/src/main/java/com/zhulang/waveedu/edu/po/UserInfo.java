package com.zhulang.waveedu.edu.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户详情表
 *
 * @author 狐狸半面添
 * @create 2023-01-18 23:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("basic_user_info")
public class UserInfo {
    /**
     * 用户id
     */
    @TableId
    private Long id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 用户头像
     */
    private String icon;
    /**
     * 用户个性签名
     */
    private String signature;
    /**
     * 性别：男或女
     */
    private String sex;
    /**
     * 生日
     */
    private LocalDate born;
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
