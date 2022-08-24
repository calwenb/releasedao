package com.wen.releasedao.core.manager;

import com.wen.releasedao.core.exception.SqlException;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;
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
    private static DataSource dataSource;
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

    /**
     * 获取连接<br>
     * 尝试从spring事务中获取连接,否则直接从dataSource获取将不受spring事务。
     */
    public static Connection getConn() {
        Connection conn;
        try {
            conn = threadLocal.get();
            if (conn == null) {
                conn = DataSourceUtils.doGetConnection(dataSource);
                threadLocal.set(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SqlException("Connection 获取失败");
        }
        return conn;
    }

    /**
     * 关闭连接，若为事务连接不必关闭<br>
     * DataSourceUtils.doReleaseConnection 源码：<br>
     * It's the transactional Connection: Don't close it.
     */
    public static void close() {
        try {
            Connection conn = threadLocal.get();
            if (conn != null && !conn.isClosed()) {
                DataSourceUtils.doReleaseConnection(conn, dataSource);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SqlException("Connection 关闭失败");
        } finally {
            threadLocal.remove();
        }
    }

}
