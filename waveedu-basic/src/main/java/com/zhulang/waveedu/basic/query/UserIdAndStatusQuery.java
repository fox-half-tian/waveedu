package com.zhulang.waveedu.basic.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-01-19 13:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserIdAndStatusQuery {
    /**
     * 用户主键id
     */
    private Long id;
    /**
     * 用户状态
     */
    private Integer status;
}
