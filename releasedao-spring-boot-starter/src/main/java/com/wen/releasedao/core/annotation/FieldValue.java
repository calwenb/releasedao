package com.wen.releasedao.core.annotation;

import java.lang.annotation.*;
/**
 * 自动填充值
 * @author calwen
 * @since 2022/10/10
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldValue {
    String value() default "null";
}
