package com.wen.releasedao.core.manager;

import com.wen.releasedao.core.exception.SqlException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * sql连接管理类
 * 获取、释放连接
 *
 * @author calwen
 * @since 2022/8/24
 */
@Component
public class ConnectionManager {
    static DataSource dataSource;
    @Resource
    ApplicationContext context;


    @PostConstruct
    public void init() {
        dataSource = context.getBean(DataSource.class);
    }

    /**
     * 保证线程安全
     */
    private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public static Connection getConn() {
        try {
            Connection conn = threadLocal.get();
            if (conn == null) {
                conn = dataSource.getConnection();
                threadLocal.set(conn);
            }
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SqlException("Connection 获取失败");
        }
    }

    public static void close() {
        try {
            Connection conn = threadLocal.get();
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SqlException("Connection 关闭失败");
        } finally {
            threadLocal.remove();
        }
    }

}
