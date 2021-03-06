package com.gsafety.socket.backend.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Created by xiaodm on 2016/5/5.
 */
@Aspect
@Component
public class ControllerLogAspectJ {
    Logger logger = LoggerFactory.getLogger(ControllerLogAspectJ.class);


    /**
     * 前置通知：
     *
     * @param joinPoint JoinPoint
     */
    @Before("execution(public * com.gsafety.socket.webapi.controller..*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        StringBuilder args = new StringBuilder();
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            args.append(joinPoint.getArgs()[i]).append(",");
        }
        logger.info("The method：%s" ,joinPoint.getSignature().getName() ," begin, Args:%s" ,args);
    }

    /**
     * 最终通知（after advice）在连接点结束之后执行，不管返回结果还是抛出异常。
     *
     * @param joinPoint JoinPoint
     */
    @After("execution(public * com.gsafety.socket.webapi.controller..*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        logger.info("The method " + joinPoint.getSignature().getName() + " end");
    }

    /**
     * 异常通知：仅当连接点抛出异常时执行
     *
     * @param joinPoint JoinPoint
     * @param throwable Throwable
     */
    @AfterThrowing(pointcut = "execution(public * com.gsafety.socket.webapi.controller..*.*(..))", throwing = "throwable")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        logger.error("exception:%s " , throwable ," in method:%s",
                joinPoint.getSignature().getName());
    }
}