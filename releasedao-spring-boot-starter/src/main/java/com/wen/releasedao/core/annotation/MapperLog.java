package com.wen.releasedao.core.annotation;

import java.lang.annotation.*;

/**
 * 弃用
 *
 * @author calwen
 * @since 2022/8/15
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface MapperLog {
    boolean value() default true;
}
