package com.nosbielc.crawler.central.application.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeLoggerAspect {

    @Value("${app.service.log.enabled}")
    private boolean logServiceEnabled;

    @Value("${app.repository.log.enabled}")
    private boolean logRepoEnabled;


    @Around("(@within(org.springframework.stereotype.Service) && execution(public * *(..)))" +
            " && !@annotation(com.accenture.etlcrawler.application.aspect.LoggingOff)")
    public Object aroundServices(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String service = null;
        String method = null;
        Logger logger = LogManager.getLogger(joinPoint.getTarget().getClass());
        try {
            if (logServiceEnabled) {
                Object target = joinPoint.getTarget();
                service = target.getClass().getSimpleName();
                method = joinPoint.getSignature().getName();
                return logAndProceeed(logger, joinPoint, startTime, service, method);
            } else {
                return joinPoint.proceed();
            }
        } catch (Exception ex) {
            if (logServiceEnabled) {
                logException(logger, service, method, executionTime(startTime), ex);
            }
            throw ex;
        }
    }

    @Around("execution(* org.springframework.data.repository.CrudRepository+.*(..))")
    public Object aroundRepositories(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String repository = null;
        String method = null;
        Logger logger = LogManager.getLogger(joinPoint.getTarget().getClass());
        try {
            if (logRepoEnabled) {
                repository = joinPoint.getStaticPart().getSignature().getDeclaringType().getSimpleName();
                method = joinPoint.getSignature().getName();
                return logAndProceeed(logger, joinPoint, startTime, repository, method);
            } else {
                return joinPoint.proceed();
            }
        } catch (Exception ex) {
            if (logRepoEnabled) {
                logException(logger, repository, method, executionTime(startTime), ex);
            }
            throw ex;
        }
    }

    private Object logAndProceeed(Logger logger,
                                  ProceedingJoinPoint joinPoint,
                                  long startTime,
                                  String service, String method)
            throws Throwable {
        logStartExecution(logger, service, method);
        Object returnValue = joinPoint.proceed();
        logExecution(logger, service, method, executionTime(startTime));
        return returnValue;
    }

    private void logStartExecution(Logger logger, String service, String method) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("-- Started [%s].%s ...", String.valueOf(service), method));
        }
    }

    private void logExecution(Logger logger, String service, String method, long executionTime) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("-- [%s].%s took [%dms] Execution Time.",
                    String.valueOf(service), method, executionTime));
        }

        //if (executionTime > 100L) {
        logger.warn(String.format("-- [%s].%s took [%dms] Execution Time.",
                String.valueOf(service), method, executionTime));
        //}
    }

    private void logException(Logger logger, String service, String method, long executionTime, Exception ex) {
        logger.error(String.format("-- [%s].%s took [%dms] Execution Time, and throw Exception.",
                String.valueOf(service), method, executionTime), ex);
    }

    private long executionTime(long startTime) {
        return (System.currentTimeMillis() - startTime);
    }

}
