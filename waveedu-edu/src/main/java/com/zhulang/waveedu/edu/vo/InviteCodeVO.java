package com.zhulang.waveedu.edu.vo;

import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 邀请码的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-17 2:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteCodeVO {
    /**
     * 主键Id
     */
    @SnowIdValidate(message = "无效邀请码")
    private Long id;
    /**
     * 真实邀请码
     */
    @NotNull(message = "无效邀请码")
    @Pattern(regexp = RegexUtils.RegexPatterns.INVITE_CODE_REGEX, message = "无效邀请码")
    private String inviteCode;
}
