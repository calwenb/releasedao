package com.wen.releasedao.core.annotation;

import java.lang.annotation.*;

/**
 * 为实体类属性指定数据库字段名
 * 若未指定，默认驼峰式转蛇形式
 *
 * @author calwen
 * @since 2022/7/9
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    /**
     * 数据库中的列名
     */
    String value() default "";

    /**
     * 是否使用
     */
    boolean exist() default true;

    /**
     * 默认值
     */
    String defaultValue() default "";
}
