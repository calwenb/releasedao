package com.wen.releasedao.core.annotation;

import java.lang.annotation.*;
/**
 * 联表查询注解，配置联合信息
 * @author calwen
 * @since 2022/10/10
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldJoin {
    String value() default "";
    String point () default "";
}
