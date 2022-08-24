package com.wen.releasedao.core.manager;

import com.wen.releasedao.core.bo.Logger;

/**
 * 日志记录 Manager管家
 *
 * @author calwen
 * @since 2022/8/24
 */
public class LoggerManager {
    private static final ThreadLocal<Logger> loggerLocal = new ThreadLocal<>();


    public static Logger getLogger() {
        Logger logger = loggerLocal.get();
        if (logger == null) {
            logger = new Logger();
            loggerLocal.set(logger);
        }
        return logger;
    }

    public static void remove() {
        loggerLocal.remove();
    }
}
