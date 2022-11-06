package com.wen.releasedao.core.enums;

/**
 * 更新操作类型枚举
 *
 * @author calwen
 * @since 2022/8/27
 */
public enum MapperTypeEnum {
    SELECT,
    /**
     * list 查询
     */
    SELECT_ALL,
    /**
     * 一个
     */
    SELECT_ONE,
    /**
     * 计数
     */
    SELECT_COUNT,
    DELETE,
    UPDATE,
    /**
     * 插入
     */
    INSERT,
    /**
     * 替换
     */
    REPLACE,
}
