package com.wen.releasedao.core.bo;

import com.wen.releasedao.config.PropertyConfig;
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
//
//
//    private Boolean turnOnLogger;
//
//
//    public void log(PreparedStatement pst, String sql, List<Object> values) {
//        if (turnOnLogger()) {
//            this.sql = sql;
//            this.pstStr = String.valueOf(pst);
//            this.values = Optional.ofNullable(values).orElse(Collections.emptyList());
//        }
//
//    }
//
//    public void log(PreparedStatement pst, String sql, Object[] values) {
//        if (turnOnLogger()) {
//            this.sql = sql;
//            this.pstStr = String.valueOf(pst);
//            this.values = Arrays.asList(Optional.ofNullable(values).orElse(new Object[]{}));
//        }
//
//    }
//
//    public void logData(T data) {
//        if (turnOnLogger()) {
//            this.data = data;
//        }
//    }
//
//    public void logError(String message, Throwable e) {
//        if (turnOnLogger()) {
//            this.success = false;
//            this.message = message;
//            this.exception = e.getClass().getName();
//            this.errorMessage = e.getMessage();
//        }
//    }
//
//
//    public void display() {
//        if (turnOnLogger()) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("\n++++ ReleaseDao Logger Start ++++ \n");
//            formatSql(sb);
//            if (success) {
//                formatResult(sb);
//            } else {
//                formatError(sb);
//            }
//            sb.append("\n\n++++ ReleaseDao Logger End ++++ \n");
//            System.out.println(sb);
//        }
//    }
//
//
//    private void formatSql(StringBuilder sb) {
//        sb.append("\n==>    Sql:  ").append(this.sql);
//        Optional.ofNullable(values).ifPresent((values) -> {
//            sb.append("\n==>  Value:  ");
//            for (Object v : values) {
//                if (v == null) {
//                    continue;
//                }
//                String className = v.getClass().getSimpleName();
//                sb.append(v).append(" (").append(className).append(")   ");
//            }
//        });
//
//        sb.append("\n==> ExeSQL: ").append(pstStr.substring(pstStr.indexOf(':') + 1));
//    }
//
//
//    private void formatResult(StringBuilder sb) {
//        if (data == null) {
//            sb.append("\n\n++ No Result ++");
//            return;
//        }
//        sb.append("\n\n++  Result ++");
//        if (data instanceof List) {
//            List<Object> list = (List) data;
//            int row = 0;
//            for (Object o : list) {
//                sb.append("\n<==  Row ").append(++row).append(":  ").append(o);
//            }
//            sb.append("\n<==  Total:  ").append(row);
//        } else if (data instanceof Integer) {
//            sb.append("\n<== Change: ").append(data);
//        } else {
//            sb.append("\n<==  Row ").append(1).append(":  ").append(data);
//            sb.append("\n<==  Total:  ").append(1);
//        }
//    }
//
//    private void formatError(StringBuilder sb) {
//        sb.append("\n\n++ An exception occurs ++");
//        sb.append("\n<==    exception:  ").append(exception);
//        sb.append("\n<==      message:  ").append(message);
//        sb.append("\n<== errorMessage:  ").append(errorMessage);
//    }
//
//
//    /**
//     * 是否开启日志功能
//     */
//    private boolean turnOnLogger() {
//        return PropertyConfig.isLogger();
//    }

}
