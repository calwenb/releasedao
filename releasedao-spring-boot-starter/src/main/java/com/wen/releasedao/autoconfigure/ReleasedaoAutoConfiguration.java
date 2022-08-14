package com.wen.releasedao.autoconfigure;

import com.wen.releasedao.core.aop.CacheAop;
import com.wen.releasedao.core.aop.MapperLogAop;
import com.wen.releasedao.config.PropertyConfig;
import com.wen.releasedao.core.mapper.BaseMapper;
import com.wen.releasedao.core.mapper.impl.BaseMapperImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * BaseormAutoConfiguration类
 * 自动装配需要的bean。
 * 意义：
 * 1.AutoConfiguration类，@bean，@ConditionalOnProperty
 * 2.在spring.factories中指定AutoConfiguration类
 * 3.由springboot自动装配机制将第三方需要的bean放入IOC容器中
 *
 * @author calwen
 * @since 2022/7/9
 */

@Configuration
public class ReleasedaoAutoConfiguration {

    @Bean
    public BaseMapper baseMapper() {
        return new BaseMapperImpl();
    }

    @Bean
    @ConditionalOnProperty(prefix = "releasedao.config", name = "logger", havingValue = "true")
    public MapperLogAop mapperLogAop() {
        return new MapperLogAop();
    }

    @Bean
    @ConditionalOnProperty(prefix = "releasedao.config", name = "cache")
    public CacheAop cacheAop() {
        return new CacheAop();
    }

    @Bean
    public PropertyConfig propertyConfig() {
        return new PropertyConfig();
    }


}
