package de.crazymonkey.finanzinformation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.crazymonkey.finanzinformation.profile.FinanzinformationProfile;

/**
 * Used to Log the execution time of the methods by the @Aspect.
 * For the definition of logging see {@link FinanzinformationProfile}
 * @author Алексей
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {

}
