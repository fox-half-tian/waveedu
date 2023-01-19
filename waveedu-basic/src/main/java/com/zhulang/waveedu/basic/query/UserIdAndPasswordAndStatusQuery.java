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
public class UserIdAndPasswordAndStatusQuery {
    /**
     * 用户主键id
     */
    private Long id;
    /**
     * 加密的密码
     */
    private String password;
    /**
     * 用户状态
     */
    private Integer status;
}
