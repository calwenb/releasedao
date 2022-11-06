package com.wen.releasedao.core.bo;

import com.wen.releasedao.core.enums.MapperTypeEnum;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author calwen
 * @since 2022/11/6
 */
@Data
public class MapperBO<T> {
    /**
     *
     */
    private Class<T> eClass;
    private T entity;
    /**
     *
     */
    private String classId;
    /**
     *
     */
    private String dbId;
    /**
     *
     */
    private String tableName;
    /**
     *
     */
    private Field[] fields;

    private Constructor<T> classCon;
    /**
     *
     */
    private StringBuilder sql;
    /**
     *
     */
    private MapperTypeEnum mapperTypeEnum;
    /**
     *
     */
    private Map<String, String> resultMap;
    private Map<String, String> needMap;
}
