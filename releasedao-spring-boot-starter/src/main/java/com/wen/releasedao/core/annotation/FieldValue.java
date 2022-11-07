package com.wen.releasedao.core.annotation;

import java.lang.annotation.*;

/**
 * 自动填充值<br>
 * 不建议使用，Column统一整合
 *
 * @author calwen
 * @since 2022/10/10
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface FieldValue {
    String value() default "";
}
