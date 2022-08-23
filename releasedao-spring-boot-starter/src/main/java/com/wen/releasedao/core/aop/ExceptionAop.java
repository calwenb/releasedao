package com.wen.releasedao.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Slf4j
public class ExceptionAop {
    @Pointcut("execution(public * com.wen.releasedao.core.mapper.BaseMapper.*(..))")
    private void pointcut() {
    }
    @AfterThrowing(pointcut = "pointcut()",throwing = "e")
    public void a(Throwable e){

    }

}
