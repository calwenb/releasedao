package com.wen.releasedao.core.aop;

import com.wen.releasedao.core.manager.ConnectionManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Field;
import java.sql.Connection;

@Aspect
@Slf4j
public class ConnectionAop {
    @Pointcut("execution(public * com.wen.releasedao.core.mapper.impl.BaseMapperImpl.*(..))")
    public void pointcut() {
    }

    @Around("execution(public * com.wen.releasedao.core.mapper.BaseMapper.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object rs = null;
        try {
            Object target = joinPoint.getTarget();
            Class<?> aClass = target.getClass();
            Field field = aClass.getDeclaredField("conn");
            field.setAccessible(true);

            //获取连接
            Connection conn = ConnectionManager.getConn();
            field.set(target, conn);

            rs = joinPoint.proceed();
            field.set(target, null);

        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            //释放连接
            ConnectionManager.close();
        }
        return rs;
    }
}
