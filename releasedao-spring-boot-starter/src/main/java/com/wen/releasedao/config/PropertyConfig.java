package com.wen.releasedao.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 配置类
 *
 * @author calwen
 * @since 2022/8/26
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "releasedao.config")
public class PropertyConfig {
    /**
     * 默认开启驼峰式转换
     */
    private boolean camelCase = true;
    /**
     * 默认开启日志
     */
    private static boolean logger = true;
    /**
     * 默认七天的缓存时间
     */
    private long expiredTime = TimeUnit.DAYS.toSeconds(7);


    @Value("${releasedao.config.logger}")
    public void setLogger(boolean logger) {
        PropertyConfig.logger = logger;
    }


    public static boolean isLogger() {
        return logger;
    }

}
