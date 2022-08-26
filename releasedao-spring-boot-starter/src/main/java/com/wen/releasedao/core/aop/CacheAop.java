package com.wen.releasedao.core.aop;

import com.wen.releasedao.config.PropertyConfig;
import com.wen.releasedao.core.annotation.CacheUpdate;
import com.wen.releasedao.core.enums.CacheUpdateEnum;
import com.wen.releasedao.core.util.MapperUtil;
import com.wen.releasedao.core.wrapper.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存AOP类
 * 执行mapper加入缓存
 * 遵循缓存旁路模式
 *
 * @author calwen
 * @since 2022/7/16
 */
@Aspect
@Slf4j
public class CacheAop {
    @Resource
    RedisTemplate<String, Object> redisTemplate;
    private final String CACHE_PREFIX = "redao:cache:";
    private final String CACHE_MAP_PREFIX = "redao:cache-map:";

    @Pointcut("@annotation(com.wen.releasedao.core.annotation.CacheQuery)")
    private void queryPointcut() {

    }

    @Pointcut("@annotation(com.wen.releasedao.core.annotation.CacheUpdate)")
    private void updatePointcut() {

    }


    @Around("queryPointcut()")
    public Object queryCache(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("++++ 进入查询缓存AOP ++++");
        Object[] args = joinPoint.getArgs();
        Class<?> targetClass = (Class<?>) args[0];
        String tableName = MapperUtil.parseTableName(targetClass);
        Class<?> aClass = args[1].getClass();
        String keySuffix;
        String cacheId;
        String idValue;
        //缓存 1.QueryWrapper 格式化字符串 。2. id
        if (Objects.equals(QueryWrapper.class, aClass)) {
            QueryWrapper wrapper = (QueryWrapper) args[1];
            keySuffix = String.valueOf(wrapper.getResult());
        } else {
            cacheId = MapperUtil.parseId(targetClass);
            idValue = String.valueOf(args[1]);
            keySuffix = cacheId + "=" + idValue;
        }
        String key = CACHE_PREFIX + tableName + ":" + keySuffix;
        Object cache = redisTemplate.opsForValue().get(key);

        if (cache != null) {
            log.debug("有缓存 ==> key: " + key);
            log.debug("value ==> " + cache);
            return cache;
        }

        Object rs = joinPoint.proceed();
        //有值放入缓存中
        if (rs != null) {
            if (rs instanceof Collection) {
                return rs;
            }
            redisTemplate.opsForValue().set(key, rs, PropertyConfig.getExpiredTime(), TimeUnit.SECONDS);
            String id = MapperUtil.parseId(rs.getClass());
            Field field = rs.getClass().getDeclaredField(id);
            field.setAccessible(true);
            idValue = String.valueOf(field.get(rs));
            key = CACHE_MAP_PREFIX + tableName + ":id:" + idValue;
            redisTemplate.opsForSet().add(key, keySuffix);
            redisTemplate.expire(key, PropertyConfig.getExpiredTime(), TimeUnit.SECONDS);
            log.debug("加入缓存 ==> key: " + key);
        }
        return rs;

    }


    @Around("updatePointcut()")
    public <T> Object updateCache(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("++++ 进入更新缓存AOP ++++");

        Object rs = joinPoint.proceed();
        //成功更新操作
        if (rs != null) {
            if (rs instanceof Boolean && Objects.equals(Boolean.FALSE, rs)) {
                return rs;
            }
            if (rs instanceof Integer && (Integer) rs == 0) {
                return rs;
            }

            Object[] args = joinPoint.getArgs();

            Object target = args[0];
            Class<?> targetClass = target.getClass();
            if (target instanceof Collection) {
                List<T> list = (List<T>) target;
                targetClass = list.get(0).getClass();
            }

            String tableName = MapperUtil.parseTableName(targetClass);

            MethodSignature ms = (MethodSignature) joinPoint.getSignature();
            Method method = ms.getMethod();
            CacheUpdate declaredAnnotation = method.getDeclaredAnnotation(CacheUpdate.class);
            CacheUpdateEnum cacheUpdate = declaredAnnotation.value();

            switch (cacheUpdate) {
                //id 删除行缓存
                case ID:
                case ENTITY:
                    delRowCache(target, tableName);
                    break;
                //删除表缓存
                case WRAPPER:
                case BATCH:
                    delTableCache(tableName);
                    break;
                //删除全部缓存
                case OTHER:
                    delAllCache();
                    break;
                default:
                    throw new RuntimeException("非法操作");
            }

        }
        return rs;
    }

    /**
     * 删除一条该缓存
     */
    private void delRowCache(Object target, String tableName) throws NoSuchFieldException, IllegalAccessException {
        Class<?> targetClass = target.getClass();
        String cacheId = MapperUtil.parseId(targetClass);
        Field field = targetClass.getDeclaredField(cacheId);
        field.setAccessible(true);
        Object idValue = field.get(target);
        String topKey = CACHE_MAP_PREFIX + tableName + ":id:" + idValue;
        Set<Object> set = redisTemplate.opsForSet().members(topKey);
        set = Optional.ofNullable(set).orElse(Collections.emptySet());
        for (Object o : set) {
            String key = CACHE_PREFIX + tableName + ":" + o;
            Boolean delete = redisTemplate.delete(key);
            System.out.println("is de: " + delete);
        }
        redisTemplate.delete(topKey);
    }

    /**
     * 删除表级 缓存
     */
    private void delTableCache(String tableName) {
        Set<String> keys = redisTemplate.keys(CACHE_PREFIX + tableName + ":*");
        Set<String> MapKeys = redisTemplate.keys(CACHE_MAP_PREFIX + tableName + ":*");
        keys = Optional.ofNullable(keys).orElse(Collections.emptySet());
        MapKeys = Optional.ofNullable(MapKeys).orElse(Collections.emptySet());
        keys.addAll(MapKeys);
        redisTemplate.delete(keys);
        log.debug("delete caches ==> " + keys);
    }

    /**
     * 删除全部缓存
     */
    private void delAllCache() {
        Set<String> keys = redisTemplate.keys(CACHE_PREFIX + "*");
        Set<String> MapKeys = redisTemplate.keys(CACHE_MAP_PREFIX + "*");
        keys = Optional.ofNullable(keys).orElse(Collections.emptySet());
        MapKeys = Optional.ofNullable(MapKeys).orElse(Collections.emptySet());
        keys.addAll(MapKeys);
        redisTemplate.delete(keys);
        log.debug("delete caches ==> " + keys);
    }


}

