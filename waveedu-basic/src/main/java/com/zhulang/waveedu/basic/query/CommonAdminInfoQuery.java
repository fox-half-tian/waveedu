package com.zhulang.waveedu.basic.query;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-11 19:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonAdminInfoQuery {
    /**
     * 主键（雪花算法生成）
     */
    private Long id;

    /**
     * 用户名（root生成）,16位
     */
    private String username;

    /**
     * 密码，24位
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
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
