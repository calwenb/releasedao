package com.wen.releasedao.core.enums;

/**
 * select的查询类型枚举类<br>
 * 不建议使用，用 MapperTypeEnum 代替
 *
 * @author calwen
 * @since 2022/7/9
 */
@Deprecated
public enum SelectTypeEnum {
    /**
     * list 查询
     */
    ALL,
    /**
     * 一个
     */
    ONE,
    /**
     * 计数
     */
    COUNT
}
