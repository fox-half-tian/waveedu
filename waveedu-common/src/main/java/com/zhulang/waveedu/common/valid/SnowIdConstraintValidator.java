package com.zhulang.waveedu.common.valid;

import com.zhulang.waveedu.common.util.RegexUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义雪花算法id校验器
 *
 * @author 狐狸半面添
 * @create 2023-02-11 20:22
 */
public class SnowIdConstraintValidator implements ConstraintValidator<SnowIdValidate,Long> {
    /**
     * 如果返回 true ，表示验证成功 -> 通过
     * 如果返回 false，表示验证失败 -> 不通过
     */
    @Override
    public boolean isValid(Long snowId, ConstraintValidatorContext constraintValidatorContext) {
        return !RegexUtils.isSnowIdInvalid(snowId);
    }
}
