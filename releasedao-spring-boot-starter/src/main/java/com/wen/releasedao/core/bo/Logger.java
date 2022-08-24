package com.wen.releasedao.core.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.PreparedStatement;

/**
 * 日志业务 对象
 *
 * @author calwen
 * @since 2022/8/24
 */
@Data
@Accessors(chain = true)
public class Logger<T> {

    /**
     * 类
     */
    private String classStr;
    /**
     * 获得数据
     */
    private T data;
    /**
     * 信息
     */
    private String message;
    /**
     * sql语句
     */
    private String sql;
    /**
     * 最后记录的sql
     */
    private String exeSql;

    private String pstStr;
    /**
     * 预编译设值
     */
    private Object[] values;
    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 异常类
     */
    private String exception;
    /**
     * 异常信息
     */
    private String errorMessage;

    public void logSqlAndValue(String sql, Object[] values) {
        this.setSql(sql)
                .setValues(values);
    }

    public void logPst(PreparedStatement pst) {
        this.setPstStr(String.valueOf(pst));
    }

    public void logError(String message, Exception e) {
        this.setSuccess(false)
                .setMessage(message)
                .setException(e.getClass().getName())
                .setErrorMessage(e.getMessage());
    }

    public void logData(T data) {
        this.data = data;
    }

}
