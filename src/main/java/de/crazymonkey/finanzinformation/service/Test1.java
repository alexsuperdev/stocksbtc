//package de.crazymonkey.finanzinformation.service;
//
//import java.util.function.Supplier;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.retry.RetryCallback;
//import org.springframework.retry.RetryContext;
//import org.springframework.retry.annotation.Recover;
//import org.springframework.retry.annotation.Retryable;
//import org.springframework.retry.support.RetryTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Test1<T> {
//	private int retryCounter;
//	private int maxRetries;
//
//	@Autowired
//	private RetryTemplate retryTemplate;
//
//	public Test1(int maxRetries) {
//		this.maxRetries = maxRetries;
//	}
//
//	// Takes a function and executes it, if fails, passes the function to the retry
//	// command
//	public T run(Supplier<T> function) {
//		try {
//			return function.get();
//		} catch (Exception e) {
//			return retry(function);
//		}
//
//	}
//
//	public int getRetryCounter() {
//		return retryCounter;
//	}
//
//	private T retry(Supplier<T> function) throws RuntimeException {
//		System.out.println("FAILED - Command failed, will be retried " + maxRetries + " times.");
//		retryCounter = 0;
//		while (retryCounter < maxRetries) {
//			try {
//				return function.get();
//			} catch (Exception ex) {
//				retryCounter++;
//				System.out.println(
//						"FAILED - Command failed on retry " + retryCounter + " of " + maxRetries + " error: " + ex);
//				if (retryCounter >= maxRetries) {
//					System.out.println("Max retries exceeded.");
//					break;
//				}
//			}
//		}
//		throw new RuntimeException("Command failed on all of " + maxRetries + " retries");
//	}
//}
