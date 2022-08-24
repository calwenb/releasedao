package com.wen.releasedao.core.aop;

import com.wen.releasedao.core.manager.ConnectionManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.lang.reflect.Field;
import java.sql.Connection;

@Aspect
@Slf4j
public class ConnectionAop {
    @Pointcut("execution(public * com.wen.releasedao.core.mapper.impl.BaseMapperImpl.*(..))")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Connection conn;
        try {

            Object target = joinPoint.getTarget();
            Class<?> aClass = target.getClass();
            Field field = aClass.getDeclaredField("conn");
            field.setAccessible(true);

            //获取连接
            conn = ConnectionManager.getConn();
            field.set(target, conn);

            Object rs = joinPoint.proceed();
            field.set(target, null);
            return rs;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close();
        }

    }
}
