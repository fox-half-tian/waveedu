package com.zhulang.waveedu.basic.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author 狐狸半面添
 * @create 2023-03-11 17:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginVO {
    /**
     * 用户名
     */
    @NotNull(message = "用户名不允许为空")
    @Length(min = 12, max = 12,message = "用户名格式错误")
    private String username;
    @NotNull(message = "密码不允许为空")
    @Length(min = 12, max = 12,message = "密码格式错误")
    private String password;
}
