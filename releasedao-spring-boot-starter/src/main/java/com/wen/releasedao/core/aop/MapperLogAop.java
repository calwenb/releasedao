package com.wen.releasedao.core.aop;

import com.wen.releasedao.core.manager.LoggerManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

/**
 * MapperLogAop类
 * 对BaseMapper进行日志输出
 * AOP会将类中的属性初始化，并且不会设值
 * 所以不用考虑线程安全问题
 *
 * @author calwen
 * @since 2022/7/9
 */
@Slf4j
@Aspect
@Order(Integer.MAX_VALUE - 1)
public class MapperLogAop {

    @Around("execution(public * com.wen.releasedao.core.mapper.BaseMapper.*(..))")
    public Object printfLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object rs = null;
        try {
            rs = joinPoint.proceed();
        } finally {
            LoggerManager.logData(rs);
            LoggerManager.display();
            LoggerManager.remove();

        }
        return rs;

    }

}
