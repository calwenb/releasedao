//package com.wen.releasedao.core.util;
//
//import com.wen.releasedao.core.bo.Logger;
//
//import java.sql.PreparedStatement;
//
///**
// * @author calwen
// * @since 2022/8/24
// */
//public class LoggerUtil {
//    public static void setSqlAndValue(Logger logger, String sql, Object[] values) {
//        logger.setSql(sql);
//        logger.setValues(values);
//    }
//
//   /* public static void setPstStr(Logger logger, PreparedStatement pst) {
//        logger.setPstStr(String.valueOf(pst));
//    }*/
//
//    public static void happenError(Logger logger,String message, Exception e) {
//        logger.setSuccess(false)
//                .setMessage(message)
//                .setException(e.getClass().getName())
//                .setErrorMessage(e.getMessage());
//    }
//
//    public static <T> void setData(Logger<T> logger, T data) {
//        logger.setData(data);
//    }
//}
