package com.axiom.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggingAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

	@Before("execution(* com.axiom.controller.*.*(..)) || execution(* com.axiom.service.*.*(..)) || execution(* com.axiom.exception..*.*(..))")
	public void logBefore(JoinPoint joinpoint) {
		LOGGER.info("{} :: {}  **Entry**", joinpoint.getSourceLocation().getWithinType().getSimpleName(),
				joinpoint.getSignature().getName());
	}

	@After("execution(* com.axiom.controller.*.*(..)) || execution(* com.axiom.service.*.*(..)) || execution(* com.axiom.exception.*.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		LOGGER.info("{} :: {}  **Exit **", joinPoint.getSourceLocation().getWithinType().getSimpleName(),
				joinPoint.getSignature().getName());
	}

	@AfterThrowing("execution(* com.axiom.controller.*.*(..)) || execution(* com.axiom.service.*.*(..)) || execution(* com.axiom.exception.*.*(..))")
	public void logException(JoinPoint joinPoint) {
		LOGGER.info("{} :: {}  **Exception** ", joinPoint.getSourceLocation().getWithinType().getSimpleName(),
				joinPoint.getSignature().getName());
	}

}