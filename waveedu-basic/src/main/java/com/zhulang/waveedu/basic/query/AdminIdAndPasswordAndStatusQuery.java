package com.zhulang.waveedu.basic.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-01-19 21:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminIdAndPasswordAndStatusQuery {
    /**
     * 管理员主键id
     */
    private Long id;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户状态
     */
    private Integer status;
}
