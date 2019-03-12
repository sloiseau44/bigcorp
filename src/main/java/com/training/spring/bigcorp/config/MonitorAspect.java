package com.training.spring.bigcorp.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MonitorAspect {
    private final static Logger logger = LoggerFactory.getLogger(MonitorAspect.class);

    @Before("@annotation(Monitored)")
    public void logServiceBeforeCall(JoinPoint jp) {
        logger.info("Appel finder " + jp.getSignature());
    }
}
