package com.wen.releasedao.core.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 日志 业务对象（BO）
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

    private String pstStr;
    /**
     * 预编译设值
     */
    private List<Object> values;
    /**
     * 是否成功
     */
    private Boolean success = true;
    /**
     * 异常类
     */
    private String exception;
    /**
     * 异常信息
     */
    private String errorMessage;


    public void log(PreparedStatement pst, String sql, List<Object> values) {
        if (turnOnLogger()) {
            this.sql = sql;
            this.pstStr = String.valueOf(pst);
            this.values = Optional.ofNullable(values).orElse(Collections.emptyList());
        }

    }

    public void log(PreparedStatement pst, String sql, Object[] values) {
        if (turnOnLogger()) {
            this.sql = sql;
            this.pstStr = String.valueOf(pst);
            this.values = Arrays.asList(Optional.ofNullable(values).orElse(new Object[]{}));
        }

    }

    public void logData(T data) {
        if (turnOnLogger()) {
            this.data = data;
        }
    }

    public void logError(String message, Throwable e) {
        if (turnOnLogger()) {
            this.success = false;
            this.message = message;
            this.exception = e.getClass().getName();
            this.errorMessage = e.getMessage();
        }
    }

    /**
     * 是否开启日志功能
     */
    private boolean turnOnLogger() {
        return true;
    }

    public void print() {
        System.out.println(format());
    }

    public String format() {
        return "Logger{" +
                "\n classStr='" + classStr + '\'' +
                ", \n data=" + data +
                ", \n message='" + message + '\'' +
                ", \n sql='" + sql + '\'' +
                ", \n pstStr='" + pstStr + '\'' +
                ", \n values=" + values +
                ", \n success=" + success +
                ", \n exception='" + exception + '\'' +
                ", \n errorMessage='" + errorMessage + '\'' +
                '}';
    }


}
