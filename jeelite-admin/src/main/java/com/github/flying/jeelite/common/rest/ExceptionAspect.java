package com.github.flying.jeelite.common.rest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 异常处理切面
 */
@Aspect
@Component
@Order(1)
public class ExceptionAspect {

	@AfterThrowing(throwing = "ex", pointcut = "execution(* com.github.flying.jeelite.modules..*.*(..))")
	public void afterThrowing(Exception ex) throws Throwable {
		// 捕获自定义异常
		if (StringUtils.contains(ex.getMessage(), "RestException")) {
			throw new RestException(StringUtils.substringBetween(ex.getMessage(), "RestException: ", "\r\n"));
		} else {
			throw ex;
		}
	}

}