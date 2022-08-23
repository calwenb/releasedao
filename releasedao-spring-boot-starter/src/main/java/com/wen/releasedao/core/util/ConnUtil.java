package com.wen.releasedao.core.util;

import com.wen.releasedao.core.exception.SqlException;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnUtil {
    public static void close(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SqlException("Connection 关闭失败");
        }
    }
}
