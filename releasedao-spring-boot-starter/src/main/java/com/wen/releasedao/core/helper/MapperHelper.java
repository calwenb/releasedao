package com.wen.releasedao.core.helper;


import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mysql.cj.util.StringUtils;
import com.wen.releasedao.core.annotation.FieldJoin;
import com.wen.releasedao.core.annotation.FieldName;
import com.wen.releasedao.core.annotation.FieldId;
import com.wen.releasedao.core.annotation.TableName;
import com.wen.releasedao.core.bo.MapperBO;
import com.wen.releasedao.core.enums.MapperTypeEnum;
import com.wen.releasedao.core.exception.MapperException;
import com.wen.releasedao.util.SqlUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Mapper帮助者<br>
 * 为BaseMapper提供支持
 *
 * @author calwen
 * @since 2022/7/9
 */
public class MapperHelper {

    public static <T> MapperBO<T> buildMapperBO(Class<T> eClass, MapperTypeEnum type, T entity) {
        String classId = parseId(eClass, true);
        String dbId = parseId(eClass, false);
        String tableName = parseTableName(eClass);
        Field[] fields = parseField(eClass);
        Map<String, String> resultMap = parseResultMap(eClass);
        Constructor<T> constructor = parseConstructor(eClass);
        MapperBO<T> bo = new MapperBO<>();
        bo.setEClass(eClass);
        bo.setEntity(entity);
        bo.setClassId(classId);
        bo.setDbId(dbId);
        bo.setTableName(tableName);
        bo.setFields(fields);
        bo.setClassCon(constructor);
        bo.setSql(new StringBuilder());
        bo.setMapperTypeEnum(type);
        bo.setResultMap(resultMap);
        bo.setNeedMap(new HashMap<>());
        return bo;
    }

    /**
     * 解析字段
     * 过滤 @FieldName(exist = false)
     */
    public static <T> Field[] parseField(Class<T> eClass) {
        Field[] fields = eClass.getDeclaredFields();
        fields = Arrays.stream(fields).filter((f) -> {
            f.setAccessible(true);
            FieldName annotation = f.getDeclaredAnnotation(FieldName.class);
            if (annotation != null) {
                return annotation.exist();
            }
            return true;
        }).toArray(Field[]::new);
        return fields;
    }


    /**
     * 解析 主键id
     *
     * @param property-是否 返回类属性名，否则数据库字段
     * @return 主键
     */
    public static <T> String parseId(Class<T> eClass, boolean property) {
        Field[] fields = eClass.getDeclaredFields();

        //找到属性上 @FieldId 注解
        for (Field f : fields) {
            f.setAccessible(true);
            FieldId idFieldAnn = f.getDeclaredAnnotation(FieldId.class);
            if (idFieldAnn == null) {
                continue;
            }
            //获取类上的属性名，不做处理
            if (property) {
                return f.getName();
            }
            if (!StringUtils.isNullOrEmpty(idFieldAnn.value())) {
                return idFieldAnn.value();
            } else {
                return SqlUtil.camelToSnake(f.getName());
            }
        }
        //未指定，默认第一个
        if (property) {
            return fields[0].getName();
        } else {
            return SqlUtil.camelToSnake(fields[0].getName());
        }
    }


    /**
     * 解析表名
     * &#064;TableName("name")
     */
    public static <T> String parseTableName(Class<T> eClass) {
        //反射获取目标信息
        TableName tableNameAnno = eClass.getDeclaredAnnotation(TableName.class);
        String className = eClass.getSimpleName();
        //确定表名
        String tableName;
        if (tableNameAnno != null) {
            tableName = tableNameAnno.value();
        } else {
            //是否驼峰转蛇形
            tableName = SqlUtil.camelToSnake(className);
        }
        return tableName;
    }

    /**
     * 解析 对象字段与sql字段映射
     */
    public static <T> Map<String, String> parseResultMap(Class<T> eClass) {
        Field[] fields = eClass.getDeclaredFields();
        Map<String, String> map = new LinkedHashMap<>(fields.length);
        Arrays.stream(fields).filter((f) -> {
            f.setAccessible(true);
            FieldName annotation = f.getDeclaredAnnotation(FieldName.class);
            if (annotation != null) {
                return annotation.exist();
            }
            return true;
        }).forEach(f -> {
            String objectField = f.getName();
            String sqlField;
            FieldName anno = f.getDeclaredAnnotation(FieldName.class);
            if (anno != null && !StringUtils.isNullOrEmpty(anno.value())) {
                sqlField = anno.value();
            } else {
                sqlField = SqlUtil.camelToSnake(objectField);
            }
            map.put(objectField, sqlField);
        });
        return map;

    }

    /**
     * 解析生成对象
     */
    public static <T> T parseEntity(ResultSet rs, MapperBO<T> mapperBO) {
        Field[] fields = mapperBO.getFields();
        Map<String, String> resultMap = mapperBO.getResultMap();
        Constructor<T> classCon = mapperBO.getClassCon();
        try {
            Object[] fieldsVal = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                //从 字段映射中获取 sql字段
                Field field = fields[i];
                field.setAccessible(true);
                FieldJoin joinAnno = field.getDeclaredAnnotation(FieldJoin.class);
                if (joinAnno != null) {
                    Class<?> cClass = field.getClass();
                    MapperBO<?> mapperBO1 = buildMapperBO(cClass, MapperTypeEnum.SELECT, null);
                    Object child = parseEntity(rs, mapperBO1);
                    fieldsVal[i] = child;
                    continue;
                }
                String fieldName = field.getName();
                String sqlField = resultMap.get(fieldName);
                if (sqlField == null) {
                    continue;
                }
                fieldsVal[i] = rs.getObject(sqlField);
                //LocalDateTime转Date
                if (fieldsVal[i] != null && fieldsVal[i].getClass().equals(LocalDateTime.class)) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());
                    fieldsVal[i] = Date.from(objectMapper.convertValue(fieldsVal[i], LocalDateTime.class).atZone(ZoneId.systemDefault()).toInstant());
                }
            }
            return classCon.newInstance(fieldsVal);
        } catch (Exception e) {
            throw new MapperException("解析生成对象时异常", e);
        }
    }


    /**
     * 获得查询结果，解析成对象
     */
    public static <T> Object getEntity(ResultSet rs, MapperBO<T> mapperBO) {
        MapperTypeEnum type = mapperBO.getMapperTypeEnum();
        try {
            List<T> list = new ArrayList<>();
            //返回数据解析实体
            while (rs.next()) {
                // count(*)
                if (Objects.equals(type, MapperTypeEnum.SELECT_COUNT)) {
                    return rs.getInt(1);
                }
                T entity = parseEntity(rs, mapperBO);
                // 单个实体
                if (type == MapperTypeEnum.SELECT_ONE) {
                    return entity;
                }
                list.add(entity);
            }
            // 集合返回则不能返回null
            if (MapperTypeEnum.SELECT_ALL.equals(type)) {
                return list;
            }
            return list.isEmpty() ? null : list;
        } catch (Exception e) {
            throw new MapperException("解析生成目标时异常", e);
        }
    }

    /**
     * 获取类构造器
     */
    public static <T> Constructor<T> parseConstructor(Class<T> eClass) {
        try {
            Field[] fields = eClass.getDeclaredFields();
            //获取全部属性的类
            Class<?>[] classes = new Class[fields.length];
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                classes[i] = fields[i].getType();
            }
            return eClass.getDeclaredConstructor(classes);
        } catch (NoSuchMethodException e) {
            throw new MapperException("获取构建器时异常", e);
        }
    }


}
