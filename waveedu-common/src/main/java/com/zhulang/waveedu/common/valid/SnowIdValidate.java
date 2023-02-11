package com.zhulang.waveedu.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义雪花算法id校验注解
 *
 * @author 狐狸半面添
 * @create 2023-02-11 20:20
 */
@Documented
@Constraint(validatedBy = {SnowIdConstraintValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface SnowIdValidate {
    String message() default "{com.zhulang.waveedu.common.valid.SnowIdValidate.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
