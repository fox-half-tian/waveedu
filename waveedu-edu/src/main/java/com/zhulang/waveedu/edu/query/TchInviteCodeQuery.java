package com.zhulang.waveedu.edu.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邀请码与是否禁用 封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-03 22:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TchInviteCodeQuery {
    /**
     * 教学团队邀请码（随机六位）
     */
    private String tchInviteCode;

    /**
     * 是否禁用邀请码，0表示依旧生效，1表示禁用
     */
    private Integer codeIsForbidden;
}
