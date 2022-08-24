package com.wen.releasedao.core.mapper.impl;

import com.mysql.cj.util.StringUtils;
import com.wen.releasedao.core.annotation.CacheQuery;
import com.wen.releasedao.core.annotation.CacheUpdate;
import com.wen.releasedao.core.enums.CacheUpdateEnum;
import com.wen.releasedao.core.enums.SelectTypeEnum;
import com.wen.releasedao.core.exception.SqlException;
import com.wen.releasedao.core.mapper.BaseMapper;
import com.wen.releasedao.core.util.MapperUtil;
import com.wen.releasedao.core.wrapper.QueryWrapper;
import com.wen.releasedao.core.wrapper.SetWrapper;
import com.wen.releasedao.util.CastUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
//@SuppressWarnings("unchecked")
public class BaseMapperImpl implements BaseMapper {
    /**
     * 数据库连接 AOP自动管理连接
     */
    private Connection conn;
    /**
     * PreparedStatement sql 日志
     * AOP输出日志
     */
    public String pstLog;

    @Override
    public <T> int getCount(Class<T> tClass, QueryWrapper wrapper) {
        return (int) baseSelect(tClass, wrapper, SelectTypeEnum.COUNT);
    }

    @Override
    public <T> int getCount(Class<T> tClass) {
        return (int) baseSelect(tClass, null, SelectTypeEnum.COUNT);
    }

    @Override

    public <T> List<T> getList(Class<T> tClass, QueryWrapper wrapper) {
        return (List<T>) baseSelect(tClass, wrapper, SelectTypeEnum.ALL);

    }

    @Override

    public <T> List<T> getList(Class<T> tClass) {
        return (List<T>) baseSelect(tClass, null, SelectTypeEnum.ALL);
    }

    @Override
    @CacheQuery

    public <T> T get(Class<T> tClass, QueryWrapper wrapper) {
        return (T) baseSelect(tClass, wrapper, SelectTypeEnum.ONE);
    }


    @Override
    @CacheQuery

    public <T> T getById(Class<T> tClass, Object id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(MapperUtil.parseId(tClass), id);
        return (T) baseSelect(tClass, wrapper, SelectTypeEnum.ONE);
    }

    @Override

    public <T> T get(Class<T> tClass) {
        return (T) baseSelect(tClass, null, SelectTypeEnum.ONE);
    }

    @Override
    public <T> int add(T target) {
        return baseSave(target, "INSERT");
    }


    @Override
    public <T> int addBatch(List<T> targetList) {
        return baseBatchSave(targetList, "INSERT");
    }

    @Override
    @CacheUpdate(CacheUpdateEnum.TARGET)
    public <T> int replace(T target) {
        return baseSave(target, "REPLACE");
    }

    @Override
    @CacheUpdate(CacheUpdateEnum.BATCH)
    public <T> int replaceBatch(List<T> targetList) {
        return baseBatchSave(targetList, "REPLACE");
    }

    @CacheUpdate(CacheUpdateEnum.TARGET)
    public <T> int save(T target) {
        return replace(target);
    }

    @Override
    @CacheUpdate(CacheUpdateEnum.BATCH)
    public <T> int saveBatch(List<T> targetList) {
        return replaceBatch(targetList);
    }


    public <T> List<T> selectSQL(Class<T> tClass, String sql, Object[] setSql) {
        try {
            //执行查询
            PreparedStatement pst = conn.prepareStatement(sql);
            Map<String, String> resultMap = MapperUtil.resultMap(tClass);
            //需要设值时
            for (int i = 0; setSql != null && i < setSql.length; i++) {
                pst.setObject(i + 1, setSql[i]);
            }
            pstLog = String.valueOf(pst);
            ResultSet rs = pst.executeQuery();
            return (List<T>) MapperUtil.getTarget(rs, resultMap, tClass, null);
        } catch (Exception e) {
            System.out.println("===============\n 发生错误！！！ \n SQL==>" + pstLog);
            throw new RuntimeException(e);
        }

    }


    @CacheUpdate(CacheUpdateEnum.WRAPPER)
    public <T> int delete(Class<T> tClass, QueryWrapper queryWrapper) {

        try {

            //删除必须指定条件，否则会全表删除
            if (queryWrapper == null) {
                System.out.println("删除必须指定条件，否则会全表删除!!!");
                return 0;
            }

            //条件查询，解析where sql
            HashMap<String, Object> wrapperResult = queryWrapper.getResult();
            String whereSQL = String.valueOf(wrapperResult.get("sql"));
            List<Object> setFields = CastUtil.castList(wrapperResult.get("setSQL"), Object.class);
            if ("".equals(whereSQL)) {
                System.out.println("删除必须指定条件，否则会全表删除!!!");
                return 0;
            }

            String tableName = MapperUtil.parseTableName(tClass);

            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM ").append(tableName);
            sql.append(whereSQL);

            //执行查询
            PreparedStatement pst = conn.prepareStatement(String.valueOf(sql));

            // 占位符设值
            for (int i = 0; setFields != null && i < setFields.size(); i++) {
                pst.setObject(i + 1, setFields.get(i));
            }
            pstLog = String.valueOf(pst);
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("===============\n 发生错误！！！ \n SQL==>" + pstLog);
            throw new RuntimeException(e);
        }
    }


    @Override
    public <T> int deleteById(Class<T> tClass, Integer id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(MapperUtil.parseId(tClass), id);
        return delete(tClass, wrapper);
    }

    @CacheUpdate(CacheUpdateEnum.WRAPPER)
    public <T> int update(Class<T> tClass, SetWrapper setWrapper, QueryWrapper queryWrapper) {

        try {

            //更新必须指定条件
            if (setWrapper == null || queryWrapper == null) {
                System.out.println("更新必须指定set,where");
                return 0;
            }
            //条件查询，解析where sql
            HashMap<String, Object> wrapperResult = queryWrapper.getResult();
            String whereSQL = String.valueOf(wrapperResult.get("sql"));
            List<Object> setWhereSQL = CastUtil.castList(wrapperResult.get("setSQL"), Object.class);

            //条件查询，解析set sql
            wrapperResult = setWrapper.getResult();
            String setSQL = String.valueOf(wrapperResult.get("sql"));
            List<Object> setSetSQL = CastUtil.castList(wrapperResult.get("setSQL"), Object.class);

            if ("".equals(setSQL) || "".equals(whereSQL)) {
                System.out.println("更新必须指定set,where");
                return 0;
            }
            //解析表名
            String tableName = MapperUtil.parseTableName(tClass);

            //拼接sql
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE ").append(tableName);
            sql.append(setSQL);
            sql.append(whereSQL);

            //执行查询
            PreparedStatement pst = conn.prepareStatement(String.valueOf(sql));

            // 占位符设值
            List<Object> setFields = new ArrayList<>();
            setFields.addAll(setSetSQL);
            setFields.addAll(setWhereSQL);
            for (int i = 0; i < setFields.size(); i++) {
                pst.setObject(i + 1, setFields.get(i));
            }
            pstLog = String.valueOf(pst);
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("===============\n 发生错误！！！ \n SQL==>" + pstLog);
            throw new RuntimeException(e);
        }
    }

    @CacheUpdate(CacheUpdateEnum.OTHER)
    @Override
    public <T> boolean exeSQL(String sql, Object[] setSql) {

        try {

            //执行查询
            PreparedStatement pst = conn.prepareStatement(sql);
            //设值
            for (int i = 0; setSql != null && i < setSql.length; i++) {
                pst.setObject(i + 1, setSql[i]);
            }
            pstLog = String.valueOf(pst);
            return pst.execute();

        } catch (SQLException e) {
            System.out.println("===============\n 发生错误！！！ \n SQL==>" + pstLog);
            throw new RuntimeException(e);

        }
    }


    /**
     * 查询数据 base方法
     * 通过type 返回不同的结果类型
     *
     * @param tClass  目标class
     * @param wrapper 查询构造器
     * @param type    查询类型
     * @author calwen
     * @since 2022/7/14
     */
    private <T> Object baseSelect(Class<T> tClass, QueryWrapper wrapper, SelectTypeEnum type) {
        StringBuilder sql = null;
        try {
            //解析表名
            String tableName = MapperUtil.parseTableName(tClass);
            Map<String, String> resultMap = MapperUtil.resultMap(tClass);
            //sql拼接
            sql = new StringBuilder();
            sql.append("SELECT ");
            if (Objects.equals(type, SelectTypeEnum.COUNT)) {
                sql.append(" COUNT(*) ");
            } else if (wrapper != null && !StringUtils.isNullOrEmpty(wrapper.getSelectField())) {
                //自定义 查询结果：剔除字段映射map 中 不查询的 字段
                String[] selectFields = wrapper.getSelectField().split(",");
                resultMap = resultMap.entrySet().stream().filter(entry -> {
                    String sqlField = entry.getValue();
                    return Arrays.asList(selectFields).contains(sqlField);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                sql.append(wrapper.getSelectField());
                System.out.println(resultMap);
            } else {
                sql.append(" * ");
            }

            sql.append(" FROM ").append(tableName).append(" ");

            List<Object> setFields = null;
            //有Wrapper时
            if (wrapper != null) {
                //条件查询
                HashMap<String, Object> wrapperResult = wrapper.getResult();
                String whereSQL = String.valueOf(wrapperResult.get("sql"));
                setFields = CastUtil.castList(wrapperResult.get("setSQL"), Object.class);
                if (!"".equals(whereSQL)) {
                    sql.append(whereSQL);
                }
            }
            //执行查询
            PreparedStatement pst = conn.prepareStatement(String.valueOf(sql));

            //需要设值时
            for (int i = 0; setFields != null && i < setFields.size(); i++) {
                pst.setObject(i + 1, setFields.get(i));
            }
            pstLog = String.valueOf(pst);
            ResultSet rs = pst.executeQuery();
            // 将sql结果集解析 对象或对象集
            return MapperUtil.getTarget(rs, resultMap, tClass, type);

        } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            System.out.println("===============\n 发生错误！！！ \n SQL==>" + pstLog);
            System.out.println(sql);
            throw new RuntimeException(e);
        }
    }


    private <T> int baseBatchSave(List<T> targetList, String saveType) {
        try {
            Class<?> tClass = targetList.get(0).getClass();

            int listSize = targetList.size();

            //反射获取目标信息
            Map<String, String> resultMap = MapperUtil.resultMap(tClass);
            //解析表名
            String tableName = MapperUtil.parseTableName(tClass);

            StringBuilder sql = new StringBuilder();
            if ("INSERT".equals(saveType)) {
                sql.append("INSERT INTO ");
            } else if ("REPLACE".equals(saveType)) {
                sql.append("REPLACE INTO ");
            } else {
                throw new RuntimeException("调用baseSave()方法必须指定 保存类型！！！");
            }
            sql.append(tableName);
            sql.append(" ( ");
            resultMap.forEach((k, v) -> {
                sql.append(v).append(" , ");
            });
            sql.delete(sql.lastIndexOf(","), sql.length());
            sql.append(" ) ");
            sql.append(" VALUES ");

            for (int i = 0; i < listSize; i++) {
                sql.append(" ( ");
                for (int j = 0; j < resultMap.size(); j++) {
                    if (j == resultMap.size() - 1) {
                        sql.append(" ? ");
                        break;
                    }
                    sql.append(" ?, ");
                }
                sql.append(" ) ,");
            }
            sql.deleteCharAt(sql.length() - 1);

            PreparedStatement pst = conn.prepareStatement(String.valueOf(sql));
            AtomicInteger i = new AtomicInteger(1);
            for (T target : targetList) {
                resultMap.forEach((k, v) -> {
                    try {
                        Field objField = tClass.getDeclaredField(k);
                        objField.setAccessible(true);
                        pst.setObject(i.get(), objField.get(target));
                        //i++
                        i.getAndIncrement();
                    } catch (NoSuchFieldException | IllegalAccessException | SQLException e) {
                        System.out.println("===============\n 发生错误！！！ \n SQL==>" + pstLog);
                        throw new RuntimeException(e);
                    }
                });
            }
            pstLog = String.valueOf(pst);
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("===============\n 发生错误！！！ \n SQL==>" + pstLog);
            throw new RuntimeException(e);
        }

    }

    /**
     * 保存数据  base方法
     * 提供saveType执行INSERT、REPLACE操作
     *
     * @author calwen
     * @since 2022/7/14
     */
    private <T> int baseSave(T target, String saveType) {
        try {

            Class<?> tClass = target.getClass();

            //反射获取目标信息
            Map<String, String> resultMap = MapperUtil.resultMap(tClass);
            //解析表名
            String tableName = MapperUtil.parseTableName(tClass);

            StringBuilder sql = new StringBuilder();
            if ("INSERT".equals(saveType)) {
                sql.append("INSERT INTO ");
            } else if ("REPLACE".equals(saveType)) {
                sql.append("REPLACE INTO ");
            } else {
                throw new RuntimeException("调用baseSave()方法必须指定 保存类型！！！");
            }
            sql.append(tableName);
            sql.append(" ( ");
            resultMap.forEach((k, v) -> {
                sql.append(v).append(" , ");
            });
            sql.delete(sql.lastIndexOf(","), sql.length());
            sql.append(" ) ");
            sql.append(" VALUES ");
            sql.append(" ( ");
            for (int i = 0; i < resultMap.size(); i++) {
                if (i == resultMap.size() - 1) {
                    sql.append(" ? ");
                    break;
                }
                sql.append(" ?, ");
            }
            sql.append(" ) ");
            PreparedStatement pst = conn.prepareStatement(String.valueOf(sql));
            AtomicInteger i = new AtomicInteger(1);
            resultMap.forEach((k, v) -> {
                try {
                    Field objField = tClass.getDeclaredField(k);
                    objField.setAccessible(true);
                    pst.setObject(i.get(), objField.get(target));
                    //i++
                    i.getAndIncrement();
                } catch (NoSuchFieldException | IllegalAccessException | SQLException e) {
                    System.out.println("===============\n 发生错误！！！ \n SQL==>" + pstLog);
                    throw new RuntimeException(e);
                }
            });
            pstLog = String.valueOf(pst);
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("===============\n 发生错误！！！ \n SQL==>" + pstLog);
            throw new SqlException(" 批量保存时发生sql异常 ");
        }
    }


}
