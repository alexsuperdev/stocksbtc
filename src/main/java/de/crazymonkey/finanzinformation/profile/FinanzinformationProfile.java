package de.crazymonkey.finanzinformation.profile;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Aspect
@Configuration
@EnableAspectJAutoProxy
public class FinanzinformationProfile {

	private static final Logger Logger = LoggerFactory.getLogger(FinanzinformationProfile.class);

	@SuppressWarnings("unchecked")
	@Around("@annotation(de.crazymonkey.finanzinformation.annotation.LogExecutionTime)")
	public <T> Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		Logger.info(joinPoint.getSignature().getName() + " starting");
		long start = System.nanoTime();
		Object proceed = joinPoint.proceed();
		double executionTime = (System.nanoTime() - start);
		double sekundenRundung = Math.pow(10, 9);
		executionTime = executionTime / sekundenRundung;
		DecimalFormat format = new DecimalFormat("#0.000");
		StringBuffer logmessage = new StringBuffer(joinPoint.getSignature().getName());
		logmessage.append(" finished in ").append(format.format(executionTime)).append(" seconds ");
		if (proceed instanceof Map) {
			Map<LocalDate, T> objects = (Map<LocalDate, T>) proceed;
			logmessage.append(" for " + objects.size() + " objects ");
		} else if (proceed instanceof List) {
			List<T> objects = (List<T>) proceed;
			logmessage.append(" for " + objects.size() + " objects ");
		}
		Logger.info(logmessage.toString());
		return proceed;
	}
}
