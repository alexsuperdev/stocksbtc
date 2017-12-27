package de.crazymonkey.finanzinformation.profile;

import java.text.DecimalFormat;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Aspect
@Configuration
@EnableAspectJAutoProxy
public class FinanzinformationProfile {

	@Around("@annotation(de.crazymonkey.finanzinformation.annotation.LogExecutionTime)")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println(joinPoint.getSignature().getName() + " starting");
		long start = System.nanoTime();

		Object proceed = joinPoint.proceed();

		double executionTime = (System.nanoTime() - start);
		double sekundenRundung = Math.pow(10, 9);
		executionTime = executionTime / sekundenRundung;
		DecimalFormat format = new DecimalFormat("#0.000");
		System.out.println(joinPoint.getSignature().getName() + " finished in " + format.format(executionTime) + "s");
		return proceed;
	}
}
