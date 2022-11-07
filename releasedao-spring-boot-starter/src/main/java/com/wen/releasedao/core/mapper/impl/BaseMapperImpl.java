package com.wen.releasedao.core.mapper.impl;

import com.mysql.cj.util.StringUtils;
import com.wen.releasedao.core.annotation.*;
import com.wen.releasedao.core.bo.MapperBO;
import com.wen.releasedao.core.enums.CacheUpdateEnum;
import com.wen.releasedao.core.enums.MapperTypeEnum;
import com.wen.releasedao.core.exception.MapperException;
import com.wen.releasedao.core.helper.MapperHelper;
import com.wen.releasedao.core.manager.LoggerManager;
import com.wen.releasedao.core.mapper.BaseMapper;
import com.wen.releasedao.core.vo.PageRequest;
import com.wen.releasedao.core.vo.PageVO;
import com.wen.releasedao.core.wrapper.QueryWrapper;
import com.wen.releasedao.core.wrapper.SetWrapper;
import com.wen.releasedao.util.CastUtil;
import com.wen.releasedao.util.StringUtil;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * BaseMapper实现类
 *
 * @author calwen
 * @since 2022 /7/9
 */
public class BaseMapperImpl implements BaseMapper {
    /**
     * 数据库连接 AOP 自动管理连接
     */
    private Connection conn;


    @Override
    public <T> int getCount(Class<T> eClass, QueryWrapper wrapper) {
        return (int) baseSelect(eClass, wrapper, MapperTypeEnum.SELECT_COUNT);
    }

    @Override
    public <T> int getCount(Class<T> eClass) {
        return (int) baseSelect(eClass, null, MapperTypeEnum.SELECT_COUNT);
    }

    @Override
    public <T> List<T> getList(Class<T> eClass) {
        return (List<T>) baseSelect(eClass, null, MapperTypeEnum.SELECT_ALL);
    }

    @Override
    public <T> List<T> getList(Class<T> eClass, QueryWrapper wrapper) {
        return (List<T>) baseSelect(eClass, wrapper, MapperTypeEnum.SELECT_ALL);

    }

    @Override
    public <T> PageVO<T> page(Class<T> eClass, PageRequest pageRequest) {
        Integer page = pageRequest.getPageNum();
        Integer size = pageRequest.getPageSize();
        int total = getCount(eClass);
        int offset = (page - 1) * size;
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.limit(offset, size);
        List<T> list = getList(eClass, wrapper);
        return PageVO.of(list, page, size, total);
    }

    @Override
    public <T> PageVO<T> page(Class<T> eClass, QueryWrapper wrapper, PageRequest pageRequest) {
        Integer page = pageRequest.getPageNum();
        Integer size = pageRequest.getPageSize();
        int total = getCount(eClass, wrapper);
        int offset = (page - 1) * size;
        wrapper.limit(offset, size);
        List<T> list = getList(eClass, wrapper);
        return PageVO.of(list, page, size, total);
    }

    @Override
    @CacheQuery
    public <T> T get(Class<T> eClass, QueryWrapper wrapper) {
        return (T) baseSelect(eClass, wrapper, MapperTypeEnum.SELECT_ONE);
    }


    @Override
    @CacheQuery
    public <T> T getById(Class<T> eClass, Object id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(MapperHelper.parseId(eClass, false), id);
        return (T) baseSelect(eClass, wrapper, MapperTypeEnum.SELECT_ONE);
    }

    @Override
    public <T> T get(Class<T> eClass) {
        return (T) baseSelect(eClass, null, MapperTypeEnum.SELECT_ONE);
    }

    @Override
    public <T> boolean add(T entity) {
        return baseSave(entity, MapperTypeEnum.INSERT) > 0;
    }


    @Override
    public <T> boolean addBatch(List<T> entityList) {
        return baseBatchSave(entityList, MapperTypeEnum.INSERT) > 0;
    }

    @Override
    @CacheUpdate(CacheUpdateEnum.ENTITY)
    public <T> boolean replace(T entity) {
        return baseSave(entity, MapperTypeEnum.REPLACE) > 0;
    }

    @Override
    @CacheUpdate(CacheUpdateEnum.BATCH)
    public <T> boolean replaceBatch(List<T> entityList) {
        return baseBatchSave(entityList, MapperTypeEnum.REPLACE) > 0;
    }

    @CacheUpdate(CacheUpdateEnum.ENTITY)
    public <T> boolean save(T entity) {
        return replace(entity);
    }

    @Override
    @CacheUpdate(CacheUpdateEnum.BATCH)
    public <T> boolean saveBatch(List<T> entityList) {
        return replaceBatch(entityList);
    }

    @Override
    public <T> List<T> selectSQL(Class<T> eClass, String sql, Object[] values) {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            for (int i = 0; values != null && i < values.length; i++) {
                pst.setObject(i + 1, values[i]);
            }
            ResultSet rs = pst.executeQuery();
            MapperBO<T> mapperBO = MapperHelper.buildMapperBO(eClass, MapperTypeEnum.SELECT_ALL, null);
            return (List<T>) MapperHelper.getEntity(rs, mapperBO);
        } catch (Exception e) {
            throw new MapperException("自定义查询SQL 时异常", e);
        } finally {
            LoggerManager.log(pst, sql, values);
        }
    }

    @Override
    @CacheUpdate(CacheUpdateEnum.WRAPPER)
    public <T> boolean delete(Class<T> eClass, QueryWrapper queryWrapper) {
        PreparedStatement pst = null;
        String sql = "";
        List<Object> values = new ArrayList<>();
        try {
            //删除必须指定条件，否则会全表删除
            if (queryWrapper == null) {
                throw new MapperException("删除必须查询条件，否则会全表删除!!!");
            }
            //条件查询，解析where sql
            HashMap<String, Object> wrapperResult = queryWrapper.getResult();
            String whereSQL = String.valueOf(wrapperResult.get("sql"));
            values = CastUtil.castList(wrapperResult.get("values"), Object.class);
            if (StringUtils.isNullOrEmpty(whereSQL)) {
                throw new MapperException("删除必须查询条件，否则会全表删除!!!");
            }

            String tableName = MapperHelper.parseTableName(eClass);

            sql = "DELETE FROM " + tableName + whereSQL;

            //执行查询
            pst = conn.prepareStatement(sql);

            // 占位符设值
            for (int i = 0; values != null && i < values.size(); i++) {
                pst.setObject(i + 1, values.get(i));
            }
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new MapperException("delete 时异常", e);
        } finally {
            LoggerManager.log(pst, sql, values);
        }
    }


    @Override
    public <T> boolean deleteById(Class<T> eClass, Integer id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(MapperHelper.parseId(eClass, false), id);
        return delete(eClass, wrapper);
    }

    @Override
    @CacheUpdate(CacheUpdateEnum.WRAPPER)
    public <T> boolean update(Class<T> eClass, SetWrapper setWrapper, QueryWrapper queryWrapper) {
        PreparedStatement pst = null;
        String sql = "";
        List<Object> values = new ArrayList<>();
        try {
            //更新必须指定条件
            if (setWrapper == null || queryWrapper == null) {
                System.out.println("更新必须指定set,where");
                return false;
            }
            HashMap<String, Object> wrapperResult;
            //条件查询，解析where sql
            wrapperResult = queryWrapper.getResult();
            String whereSQL = String.valueOf(wrapperResult.get("sql"));
            List<Object> whereValues = CastUtil.castList(wrapperResult.get("values"), Object.class);

            //条件查询，解析set sql
            wrapperResult = setWrapper.getResult();
            String setSql = String.valueOf(wrapperResult.get("sql"));
            List<Object> setValues = CastUtil.castList(wrapperResult.get("values"), Object.class);

            if (StringUtils.isNullOrEmpty(setSql) || StringUtils.isNullOrEmpty(whereSQL)) {
                throw new MapperException("更新必须指定set,where");
            }
            //解析表名
            String tableName = MapperHelper.parseTableName(eClass);

            //拼接sql
            sql = "UPDATE " + tableName + setSql + whereSQL;

            //执行查询
            pst = conn.prepareStatement(sql);

            // 占位符设值
            values = new ArrayList<>();
            values.addAll(setValues);
            values.addAll(whereValues);
            for (int i = 0; i < values.size(); i++) {
                pst.setObject(i + 1, values.get(i));
            }
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new MapperException("更新 时异常", e);
        } finally {
            LoggerManager.log(pst, sql, values);
        }
    }

    @Override
    @CacheUpdate(CacheUpdateEnum.OTHER)
    public <T> boolean exeSQL(String sql, Object[] values) {
        PreparedStatement pst = null;
        try {
            //执行查询
            pst = conn.prepareStatement(sql);
            //设值
            for (int i = 0; values != null && i < values.length; i++) {
                pst.setObject(i + 1, values[i]);
            }
            return pst.execute();

        } catch (SQLException e) {
            throw new MapperException("自定义执行sql 时异常", e);
        } finally {
            LoggerManager.log(pst, sql, values);
        }
    }


    /**
     * 查询数据 base方法
     * 通过type 返回不同的结果类型
     *
     * @param eClass  目标class
     * @param wrapper 查询构造器
     * @param type    查询类型
     */
    private <T> Object baseSelect(Class<T> eClass, QueryWrapper wrapper, MapperTypeEnum type) {
        MapperBO<T> mapperBO = MapperHelper.buildMapperBO(eClass, type, null);
        PreparedStatement pst = null;
        StringBuilder sql = mapperBO.getSql();
        List<Object> values = new ArrayList<>();
        try {
            String tableName = mapperBO.getTableName();
            Map<String, String> resultMap = mapperBO.getResultMap();
            sql.append("SELECT ");
            if (Objects.equals(type, MapperTypeEnum.SELECT_COUNT)) {
                sql.append(" COUNT(*) ");
            } else if (wrapper != null && !StringUtils.isNullOrEmpty(wrapper.getSelectField())) {
                //自定义 查询结果：剔除字段映射map 中 不查询的 字段
                String[] selectFields = wrapper.getSelectField().split(",");
                resultMap = resultMap.entrySet().stream().filter(entry -> {
                    String sqlField = entry.getValue();
                    return Arrays.asList(selectFields).contains(sqlField);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                sql.append(wrapper.getSelectField());
            } else {
                sql.append(" * ");
            }
            sql.append(" FROM ").append(tableName).append(" ");
            //有Wrapper时
            if (wrapper != null) {
                //条件查询
                HashMap<String, Object> wrapperResult = wrapper.getResult();
                String whereSQL = String.valueOf(wrapperResult.get("sql"));
                values = CastUtil.castList(wrapperResult.get("values"), Object.class);
                if (!StringUtils.isNullOrEmpty(whereSQL)) {
                    sql.append(whereSQL);
                }
            }
            //执行查询
            pst = conn.prepareStatement(String.valueOf(sql));

            //需要设值时
            for (int i = 0; values != null && i < values.size(); i++) {
                pst.setObject(i + 1, values.get(i));
            }
            ResultSet rs = pst.executeQuery();
            // 将sql结果集解析 对象或对象集
            return MapperHelper.getEntity(rs, mapperBO);
        } catch (Exception e) {
            throw new MapperException("查询 时异常", e);
        } finally {
            LoggerManager.log(pst, String.valueOf(sql), values);
        }
    }

    /**
     * 保存数据  base方法
     * 提供saveType执行INSERT、REPLACE操作
     *
     * @param entity-数据
     * @param type-保存类型
     */
    private <T> int baseSave(T entity, MapperTypeEnum type) {
        if (entity == null) {
            throw new MapperException("[save] 插入数据不能为 Null ");
        }
        PreparedStatement pst = null;
        Class<T> eClass = (Class<T>) entity.getClass();
        MapperBO<T> mapperBO = MapperHelper.buildMapperBO(eClass, type, entity);
        StringBuilder sql = mapperBO.getSql();
        List<Object> values = new ArrayList<>();
        try {
            this.baseSaveSqlPrefix(mapperBO);
            this.baseSaveSqlQuestion(mapperBO);
            sql.append(" ) ");
            pst = conn.prepareStatement(String.valueOf(sql), PreparedStatement.RETURN_GENERATED_KEYS);
            //填充Entity实现动态更新
            this.fillingEntity(mapperBO);
            this.setBaseSaveValue(mapperBO, pst, values);
            int rs = pst.executeUpdate();
            //更新原实体
            ResultSet GeneratedKeys = pst.getGeneratedKeys();
            if (GeneratedKeys.next()) {
                int id = GeneratedKeys.getInt(1);
                Object newEntity = getById(entity.getClass(), id);
                BeanUtils.copyProperties(newEntity, entity);
            }
            return rs;
        } catch (Exception e) {
            throw new MapperException(" 保存时发生sql异常 ", e);
        } finally {
            LoggerManager.log(pst, String.valueOf(sql), values);
        }
    }


    /**
     * 批量保存
     */
    private <T> int baseBatchSave(List<T> entityList, MapperTypeEnum type) {
        PreparedStatement pst = null;
        StringBuilder sql = new StringBuilder();
        List<Object> values = new ArrayList<>();
        try {
            Class<T> eClass = (Class<T>) entityList.get(0).getClass();
            int listSize = entityList.size();
            MapperBO<T> mapperBO = MapperHelper.buildMapperBO(eClass, type, null);
            this.baseSaveSqlPrefix(mapperBO);
            for (int i = 0; i < listSize; i++) {
                this.baseSaveSqlQuestion(mapperBO);
                sql.append(" ) ,");
            }
            sql.deleteCharAt(sql.length() - 1);
            pst = conn.prepareStatement(String.valueOf(sql));
            this.setBaseSaveValue(mapperBO, pst, values);
            return pst.executeUpdate();
        } catch (SQLException e) {
            throw new MapperException("批量保存 时异常", e);
        } finally {
            LoggerManager.log(pst, String.valueOf(sql), values);
        }
    }

    private <T> void setBaseSaveValue(MapperBO<T> mapperBO, PreparedStatement pst, List<Object> values) {
        Class<T> eClass = mapperBO.getEClass();
        MapperTypeEnum type = mapperBO.getMapperTypeEnum();
        T entity = mapperBO.getEntity();
        AtomicInteger i = new AtomicInteger(1);
        mapperBO.getResultMap().forEach((k, v) -> {
            try {
                Field f = eClass.getDeclaredField(k);
                f.setAccessible(true);
                Object value = f.get(entity);
                if (value != null) {
                    value = f.get(entity);
                } else if (MapperTypeEnum.INSERT.equals(type)
                        && (f.isAnnotationPresent(CreateTime.class) || f.isAnnotationPresent(UpdateTime.class))) {
                    value = new Date();
                } else if (MapperTypeEnum.REPLACE.equals(type)
                        && f.isAnnotationPresent(UpdateTime.class)) {
                    value = new Date();
                } else if (f.isAnnotationPresent(Column.class)) {
                    Column columnAnno = f.getDeclaredAnnotation(Column.class);
                    String defaultValue = columnAnno.defaultValue();
                    if (!StringUtils.isNullOrEmpty(defaultValue)) {
                        value = parseDefault(f.getClass().getSimpleName(), defaultValue);
                    }
                }
                pst.setObject(i.get(), value);
                values.add(value);
                i.getAndIncrement();
            } catch (Exception e) {
                throw new MapperException("设置预编译 时异常", e);
            }
        });
    }

    private Object parseDefault(String className, String value) {
        switch (className) {
            case "String":
                return value;
            case "Integer":
                return Integer.valueOf(value);
            case "Long":
                return Long.valueOf(value);
            case "Short":
                return Short.valueOf(value);
            case "Byte":
                return Byte.valueOf(value);
            case "Character":
                return value.charAt(0);
            case "Double":
                return Double.valueOf(value);
            case "Float":
                return Float.valueOf(value);
            case "Boolean":
                return Boolean.valueOf(value);
            default:
                throw new MapperException("默认值仅支持基本类型");
        }
    }

    /**
     * baseSave sql前缀拼接
     */
    private <T> void baseSaveSqlPrefix(MapperBO<T> mapperBO) {
        String type = mapperBO.getMapperTypeEnum().name();
        String tableName = mapperBO.getTableName();
        StringBuilder sql = mapperBO.getSql();
        StringUtil.append(sql, " {} INTO {} ( ", type, tableName);
        mapperBO.getResultMap().forEach((k, v) -> {
            StringUtil.append(sql, " `{}` ,", v);
        });
        sql.delete(sql.lastIndexOf(","), sql.length()).append(" ) ").append(" VALUES ");
    }

    /**
     * baseSaveSql 问号拼接
     */
    private <T> void baseSaveSqlQuestion(MapperBO<T> mapperBO) {
        StringBuilder sql = mapperBO.getSql();
        sql.append(" ( ");
        mapperBO.getResultMap().forEach((k, v) -> sql.append(" ? ,"));
        sql.deleteCharAt(sql.length() - 1);
    }

    /**
     * 更新操作时，通过主键将Entity中属性填充
     */
    private <T> void fillingEntity(MapperBO<T> mapperBO) throws IllegalAccessException, NoSuchFieldException {
        T entity = mapperBO.getEntity();
        Class<T> eClass = mapperBO.getEClass();
        MapperTypeEnum type = mapperBO.getMapperTypeEnum();
        //插入不操作
        if (MapperTypeEnum.INSERT.equals(type)) {
            return;
        }
        String fid = MapperHelper.parseId(eClass, true);
        Field field = eClass.getDeclaredField(fid);
        field.setAccessible(true);
        Object oid = field.get(entity);
        //主键值等于null 不操作
        if (oid == null) {
            return;
        }
        Object data = getById(eClass, oid);
        if (data != null) {
            Field[] fields = eClass.getDeclaredFields();
            ArrayList<String> ignoreFields = new ArrayList<>();
            for (Field f : fields) {
                f.setAccessible(true);
                Object o = f.get(entity);
                if (o != null) {
                    ignoreFields.add(f.getName());
                }
            }
            BeanUtils.copyProperties(data, entity, ignoreFields.toArray(new String[0]));
        }
    }
}

/**
 * if (entity != null) {
 * Field f = eClass.getField(k);
 * if (f.get(entity) == null) {
 * FieldValue valueAnn = f.getAnnotation(FieldValue.class);
 * if (valueAnn != null) {
 * StringUtil.append(sql, " `{}` ,", valueAnn.value());
 * sql.delete(sql.lastIndexOf(","), sql.length()).append(" ) ").append(" VALUES ");
 * return;
 * }
 * }
 * String value = valueAnn.value();
 * switch (f.getClass().getSimpleName()) {
 * case "String":
 * break;
 * case "Integer":
 * break;
 * case "Long":
 * break;
 * case "Short":
 * break;
 * case "Byte":
 * break;
 * case "Character":
 * break;
 * case "Double":
 * break;
 * case "Float":
 * break;
 * case "Boolean":
 * break;
 * }
 */